#!/usr/bin/env bash
# audit-instruction-refs.sh
#
# Scans all *.instructions.md files and .github/skills/*.md files for:
#   1. [ERROR] GitHub blob URLs with SHA-pinned line anchors (#L<N>) — these are brittle and
#      should be replaced with repo-relative file paths.
#   2. [WARN]  Repo-relative file paths (anchored to a known top-level directory) that no longer
#              exist in the working tree.
#
# Usage (from repo root):
#   .github/scripts/audit-instruction-refs.sh
#
# Exit codes:
#   0 — no issues found
#   1 — one or more issues found

set -euo pipefail

REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "$REPO_ROOT"

ISSUES=0

# ---------------------------------------------------------------------------
# Helper: colour output when running interactively
# ---------------------------------------------------------------------------
RED=""
YELLOW=""
RESET=""
if [ -t 1 ]; then
  RED="\033[0;31m"
  YELLOW="\033[0;33m"
  RESET="\033[0m"
fi

warn()  { echo -e "${YELLOW}WARN${RESET}  $*"; ISSUES=$((ISSUES + 1)); }
error() { echo -e "${RED}ERROR${RESET} $*"; ISSUES=$((ISSUES + 1)); }

# ---------------------------------------------------------------------------
# Collect files to audit
# ---------------------------------------------------------------------------
FILES=()
while IFS= read -r file; do
  FILES+=("$file")
done < <(
  find .github/instructions .github/skills \
    \( -name "*.instructions.md" -o -name "*.md" \) \
    -type f 2>/dev/null | sort
)

if [ ${#FILES[@]} -eq 0 ]; then
  echo "No instruction or skill files found — nothing to audit."
  exit 0
fi

echo "Auditing ${#FILES[@]} file(s)..."
echo

# ---------------------------------------------------------------------------
# Check 1: SHA-pinned GitHub blob URLs with line anchors
# ---------------------------------------------------------------------------
# Pattern: github.com/<owner>/<repo>/blob/<40-char-sha>/path#L<N>
# These are brittle — replace with repo-relative paths.
SHA_PATTERN='github\.com/[^/]+/[^/]+/blob/[0-9a-f]{40}/[^)\"[:space:]]+#L[0-9]+'

for file in "${FILES[@]}"; do
  while IFS= read -r match; do
    lineno="${match%%:*}"
    rest="${match#*:}"
    error "SHA-pinned line-anchor URL in ${file}:${lineno}"
    echo "       → ${rest}"
  done < <(grep -nE "$SHA_PATTERN" "$file" 2>/dev/null || true)
done

# ---------------------------------------------------------------------------
# Check 2: Full repo-relative file paths that no longer exist
# ---------------------------------------------------------------------------
# Only flag backtick-quoted paths that are anchored to a known top-level
# directory (app/, data/, domain/, buildSrc/, gradle/, .github/, feature/,
# common/, android/, task/).  This avoids false positives from abbreviated
# example paths, template placeholders, and single-segment filenames.
TOPLEVEL_DIRS="app|data|domain|buildSrc|gradle|\.github|feature|common|android|task"
FULL_PATH_PATTERN="\`(($TOPLEVEL_DIRS)/[a-zA-Z0-9_./-]+\.(kt|toml|kts|sh|json|yaml|yml|md|xml|pro))\`"

for file in "${FILES[@]}"; do
  while IFS= read -r match; do
    lineno="${match%%:*}"
    rest="${match#*:}"
    path_candidate=$(printf '%s\n' "$rest" | perl -ne 'if(/`((?:app|data|domain|buildSrc|gradle|\.github|feature|common|android|task)\/[^`]+)`/){print $1; exit}')
    if [ -n "$path_candidate" ]; then
      if ! [ -f "$path_candidate" ]; then
        warn "Path no longer exists in repo (${file}:${lineno}): ${path_candidate}"
      fi
    fi
  done < <(grep -nE "$FULL_PATH_PATTERN" "$file" 2>/dev/null || true)
done

# ---------------------------------------------------------------------------
# Summary
# ---------------------------------------------------------------------------
echo
if [ "$ISSUES" -eq 0 ]; then
  echo "✓ No issues found."
  exit 0
else
  echo "✗ ${ISSUES} issue(s) found. Fix them before merging."
  exit 1
fi
