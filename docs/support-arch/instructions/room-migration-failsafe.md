---
name: room-migration-failsafe
category: data-layer-migration
trigger-intent: Use when bumping AniTrendStore schema versions, adding Room migrations, validating on-device upgrades, or checkpointing local database state before risky migration work.
---

# Room Migration Fail-Safe

## Purpose

Protect user data during Room schema changes. Require backups, schema diffs, and upgrade smoke
tests before accepting a migration patch.

## Trigger Intent

- `AniTrendStore.DATABASE_SCHEMA_VERSION` changes.
- `MIGRATIONS` or `@AutoMigration` entries change.
- A Room entity change affects persisted shape, nullability, indices, or join tables.
- A major upgrade needs a reversible local validation path.

## Repo Anchors

- `data/src/main/kotlin/co/anitrend/data/android/database/AniTrendStore.kt`
- `data/src/main/kotlin/co/anitrend/data/android/database/migration/MigrationHelper.kt`
- `data/schemas/co.anitrend.data.android.database.AniTrendStore/`
- `AGENTS.md`

## Execution Steps

### 1. Inspect the migration surface

```bash
rg -n 'DATABASE_SCHEMA_VERSION|AutoMigration|fallbackToDestructiveMigration|MIGRATIONS' \
  data/src/main/kotlin/co/anitrend/data/android/database
git diff -- data/src/main/kotlin/co/anitrend/data/android/database
```

### 2. Snapshot schema JSON before accepting the change

```bash
find data/schemas/co.anitrend.data.android.database.AniTrendStore -maxdepth 1 -type f | sort
diff -u \
  data/schemas/co.anitrend.data.android.database.AniTrendStore/<from>.json \
  data/schemas/co.anitrend.data.android.database.AniTrendStore/<to>.json | sed -n '1,240p'
```

### 3. Build the data module so Room exports the new schema

```bash
./gradlew :data:assembleDebug --no-daemon --stacktrace
git diff -- data/schemas/co.anitrend.data.android.database.AniTrendStore/
```

### 4. Backup the live on-device database before upgrade

```bash
adb shell pm list packages | rg '^package:co\\.anitrend$'
adb shell run-as co.anitrend ls databases
adb shell run-as co.anitrend cp databases/anitrend-db /sdcard/Download/anitrend-db-preupgrade.sqlite
adb pull /sdcard/Download/anitrend-db-preupgrade.sqlite /tmp/anitrend-db-preupgrade.sqlite
sqlite3 /tmp/anitrend-db-preupgrade.sqlite 'PRAGMA user_version;'
sqlite3 /tmp/anitrend-db-preupgrade.sqlite '.schema'
```

If the target is an emulator, checkpoint the full device first:

```bash
adb emu avd snapshot save pre-room-migration-v<from>-to-v<to>
adb emu avd snapshot list
```

### 5. Optional containerized-state checkpoint

Use this only if the migration is being exercised in a local container harness:

```bash
docker ps --format '{{.Names}}'
docker cp <container-name>:/data/anitrend-db /tmp/anitrend-db-container-backup.sqlite
sqlite3 /tmp/anitrend-db-container-backup.sqlite 'PRAGMA user_version;'
```

### 6. Validate the migration path

```bash
./gradlew :data:connectedDebugAndroidTest --no-daemon --stacktrace
./gradlew :app:assembleDebug --no-daemon --stacktrace
```

During review, confirm all of the following:

- `fallbackToDestructiveMigration(false)` remains unchanged.
- Every version gap has either a valid manual migration or a valid auto-migration path.
- Schema JSON shows only intended column, index, or view changes.
- Join tables still preserve multiple rows and unique composite indices where required.

### 7. Roll back immediately if runtime validation fails

For emulator-based validation:

```bash
adb emu kill
```

Then restore the saved AVD snapshot from the emulator manager or rerun the emulator with the saved
snapshot selected.

For raw DB restoration:

```bash
adb push /tmp/anitrend-db-preupgrade.sqlite /sdcard/Download/anitrend-db-restore.sqlite
adb shell run-as co.anitrend cp /sdcard/Download/anitrend-db-restore.sqlite databases/anitrend-db
```

## Guardrails

- Never accept a schema bump without a new or verified path from the previous shipped version.
- Never remove `fallbackToDestructiveMigration(false)`.
- Never trust only compile success; require schema JSON diff plus runtime smoke test.
- Back up the live database before any destructive local validation.

## Deliverable

Return:

1. The old and new schema versions.
2. The schema JSON diff summary.
3. The exact backup path used.
4. The migration validation command results.
5. Any data-loss risk that still blocks merge.
