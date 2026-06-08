---
name: android-runtime-investigation
description: 'Investigate Android runtime bugs with evidence-first Argent workflows. Use for emulator targeting, runtime console/network inspection, UI hierarchy capture, optional native traffic inspection, and root-cause analysis before changing code.'
argument-hint: 'Describe the failing screen or behavior, installed variant, and whether logs, network payloads, or local data look suspicious'
---

# Android Runtime Investigation

## What This Skill Produces

- A repeatable, evidence-first workflow for Android bug investigation on emulator-first environments.
- A narrowed emulator and app target before reading logs.
- Correlated runtime evidence from React runtime logs, JS network captures, and optional native network traffic.
- A root-cause hypothesis tied to concrete evidence instead of guesswork.
- A reusable export helper for pulling Chucker database files from the app sandbox with [export-chucker-db.sh](./scripts/export-chucker-db.sh).
- A reusable query helper for inspecting exported databases with [query-chucker-db.sh](./scripts/query-chucker-db.sh).
- A quick-reference guide for common Chucker SQLite inspection flows in [chucker-sqlite-queries.md](./references/chucker-sqlite-queries.md).

## When To Use

- A screen regressed after a data-contract or UI change and the failure is only visible at runtime.
- You need to confirm whether a bug is caused by rendering, serialization, GraphQL payload shape, or stale local data.
- AniTrend debug builds are installed and the app can be inspected through Argent tools.
- The app has multiple installed flavors and you need to target the right package before collecting runtime evidence.

## Host Requirements

- Argent MCP tools are available in the current session.
- Optional fallback path: ADB + Chucker scripts remain available when app-level DB export is explicitly required.

## Procedure

1. Discover the emulator target and boot if needed.

Use Argent device discovery first:

- `argent_list-devices`
- If no ready Android target exists, boot one with `argent_boot-device` using an AVD from `avds`.

Decision point:
- If multiple emulators are booted, pick the one whose AVD name or API level most closely matches
  the failure environment. If no match is determinable, select the first device returned by
  `argent_list-devices` and state the chosen serial in the summary.

2. Launch the app explicitly on the target emulator.

- Use `argent_launch-app` with package id (`bundleId` field for Android package names).
- Prefer explicit launch over home-screen tapping to avoid launcher ambiguity.

3. Capture immediate UI baseline before interactions.

- `argent_describe` for accessibility hierarchy snapshot.
- `argent_screenshot` for visual baseline.

Quality check:
- Baseline should show current screen state and interactive targets before repro steps.

4. Reproduce the failing flow with deterministic Argent actions.

- Use `argent_gesture-tap`, `argent_gesture-swipe`, `argent_keyboard`, and `argent_button`.
- Use `argent_debugger-component-tree` for React Native target coordinates before tapping.
- Re-capture `argent_screenshot` after each major transition.

5. Connect runtime debugger and gather logs first.

- `argent_debugger-connect`
- `argent_debugger-status`
- `argent_debugger-log-registry`

Decision point:
- If logs already show a crash, request error, or serializer exception, record that line as the
  primary evidence and continue to component tree inspection so you can confirm the UI state.
- If logs are quiet but UI is wrong, move to network and state inspection.

6. Inspect JS-level network requests tied to the repro.

- `argent_view-network-logs` (list recent requests)
- `argent_view-network-request-details` (inspect suspicious request ids)

What to look for:
- GraphQL field mismatches, nullability surprises, status/error payloads, stale responses.

7. Inspect React component tree and source mapping at failure points.

- `argent_debugger-component-tree` for visible component text and tap coordinates.
- `argent_debugger-inspect-element` on suspicious coordinates to map to source file/line.

8. If JS network evidence is incomplete, collect native traffic.

- `argent_native-devtools-status` (confirm injection readiness)
- `argent_native-network-logs` (captures native-side requests beyond JS fetch)

Decision point:
- If native logs confirm backend shape mismatch, fix contract/mapping.
- If payload is correct but UI state is wrong, fix ViewModel/state/rendering logic.

9. Optional fallback: Chucker DB export path (only when explicitly needed and after steps 5-8 do
  not name the root cause).

Use this only when Argent evidence is insufficient and debug DB inspection is required.

Preferred path:

```bash
.agents/skills/android-runtime-investigation/scripts/export-chucker-db.sh --package <package-name>
```

Then query via helper:

```bash
.agents/skills/android-runtime-investigation/scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter <screen-or-endpoint-keyword>
```

10. Correlate evidence before changing code.

Use this order of confidence:
1. Runtime exception/request error in debugger logs.
2. Raw response shape from JS network logs or native network logs.
3. Mapper/serializer contract in code.
4. UI rendering assumptions.

11. Finish with a root-cause summary, not just a symptom description.

The summary should name:
- emulator target and app package inspected
- evidence sources used (debugger logs, JS network logs, native network logs, optional Chucker DB)
- failing contract, state assumption, or rendering rule
- smallest code change that addresses the root cause

## Optional Chucker Notes

When fallback DB inspection is used, keep these repo-specific checks.

### Availability check

- Verify Chucker is available in the installed build before assuming debug traffic exists.

Repo-specific context:
- AniTrend includes Chucker only in debug builds.
- Release builds use `library-no-op` and will not expose recorded traffic.

Fast checks:

```bash
adb shell dumpsys package <package-name> | rg -i "com.chuckerteam.chucker|debuggable"
adb shell run-as <package-name> ls databases
```

If `rg` is unavailable, use `grep -Ei` for the `dumpsys` filter.

Decision point:
- If `run-as` fails, you are likely not on a debuggable build or not targeting the right package.
- If no Chucker database is present, continue with logs and app data relevant to the failing module.

### Export

- Pull Chucker database files from the app sandbox without rooting the device.

Preferred path:

```bash
.agents/skills/android-runtime-investigation/scripts/export-chucker-db.sh --package <package-name>
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

If `rg` is unavailable, use `grep -Ei "chucker|http|traffic"`.

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

### Query

- Query the exported database before dropping to ad-hoc SQL.

Preferred path:

```bash
.agents/skills/android-runtime-investigation/scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter <screen-or-endpoint-keyword>
```

Useful variants:

```bash
.agents/skills/android-runtime-investigation/scripts/query-chucker-db.sh --db /path/to/chucker.db --limit 20
.agents/skills/android-runtime-investigation/scripts/query-chucker-db.sh --db /path/to/chucker.db --filter graphql --show-response
```

This helper:
- auto-detects a likely request or transactions table when possible
- selects common request and response columns only when they exist
- supports keyword filtering without hardcoding one schema version
- prints the resolved database, table, and selected columns before the rows

### Schema

- Inspect the schema before assuming table names.

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

## Completion Checklist

- The correct emulator target and package were identified explicitly.
- Runtime debugger logs were captured for the repro window.
- JS network logs were checked before code changes.
- Native network logs were checked when JS logs were insufficient.
- Chucker inspection (if used) was attempted only on a debuggable build.
- Database schema was inspected before assuming table names.
- The proposed fix is backed by runtime evidence, not only by code reading.

## Fast Invocation Examples

- "Investigate this AniTrend runtime regression with Argent debugger logs and network evidence"
- "Find the root cause of this broken media screen on device before changing serializers"
- "Use Argent JS/native network logs to confirm whether response shape or UI mapping is wrong"
