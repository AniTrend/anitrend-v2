#!/usr/bin/env bash

set -euo pipefail

usage() {
  printf 'Usage: sync-graphql-schemas.sh <anilist|anitrend|both>\n' >&2
}

if [ "$#" -ne 1 ]; then
  usage
  exit 1
fi

target="$1"
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd "$script_dir/../.." && pwd)"
data_dir="$repo_root/data"

case "$target" in
  anilist|anitrend|both) ;;
  *)
    usage
    exit 1
    ;;
esac

if [ "$target" = "anilist" ] || [ "$target" = "both" ]; then
  npx --yes @graphql-inspector/cli@5.0.7 introspect \
    https://graphql.anilist.co \
    --headers '{"user-agent":"JS GraphQL"}' \
    --write "$data_dir/schema.graphql"
fi

if [ "$target" = "anitrend" ] || [ "$target" = "both" ]; then
  npx --yes @graphql-inspector/cli@5.0.7 introspect \
    https://api.anitrend.co/graphql \
    --headers '{"User-Agent":"JS GraphQL","Accept":"*/*","Host":"api.anitrend.co"}' \
    --write "$data_dir/anitrend.schema.graphql"
fi
