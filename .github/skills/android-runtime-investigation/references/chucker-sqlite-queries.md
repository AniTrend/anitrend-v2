# Chucker SQLite Queries

Use this reference after exporting a database with [export-chucker-db.sh](../scripts/export-chucker-db.sh), or when you already have the main database file locally.

## Fast Start

Export a likely database from the device:

```bash
.github/skills/android-runtime-investigation/scripts/export-chucker-db.sh --package <package-name>
```

Query the latest likely request rows:

```bash
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh --export-dir /tmp/anitrend-chucker/<export-dir>
```

Query rows matching a screen or endpoint keyword:

```bash
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter media
```

Include response-like body columns when they exist:

```bash
.github/skills/android-runtime-investigation/scripts/query-chucker-db.sh \
  --export-dir /tmp/anitrend-chucker/<export-dir> \
  --filter recommendation \
  --show-response
```

## Schema Discovery

Always inspect the schema before assuming table names if helper auto-detection does not find a clean match.

List tables:

```bash
sqlite3 /path/to/chucker.db '.tables'
```

Show table creation SQL:

```bash
sqlite3 /path/to/chucker.db "SELECT name, sql FROM sqlite_master WHERE type='table' ORDER BY name;"
```

Show columns for one table:

```bash
sqlite3 /path/to/chucker.db "PRAGMA table_info(transactions);"
```

## Common Manual Queries

Latest rows from a known transactions table:

```bash
sqlite3 -header -column /path/to/chucker.db \
  "SELECT id, method, url, responseCode, tookMs FROM transactions ORDER BY id DESC LIMIT 20;"
```

Rows matching a keyword in a URL column:

```bash
sqlite3 -header -column /path/to/chucker.db \
  "SELECT id, method, url, responseCode FROM transactions WHERE lower(url) LIKE lower('%media%') ORDER BY id DESC LIMIT 20;"
```

Show response body-like columns when present:

```bash
sqlite3 -header -column /path/to/chucker.db \
  "SELECT id, url, responseBody FROM transactions WHERE lower(url) LIKE lower('%graphql%') ORDER BY id DESC LIMIT 5;"
```

## Investigation Patterns

- GraphQL regression: filter by `graphql`, then compare response bodies to the current fragment or model contract.
- Screen-specific failure: filter by screen language such as `media`, `relation`, or `recommendation` and check whether the matching responses are empty, null-heavy, or structurally different.
- Silent UI failure: when logcat is clean, prefer exported network evidence before changing UI assumptions.

## Decision Points

- If the helper finds multiple candidate tables, rerun it with `--table <name>`.
- If the DB has multiple main files in the export directory, rerun the helper with `--db <path>`.
- If no useful body column exists, inspect available columns first with `PRAGMA table_info(...)` instead of guessing.
