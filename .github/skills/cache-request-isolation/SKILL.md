---
name: cache-request-isolation
description: 'Diagnose and fix silent empty-UI bugs caused by CacheRequest enum collisions in AniTrend. Use when a new offline-first source variant produces blank UI with no crash, no exception, and the Room tables have zero rows. Covers: cache_log collision diagnosis, ADB Room database forensics, WAL checkpoint, Chucker traffic check, CacheRequest isolation fix, and Koin factory update pattern.'
---

# Cache Request Isolation

## When to Use

- A new offline-first data source was added, the app builds and runs without crashing, but the UI section is completely empty.
- No exceptions appear in logcat for the affected screen.
- The Room tables for the new source exist in the schema but have zero rows.
- `cache_log` has no entries for the new source's key patterns.

## Root Cause Model

AniTrend's `cache_log` table identifies cache entries by **two columns only**:

```
request  TEXT   (the CacheRequest enum alias, e.g. "media")
cache_item_id  INTEGER  (the entity ID, e.g. the media ID)
```

There is **no `key` column**. The `key` field in `CacheIdentity` subclasses is for human readability only — it does **not** differentiate entries in the database.

**Collision scenario**: If two or more source variants share the same `CacheRequest` enum value and operate on the same entity ID, the first source to run writes a `cache_log` row. Every subsequent source calls `shouldRefresh(identity, ...)`, finds that row, concludes the data is "fresh", and **silently skips the network request and all persistence**. The UI receives an empty flow from Room and shows nothing.

**Rule**: Every uniquely-fetchable resource variant must have its own `CacheRequest` enum value.

---

## Step 1 — Confirm the Symptom

Before pulling databases, verify the symptom pattern:

1. The app is installed and opens without crashing.
2. The affected UI section is empty (not loading, not erroring — just blank).
3. Check logcat:
   ```
   adb shell pidof <package>   # e.g. co.anitrend
   adb logcat --pid=<pid> *:E
   ```
   No exceptions? Proceed to Step 2.

---

## Step 2 — Pull the Room Database

```bash
PKG="co.anitrend"
mkdir -p /tmp/anitrend-db

for suffix in '' '-shm' '-wal'; do
  adb exec-out run-as "$PKG" cat "databases/anitrend-db${suffix}" \
    > "/tmp/anitrend-db/anitrend-db${suffix}" 2>/dev/null || true
done
```

Checkpoint the WAL so all pages are flushed into the main file:

```bash
sqlite3 /tmp/anitrend-db/anitrend-db "PRAGMA wal_checkpoint(TRUNCATE);"
# Expected output: 0|<N>|<N> — zero busy pages
```

---

## Step 3 — Check Row Counts in the New Tables

```bash
sqlite3 /tmp/anitrend-db/anitrend-db "
SELECT 'media_studio_connection' AS tbl, count(*) FROM media_studio_connection
UNION ALL SELECT 'media_stats',           count(*) FROM media_stats
UNION ALL SELECT 'media_relation_connection', count(*) FROM media_relation_connection
UNION ALL SELECT 'media_recommendation_connection', count(*) FROM media_recommendation_connection;
"
```

If all counts are **zero** with a non-trivial WAL checkpoint result, data never reached Room. Proceed to Step 4.

---

## Step 4 — Inspect `cache_log`

Check whether the `cache_log` has any entries for the new source's request type and ID:

```bash
# See all distinct request types currently recorded
sqlite3 /tmp/anitrend-db/anitrend-db \
  "SELECT request, cache_item_id, timestamp FROM cache_log ORDER BY timestamp DESC LIMIT 30;"
```

**Collision confirmed** when:
- A `MEDIA|<id>` row exists (written by the `Media.Detail` source), **and**
- No rows exist for the expected new request types (e.g., `MEDIA_STUDIOS|<id>`, `MEDIA_STATS|<id>`).

This means the new source variants checked `shouldRefresh("MEDIA", id)`, found the Detail entry, and skipped the network entirely.

**No entries at all** for the ID → source `invoke()` may not be called from the UI. Check ViewModel/LaunchedEffect wiring.

---

## Step 5 — Confirm via Chucker (Optional but Definitive)

```bash
mkdir -p /tmp/anitrend-chucker
for suffix in '' '-shm' '-wal'; do
  adb exec-out run-as "$PKG" cat "databases/chucker.db${suffix}" \
    > "/tmp/anitrend-chucker/chucker.db${suffix}" 2>/dev/null || true
done

sqlite3 /tmp/anitrend-chucker/chucker.db "PRAGMA wal_checkpoint(TRUNCATE);"
sqlite3 /tmp/anitrend-chucker/chucker.db \
  "SELECT id, method, path, responseCode, requestDate FROM transactions ORDER BY id DESC LIMIT 30;"
```

- **Requests absent** → collision is blocking before the network call. Fix is in `CacheRequest`.
- **Requests present with 200** → network works but `persist()` is failing. Check mapper and DAO.

---

## Step 6 — Apply the Fix

### 6a. Add new `CacheRequest` enum values

File: `data/android/src/main/kotlin/co/anitrend/data/android/cache/model/CacheRequest.kt`

Add one entry per new uniquely-fetchable resource:

```kotlin
MEDIA_STUDIOS("media_studios"),
MEDIA_STATS("media_stats"),
MEDIA_RELATIONS("media_relations"),
MEDIA_RECOMMENDATIONS("media_recommendations"),
```

### 6b. Update Koin factory — inline `MediaCache` with the distinct request

In the Koin `sourceModule` (e.g., `data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt`),
replace `cachePolicy = get<MediaCache>()` in the sidecar source factories with an inline
construction:

```kotlin
factory<MediaSource.Studios> {
    val mapper = get<MediaStudioMapper>()
    MediaSourceImpl.Studios(
        // ...other params...
        cachePolicy = MediaCache(
            localSource = cacheLocalSource(),
            request = CacheRequest.MEDIA_STUDIOS,   // <-- distinct request
        ),
        dispatcher = get(),
    )
}
```

**Do not** change the factory for the primary `Detail` source — it keeps `get<MediaCache>()` with
`CacheRequest.MEDIA`.

### 6c. Add the `CacheRequest` import at the top of the Koin Modules.kt

```kotlin
import co.anitrend.data.android.cache.model.CacheRequest
```

---

## Step 7 — Verify

1. Confirm no compile errors (the IDE language server or `./gradlew :data:compileDebugKotlin`).
2. Build and install:
   ```bash
   ./gradlew --no-daemon assembleDebug
   adb install app/build/outputs/apk/google/debug/app-google-debug.apk
   ```
3. Navigate to the affected screen, then re-pull and WAL-checkpoint the database.
4. Confirm the new tables have rows:
   ```bash
   sqlite3 /tmp/anitrend-db/anitrend-db \
     "SELECT count(*) FROM media_studio_connection;"
   ```
5. Confirm new rows appear in `cache_log`:
   ```bash
   sqlite3 /tmp/anitrend-db/anitrend-db \
     "SELECT request, cache_item_id FROM cache_log WHERE request LIKE 'MEDIA_%';"
   ```

---

## Design Rules (prevent future collisions)

| Rule | Rationale |
|---|---|
| One `CacheRequest` per independently-fetchable resource variant | `cache_log` key = `request + id`. Shared values collide silently. |
| Sidecar sources (Studios, Stats, Relations, Recommendations) get their own inline `MediaCache(request = ...)` in their Koin factory | Prevents them inheriting from the shared `get<MediaCache>()` bound to `MEDIA`. |
| The `key` field in `CacheIdentity` subclasses is documentation only | It does not affect cache lookup. Do not rely on it to differentiate sources. |
| When adding a new source variant to an existing `Cache` class, add a `CacheRequest` entry first | Failing fast at code review is cheaper than ADB forensics in production. |

---

## Related Skills

- `.github/skills/android-runtime-investigation/SKILL.md` — broader evidence-first ADB workflow
- `.github/skills/adb-device-workflow/SKILL.md` — device connection, install, logcat
- `.github/skills/room-entity-pattern/SKILL.md` — entity/DAO/mapper patterns
- `.github/skills/koin-module-wiring/SKILL.md` — Koin DI patterns for data modules
- `.github/skills/data-state-pattern/SKILL.md` — DataState and cache policy contracts
