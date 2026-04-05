#!/usr/bin/env bash

set -euo pipefail

usage() {
  cat <<'EOF'
Usage: export-chucker-db.sh --package <package-name> [--db-name <name>] [--output-dir <dir>] [--serial <serial>]

Exports a likely Chucker database from a debuggable Android app via adb run-as.

Options:
  --package     Android package name to inspect
  --db-name     Explicit database name inside the app databases directory
  --output-dir  Host directory to write copied database files into
  --serial      adb device serial when multiple devices are connected
  --help        Show this help text
EOF
}

package_name=""
db_name=""
output_dir=""
serial=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --package)
      package_name="${2:-}"
      shift 2
      ;;
    --db-name)
      db_name="${2:-}"
      shift 2
      ;;
    --output-dir)
      output_dir="${2:-}"
      shift 2
      ;;
    --serial)
      serial="${2:-}"
      shift 2
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

if [[ -z "$package_name" ]]; then
  printf '--package is required\n' >&2
  usage >&2
  exit 1
fi

if ! command -v adb >/dev/null 2>&1; then
  printf 'adb is required but not available on PATH\n' >&2
  exit 1
fi

adb_cmd=(adb)
if [[ -n "$serial" ]]; then
  adb_cmd+=( -s "$serial" )
fi

run_as_check="$(${adb_cmd[@]} shell run-as "$package_name" pwd 2>/dev/null | tr -d '\r')"
if [[ -z "$run_as_check" ]]; then
  printf 'run-as failed for package %s. Ensure this is a debuggable build and the correct package.\n' "$package_name" >&2
  exit 1
fi

if [[ -z "$db_name" ]]; then
  mapfile -t candidates < <(
    ${adb_cmd[@]} shell run-as "$package_name" ls databases 2>/dev/null |
      tr -d '\r' |
      grep -Ei 'chucker|http|traffic' || true
  )

  if [[ ${#candidates[@]} -eq 0 ]]; then
    printf 'No likely Chucker database names found for package %s.\n' "$package_name" >&2
    printf 'Available databases:\n' >&2
    ${adb_cmd[@]} shell run-as "$package_name" ls databases 2>/dev/null | tr -d '\r' >&2 || true
    exit 1
  fi

  if [[ ${#candidates[@]} -gt 1 ]]; then
    printf 'Multiple candidate databases found. Rerun with --db-name and one of:\n' >&2
    printf '  %s\n' "${candidates[@]}" >&2
    exit 1
  fi

  db_name="${candidates[0]}"
fi

timestamp="$(date +%Y%m%d-%H%M%S)"
if [[ -z "$output_dir" ]]; then
  output_dir="/tmp/anitrend-chucker/${package_name}-${timestamp}"
fi

mkdir -p "$output_dir"

copied_any=0
for suffix in '' '-wal' '-shm'; do
  target_name="${db_name}${suffix}"
  target_path="$output_dir/$target_name"

  if ${adb_cmd[@]} exec-out run-as "$package_name" cat "databases/$target_name" > "$target_path" 2>/dev/null; then
    if [[ -s "$target_path" ]]; then
      copied_any=1
    else
      rm -f "$target_path"
    fi
  fi
done

if [[ $copied_any -eq 0 ]]; then
  printf 'Unable to copy database files for %s from package %s.\n' "$db_name" "$package_name" >&2
  exit 1
fi

printf 'Exported %s for %s to %s\n' "$db_name" "$package_name" "$output_dir"

main_db="$output_dir/$db_name"
if command -v sqlite3 >/dev/null 2>&1 && [[ -s "$main_db" ]]; then
  printf 'Discovered tables:\n'
  sqlite3 "$main_db" '.tables' || true
fi
