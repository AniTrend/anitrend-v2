#!/usr/bin/env bash

set -euo pipefail

usage() {
  cat <<'EOF'
Usage: query-chucker-db.sh (--db <path> | --export-dir <dir>) [--filter <text>] [--limit <n>] [--table <name>] [--show-response]

Queries an exported Chucker SQLite database and prints recent request or response rows.

Options:
  --db             Path to the main SQLite database file
  --export-dir     Directory created by export-chucker-db.sh
  --filter         Case-insensitive text filter applied to likely request or response columns
  --limit          Number of rows to print (default: 10)
  --table          Explicit table name when auto-detection is ambiguous
  --show-response  Include response body-like columns when present
  --help           Show this help text
EOF
}

db_path=""
export_dir=""
filter_text=""
limit_rows=10
table_name=""
show_response=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --db)
      db_path="${2:-}"
      shift 2
      ;;
    --export-dir)
      export_dir="${2:-}"
      shift 2
      ;;
    --filter)
      filter_text="${2:-}"
      shift 2
      ;;
    --limit)
      limit_rows="${2:-}"
      shift 2
      ;;
    --table)
      table_name="${2:-}"
      shift 2
      ;;
    --show-response)
      show_response=1
      shift
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    *)
      printf 'Unknown argument: %s\n' "$1" >&2
      usage >&2
      exit 1
      ;;
  esac
done

if ! command -v sqlite3 >/dev/null 2>&1; then
  printf 'sqlite3 is required but not available on PATH\n' >&2
  exit 1
fi

if [[ -z "$db_path" && -n "$export_dir" ]]; then
  if [[ ! -d "$export_dir" ]]; then
    printf 'Export directory not found: %s\n' "$export_dir" >&2
    exit 1
  fi

  mapfile -t db_candidates < <(
    find "$export_dir" -maxdepth 1 -type f ! -name '*.db-wal' ! -name '*.db-shm' ! -name '*-wal' ! -name '*-shm' | sort
  )

  if [[ ${#db_candidates[@]} -eq 0 ]]; then
    printf 'No SQLite database files found in %s\n' "$export_dir" >&2
    exit 1
  fi

  if [[ ${#db_candidates[@]} -gt 1 ]]; then
    printf 'Multiple database files found. Rerun with --db and one of:\n' >&2
    printf '  %s\n' "${db_candidates[@]}" >&2
    exit 1
  fi

  db_path="${db_candidates[0]}"
fi

if [[ -z "$db_path" ]]; then
  printf 'Either --db or --export-dir is required\n' >&2
  usage >&2
  exit 1
fi

if [[ ! -f "$db_path" ]]; then
  printf 'Database file not found: %s\n' "$db_path" >&2
  exit 1
fi

if ! [[ "$limit_rows" =~ ^[0-9]+$ ]]; then
  printf '--limit must be a positive integer\n' >&2
  exit 1
fi

sql_escape() {
  printf "%s" "$1" | sed "s/'/''/g"
}

table_exists() {
  local name="$1"
  sqlite3 "$db_path" "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='$(sql_escape "$name")';" | grep -q '^1$'
}

if [[ -z "$table_name" ]]; then
  mapfile -t table_candidates < <(
    sqlite3 "$db_path" "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;" |
      grep -Ei 'transaction|request|response|http|network' || true
  )

  if [[ ${#table_candidates[@]} -eq 0 ]]; then
    printf 'Unable to auto-detect a request table. Available tables:\n' >&2
    sqlite3 "$db_path" '.tables' >&2
    exit 1
  fi

  if printf '%s\n' "${table_candidates[@]}" | grep -qx 'transactions'; then
    table_name='transactions'
  elif [[ ${#table_candidates[@]} -eq 1 ]]; then
    table_name="${table_candidates[0]}"
  else
    printf 'Multiple candidate tables found. Rerun with --table and one of:\n' >&2
    printf '  %s\n' "${table_candidates[@]}" >&2
    exit 1
  fi
fi

if ! table_exists "$table_name"; then
  printf 'Table not found in database: %s\n' "$table_name" >&2
  exit 1
fi

mapfile -t raw_columns < <(sqlite3 "$db_path" "PRAGMA table_info(\"$table_name\");")
if [[ ${#raw_columns[@]} -eq 0 ]]; then
  printf 'Could not inspect columns for table %s\n' "$table_name" >&2
  exit 1
fi

columns=()
for row in "${raw_columns[@]}"; do
  columns+=("$(printf '%s' "$row" | awk -F'|' '{print $2}')")
done

has_column() {
  local target="$1"
  local column
  for column in "${columns[@]}"; do
    if [[ "$column" == "$target" ]]; then
      return 0
    fi
  done
  return 1
}

pick_first_column() {
  local candidate
  for candidate in "$@"; do
    if has_column "$candidate"; then
      printf '%s\n' "$candidate"
      return 0
    fi
  done
  return 1
}

select_columns=()
for preferred in id method url path host scheme responseCode tookMs protocol ssl requestDate responseDate duration; do
  if has_column "$preferred"; then
    select_columns+=("$preferred")
  fi
done

if [[ $show_response -eq 1 ]]; then
  for preferred in responseBody response_body responsePayload response payload body; do
    if has_column "$preferred"; then
      select_columns+=("$preferred")
    fi
  done
fi

if [[ ${#select_columns[@]} -eq 0 ]]; then
  fallback_count=0
  for column in "${columns[@]}"; do
    select_columns+=("$column")
    fallback_count=$((fallback_count + 1))
    if [[ $fallback_count -ge 8 ]]; then
      break
    fi
  done
fi

order_column="$(pick_first_column requestDate responseDate id createdAt updatedAt date || true)"
if [[ -z "$order_column" ]]; then
  order_column='rowid'
fi

where_clauses=()
if [[ -n "$filter_text" ]]; then
  escaped_filter="$(sql_escape "$filter_text")"
  for candidate in url path host requestBody responseBody response_body responsePayload payload body error tag; do
    if has_column "$candidate"; then
      where_clauses+=("lower(coalesce(cast(\"$candidate\" as text), '')) LIKE lower('%$escaped_filter%')")
    fi
  done

  if [[ ${#where_clauses[@]} -eq 0 ]]; then
    printf 'No filterable text columns found in %s. Available columns:\n' "$table_name" >&2
    printf '  %s\n' "${columns[@]}" >&2
    exit 1
  fi
fi

select_sql=''
for column in "${select_columns[@]}"; do
  if [[ -n "$select_sql" ]]; then
    select_sql+=', '
  fi
  select_sql+="\"$column\""
done

query="SELECT $select_sql FROM \"$table_name\""
if [[ ${#where_clauses[@]} -gt 0 ]]; then
  where_sql=''
  for clause in "${where_clauses[@]}"; do
    if [[ -n "$where_sql" ]]; then
      where_sql+=" OR "
    fi
    where_sql+="$clause"
  done
  query+=" WHERE $where_sql"
fi
query+=" ORDER BY \"$order_column\" DESC LIMIT $limit_rows;"

printf 'Database: %s\n' "$db_path"
printf 'Table: %s\n' "$table_name"
printf 'Columns: %s\n' "$select_sql"
if [[ -n "$filter_text" ]]; then
  printf 'Filter: %s\n' "$filter_text"
fi
printf '\n'

sqlite3 -header -column "$db_path" "$query"
