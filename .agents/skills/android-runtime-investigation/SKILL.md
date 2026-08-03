---
name: android-runtime-investigation
description: 'Investigate Android runtime bugs with evidence-first workflows. Use for emulator targeting, logcat/AndroidRuntime/Timber inspection, Chucker HTTP/GraphQL payloads, UIAutomator hierarchy capture, Room database forensics, and root-cause analysis before changing code.'
argument-hint: 'Describe the failing screen or behavior, installed variant, and whether logs, network payloads, or local data look suspicious'
---

# Android Runtime Investigation

## What This Skill Produces

- A repeatable, evidence-first workflow for Android bug investigation on emulator-first environments.
- A narrowed emulator and app target before reading logs.
- Correlated runtime evidence from logcat/AndroidRuntime/Timber, Chucker HTTP payloads, and
  UI hierarchy captures.
- A root-cause hypothesis tied to concrete evidence instead of guesswork.
- A reusable export helper for pulling Chucker database files from the app sandbox with [export-chucker-db.sh](./scripts/export-chucker-db.sh).
- A reusable query helper for inspecting exported databases with [query-chucker-db.sh](./scripts/query-chucker-db.sh).
- A quick-reference guide for common Chucker SQLite inspection flows in [chucker-sqlite-queries.md](./references/chucker-sqlite-queries.md).

## When To Use

- A screen regressed after a data-contract or UI change and the failure is only visible at runtime.
- You need to confirm whether a bug is caused by rendering, serialization, GraphQL payload shape, or stale local data.
- Debug builds are installed and the app can be inspected through ADB.
- The app has multiple installed flavors and you need to target the right package before collecting runtime evidence.

## Host Requirements

- ADB is available and a device or emulator is connected.
- The target build is a debug variant that includes Chucker.
- Optional: Argent MCP tools are available in the current session for device discovery and
  screenshot capture.

## Procedure

1. Discover the emulator target and boot if needed.

```bash
adb devices -l
```

Decision point:
- If no Android target exists, boot an emulator or connect a device.
- If multiple devices are connected, pick the one whose AVD name or API level most closely matches
  the failure environment. If no match is determinable, select the first device returned by
  `adb devices -l` and state the chosen serial in the summary.

2. Launch the app explicitly on the target emulator.

```bash
adb -s <serial> shell monkey -p <package-name> -c android.intent.category.LAUNCHER 1
```

3. Capture immediate UI baseline before interactions.

```bash
adb -s <serial> shell uiautomator dump /sdcard/window_dump.xml
adb -s <serial> pull /sdcard/window_dump.xml /tmp/window_dump.xml
adb -s <serial> exec-out screencap -p > /tmp/screen.png
```

Quality check:
- Baseline should show current screen state and interactive targets before repro steps.

4. Reproduce the failing flow with deterministic actions.

- Use `adb shell input tap <x> <y>` and `adb shell input swipe <x1> <y1> <x2> <y2> <duration>`.
- Re-derive coordinates from a fresh `uiautomator dump` before each tap sequence.
- Re-capture a screenshot after each major transition.

5. Capture logcat evidence.

```bash
adb -s <serial> logcat -c
adb -s <serial> logcat | grep -E "AndroidRuntime|FATAL EXCEPTION|Timber|anitrend"
```

Decision point:
- If logs show a crash, request error, or serializer exception, record that line as the
  primary evidence and continue to UI inspection so you can confirm the screen state.
- If logs are quiet but UI is wrong, move to network and state inspection.

6. Export and inspect Chucker HTTP/GraphQL payloads for the repro window.

Use the export helper to pull the Chucker database:

```bash
./scripts/export-chucker-db.sh --package <package-name>
```

Then query for the relevant endpoint or screen:

```bash
./scripts/query-chucker-db.sh \
  --export-dir /tmp/<package-name>-chucker/<export-dir> \
  --filter <screen-or-endpoint-keyword>
```

What to look for:
- If graphql based request/responses: GraphQL field mismatches, nullability surprises, status/error payloads, stale responses.
- If rest based request/responses: property field mismatching, nullability surprises, status/error payloads, stale responses.

7. Capture UI hierarchy and screenshot at the failure point.

```bash
adb -s <serial> shell uiautomator dump /sdcard/window_dump.xml
adb -s <serial> pull /sdcard/window_dump.xml /tmp/window_dump.xml
adb -s <serial> exec-out screencap -p > /tmp/screen.png
```

- Use `grep` against the XML dump to verify expected labels, text content, or missing nodes.
- Cross-reference the screenshot with the dump to confirm layout and visibility.

8. Inspect database when cache or local persistence is suspected (Room/Standard SQL support only).

```bash
adb -s <serial> shell run-as <package-name> ls databases
adb -s <serial> shell run-as <package-name> cat databases/<database-name> > /tmp/<database-name>
```

- Use `sqlite3 /tmp/<database-name>.db ".tables"` to list tables.
- Query the relevant entity or connection table to check row count, nullability, and expected data.
- If WAL mode may hide recent writes, copy the `-wal` and `-shm` sidecar files as well.

Decision point:
- If the local table has zero rows but the remote payload was correct, the cache identity or
  mapper persistence path is likely wrong.
- If the local table has stale data, the cache policy or refresh gating is likely wrong.

9. Correlate evidence before changing code.

Use this order of confidence:
1. Runtime exception or request error in logcat.
2. Raw response shape from Chucker HTTP/GraphQL payloads.
3. Room database row state confirming persistence or staleness.
4. Mapper/serializer contract in code.
5. UI rendering assumptions from the hierarchy dump and screenshot.

10. Finish with a root-cause summary, not just a symptom description.

The summary should name:
- emulator target and app package inspected
- evidence sources used (logcat, Chucker DB, UIAutomator dump, Room DB)
- failing contract, state assumption, or rendering rule
- smallest code change that addresses the root cause

## Optional Chucker Notes

When Chucker DB inspection is used, keep these repo-specific checks.

### Availability check

- Verify Chucker is available in the installed build before assuming debug traffic exists.

Repo-specific context:
- AniTrend includes Chucker only in debug builds.
- Release builds use `library-no-op` and will not expose recorded traffic.

Fast checks:

```bash
adb shell dumpsys package <package-name> | grep -Ei "com.chuckerteam.chucker|debuggable"
adb shell run-as <package-name> ls databases
```

Decision point:
- If `run-as` fails, you are likely not on a debuggable build or not targeting the right package.
- If no Chucker database is present, continue with logs and app data relevant to the failing module.

### Export

- Pull Chucker database files from the app sandbox without rooting the device.

Preferred path:

```bash
./scripts/export-chucker-db.sh --package <package-name>
```

This helper:
- verifies `run-as` access
- discovers likely Chucker database names dynamically
- copies the main database plus `-wal` and `-shm` sidecars when present
- prints the output directory and, when `sqlite3` is available, the discovered tables

Then copy the main database and sidecar files to the host:

```bash
mkdir -p /tmp/<package-name>-chucker
for suffix in '' '-wal' '-shm'; do
  adb exec-out run-as <package-name> cat "databases/<db-name>${suffix}" > "/tmp/<package-name>/<db-name>${suffix}" 2>/dev/null || true
done
```

Quality check:
- The copied main database file should be non-empty.
- Keep the `-wal` file when present so recent writes are not lost.

### Query

- Query the exported database before dropping to ad-hoc SQL.

Preferred path:

```bash
./scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter <screen-or-endpoint-keyword>
```

Useful variants:

```bash
./scripts/query-chucker-db.sh --db /path/to/chucker.db --limit 20
./scripts/query-chucker-db.sh --db /path/to/chucker.db --filter graphql --show-response
```

This helper:
- auto-detects a likely request or transactions table when possible
- selects common request and response columns only when they exist
- supports keyword filtering without hardcoding one schema version
- prints the resolved database, table, and selected columns before the rows

### Schema

- Inspect the schema before assuming table names.

```bash
sqlite3 /tmp/<package-name>-chucker/<db-name> ".tables"
sqlite3 /tmp/<package-name>-chucker/<db-name> "SELECT name, sql FROM sqlite_master WHERE type='table';"
```

Decision point:
- If you already know the Chucker version and table names, query them directly.
- Otherwise inspect the schema first and only then write targeted queries.
- If the helper reports multiple candidate database names, rerun it with `--db-name <name>`.

9. Query recent requests and responses that match the failing screen.

Example investigation pattern:

```bash
sqlite3 /tmp/<package-name>-chucker/<db-name> "SELECT * FROM <transactions-table> ORDER BY id DESC LIMIT 20;"
sqlite3 /tmp/<package-name>-chucker/<db-name> "SELECT * FROM <response-table> WHERE <foreign-key> = <id>;"
```

What to look for:
- missing or renamed fields after a GraphQL fragment change
- nullability differences between expected and actual payloads
- backend errors that never surfaced clearly in UI logs
- stale cached responses that do not match the current mapper or serializer assumptions

See [chucker-sqlite-queries.md](./references/chucker-sqlite-queries.md) for common `sqlite3` patterns when the helper is not enough.

## Completion Checklist

- The correct emulator target and package were identified explicitly.
- Runtime logcat evidence was captured for the repro window, including `AndroidRuntime` and `Timber` (if part of project) output.
- Chucker HTTP/GraphQL payloads were inspected when response shape mattered.
- Room database state was inspected when cache or local persistence was suspected.
- UIAutomator dump and screenshot were captured when rendering or visibility was questioned.
- Chucker inspection (if used) was attempted only on a debuggable build.
- Database schema was inspected before assuming table names.
- The proposed fix is backed by runtime evidence, not only by code reading.

## Fast Invocation Examples

- "Investigate this AniTrend runtime regression with logcat, Chucker, UIAutomator, and Room evidence"
- "Find the root cause of this broken media screen on device before changing serializers"
- "Confirm whether this broken media screen is caused by payload shape, persistence, or UI mapping"
