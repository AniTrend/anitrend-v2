---
name: android-runtime-investigation
description: 'Investigate Android runtime bugs with evidence-first ADB workflows. Use for pid-scoped logcat capture, package selection across flavors, Chucker database inspection, GraphQL or serialization regressions, and root-cause analysis before changing code.'
argument-hint: 'Describe the failing screen or behavior, installed variant, and whether logs, network payloads, or local data look suspicious'
---

# Android Runtime Investigation

## What This Skill Produces

- A repeatable, evidence-first workflow for Android bug investigation on a real device or emulator.
- A narrowed package and process target before reading logs.
- Correlated runtime evidence from pid-scoped logcat and, when available, Chucker's recorded debug traffic.
- A root-cause hypothesis tied to concrete evidence instead of guesswork.
- A reusable export helper for pulling Chucker database files from the app sandbox with [export-chucker-db.sh](./scripts/export-chucker-db.sh).
- A reusable query helper for inspecting exported databases with [query-chucker-db.sh](./scripts/query-chucker-db.sh).
- A quick-reference guide for common Chucker SQLite inspection flows in [chucker-sqlite-queries.md](./references/chucker-sqlite-queries.md).

## When To Use

- A screen regressed after a data-contract or UI change and the failure is only visible at runtime.
- You need to confirm whether a bug is caused by rendering, serialization, GraphQL payload shape, or stale local data.
- AniTrend debug builds are installed and Chucker may contain the raw request and response that drove the bad UI state.
- The app has multiple installed flavors and you need to target the right package before collecting logs.

## Procedure

1. Reproduce on a debug build and identify the installed AniTrend package.

```bash
adb shell pm list packages | grep anitrend
```

Decision point:
- If both `co.anitrend` and `com.mxt.anitrend` are present, pick the package that matches the variant you just installed.
- If you are unsure which package is active, launch the intended app and use `pidof` in the next step.

2. Clear old logs, launch or reproduce the failing flow, and resolve the active process.

```bash
adb logcat -c
adb shell pidof -s <package-name>
```

Decision point:
- If a PID exists, continue with pid-scoped logs.
- If the app dies too quickly to keep a PID, collect `adb logcat -d` immediately after repro and filter aggressively.

3. Pull pid-scoped logs first so unrelated system noise does not bury the failure.

```bash
pid=$(adb shell pidof -s <package-name> | tr -d '\r')
adb logcat -d --pid="$pid"
```

Recommended filtering:

```bash
adb logcat -d --pid="$pid" | rg -i "AndroidRuntime|FATAL EXCEPTION|Exception|GraphQL|serialization|JsonDecodingException|RequestError|anitrend"
```

Quality check:
- The log sample should tell you whether the failure is a crash, silent request failure, empty-state rendering issue, or serializer mismatch.

4. If logs are noisy or inconclusive, narrow to the feature language of the failing screen.

Examples:
- `relation|recommendation|media`
- `graphql|request|response`
- `serialization|JsonDecodingException|kotlinx.serialization`

Decision point:
- If the bug already has a stack trace or request error, follow that evidence before looking at local databases.
- If the UI is wrong but logs are clean, inspect recorded network responses next.

5. Verify Chucker is available in the installed build before assuming debug traffic exists.

Repo-specific context:
- AniTrend includes Chucker only in debug builds.
- Release builds use `library-no-op` and will not expose recorded traffic.

Fast checks:

```bash
adb shell dumpsys package <package-name> | rg -i "com.chuckerteam.chucker|debuggable"
adb shell run-as <package-name> ls databases
```

Decision point:
- If `run-as` fails, you are likely not on a debuggable build or not targeting the right package.
- If no Chucker database is present, continue with logs and app data relevant to the failing module.

6. Pull Chucker database files from the app sandbox without rooting the device.

Preferred path:

```bash
.github/skills/android-runtime-investigation/scripts/export-chucker-db.sh --package <package-name>
```

This helper:
- verifies `run-as` access
- discovers likely Chucker database names dynamically
- copies the main database plus `-wal` and `-shm` sidecars when present
- prints the output directory and, when `sqlite3` is available, the discovered tables

First discover likely database names dynamically:

```bash
adb shell run-as <package-name> ls databases | rg -i "chucker|http|traffic"
```

Then copy the main database and sidecar files to the host:

```bash
mkdir -p /tmp/anitrend-chucker
for suffix in '' '-wal' '-shm'; do
  adb exec-out run-as <package-name> cat "databases/<db-name>${suffix}" > "/tmp/anitrend-chucker/<db-name>${suffix}" 2>/dev/null || true
done
```

Quality check:
- The copied main database file should be non-empty.
- Keep the `-wal` file when present so recent writes are not lost.

7. Query the exported database before dropping to ad-hoc SQL.

Preferred path:

```bash
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter <screen-or-endpoint-keyword>
```

Useful variants:

```bash
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh --db /path/to/chucker.db --limit 20
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh --db /path/to/chucker.db --filter graphql --show-response
```

This helper:
- auto-detects a likely request or transactions table when possible
- selects common request and response columns only when they exist
- supports keyword filtering without hardcoding one schema version
- prints the resolved database, table, and selected columns before the rows

8. Inspect the schema before assuming table names.

```bash
sqlite3 /tmp/anitrend-chucker/<db-name> ".tables"
sqlite3 /tmp/anitrend-chucker/<db-name> "SELECT name, sql FROM sqlite_master WHERE type='table';"
```

Decision point:
- If you already know the Chucker version and table names, query them directly.
- Otherwise inspect the schema first and only then write targeted queries.
- If the helper reports multiple candidate database names, rerun it with `--db-name <name>`.

9. Query recent requests and responses that match the failing screen.

Example investigation pattern:

```bash
sqlite3 /tmp/anitrend-chucker/<db-name> "SELECT * FROM <transactions-table> ORDER BY id DESC LIMIT 20;"
sqlite3 /tmp/anitrend-chucker/<db-name> "SELECT * FROM <response-table> WHERE <foreign-key> = <id>;"
```

What to look for:
- missing or renamed fields after a GraphQL fragment change
- nullability differences between expected and actual payloads
- backend errors that never surfaced clearly in UI logs
- stale cached responses that do not match the current mapper or serializer assumptions

See [chucker-sqlite-queries.md](./references/chucker-sqlite-queries.md) for common `sqlite3` patterns when the helper is not enough.

10. Correlate evidence before changing code.

Use this order of confidence:
1. Runtime crash or request error in pid-scoped logs.
2. Raw response shape from Chucker or other debug data store.
3. Mapper or serializer contract in code.
4. UI rendering assumptions.

Decision point:
- If the raw payload contradicts the local model, fix the contract or mapping.
- If the payload is correct but UI is wrong, fix rendering logic.
- If both logs and payload are clean, inspect local persistence or state selection next.

11. Finish with a root-cause summary, not just a symptom description.

The summary should name:
- the exact package and variant inspected
- the evidence source used (`logcat`, Chucker DB, local DB, or combination)
- the failing contract, state assumption, or rendering rule
- the smallest code change that addresses the root cause

## Completion Checklist

- The correct installed package was identified explicitly.
- Logs were captured with `--pid` when the app process stayed alive.
- Chucker inspection was attempted only on a debuggable build.
- Database schema was inspected before assuming table names.
- The proposed fix is backed by runtime evidence, not only by code reading.

## Fast Invocation Examples

- "Investigate this AniTrend runtime regression with pid-scoped logcat and Chucker if available"
- "Find the root cause of this broken media screen on device before changing serializers"
- "Use adb plus Chucker DB inspection to confirm whether the response shape or UI mapping is wrong"
