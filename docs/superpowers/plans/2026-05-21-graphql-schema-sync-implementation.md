# GraphQL Schema Sync Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace live AniTrend GraphQL introspection with a repo-owned schema sync script and workflow that refreshes `data/schema.graphql` and `data/anitrend.schema.graphql`, while aligning automation naming with the repository contribution conventions.

**Architecture:** Keep the schema generation logic in one parameterized shell script under `.github/scripts/`, and keep the workflow focused on trigger normalization, runtime setup, credentials, and PR creation. Reuse the existing repository automation pattern of app-token checkout plus `peter-evans/create-pull-request`, and remove the temporary `data/package.json`-based tooling in favor of a workflow-local CLI install.

**Tech Stack:** GitHub Actions, shell scripting, Node.js with `@graphql-inspector/cli`, GraphQL schema introspection, repository docs in Markdown.

---

## File Structure

- Modify: `data/graphql.config.yml`
  - Disable AniTrend live introspection and preserve the correct `Host` header spelling.
- Create: `.github/scripts/sync-graphql-schemas.sh`
  - Single entrypoint that accepts `anilist`, `anitrend`, or `both` and writes schema files into `data/`.
- Modify: `.github/workflows/graphql-schema-sync.yml`
  - Convert the draft workflow into the final trigger contract for `workflow_dispatch`, `repository_dispatch`, and `schedule`.
- Delete: `data/package.json`
  - Remove the temporary local-only CLI wrapper introduced during exploration.
- Delete: `data/package-lock.json`
  - Remove the lockfile paired with the temporary package manifest.
- Modify: `data/schema.graphql`
  - Refresh generated AniList schema.
- Modify: `data/anitrend.schema.graphql`
  - Refresh generated AniTrend schema.

### Task 1: Finalize GraphQL Config And Remove Temporary Tooling

**Files:**
- Modify: `data/graphql.config.yml:14-23`
- Delete: `data/package.json`
- Delete: `data/package-lock.json`

- [ ] **Step 1: Confirm the current config and temporary tooling state**

Run: `git diff -- data/graphql.config.yml data/package.json data/package-lock.json`

Expected: the diff shows the temporary local package tooling and the current AniTrend endpoint block still using `introspect: true` if the draft change has not yet been finalized.

- [ ] **Step 2: Write the minimal config change**

Update the AniTrend endpoint block in `data/graphql.config.yml` so it matches this shape:

```yaml
    AniTrend:
      url: https://api.anitrend.co/graphql
      schema: anitrend.schema.graphql
      headers:
        User-Agent: "JS GraphQL"
        Accept: "*/*"
        Host: "api.anitrend.co"
        Accept-Encoding: "gzip, deflate, br"
        Content-Type: "application/json"
      introspect: false
```

Delete `data/package.json` and `data/package-lock.json` entirely once the workflow-local script approach is ready to replace them.

- [ ] **Step 3: Verify the config change and tooling cleanup**

Run: `git diff -- data/graphql.config.yml data/package.json data/package-lock.json`

Expected:
- `data/graphql.config.yml` shows only the `Host` normalization and `introspect: false` for AniTrend.
- `data/package.json` and `data/package-lock.json` appear as deletions, not modifications.

- [ ] **Step 4: Commit the config cleanup slice**

```bash
git add data/graphql.config.yml data/package.json data/package-lock.json
git commit -m "chore(graphql): remove temporary schema sync package tooling"
```

### Task 2: Add The Parameterized Schema Sync Script

**Files:**
- Create: `.github/scripts/sync-graphql-schemas.sh`

- [ ] **Step 1: Write the script file with strict shell behavior and target parsing**

Create `.github/scripts/sync-graphql-schemas.sh` with this structure:

```bash
#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(git rev-parse --show-toplevel)"
DATA_DIR="$ROOT_DIR/data"
TARGET="${1:-}"

case "$TARGET" in
  anilist|anitrend|both) ;;
  *)
    printf 'Usage: %s <anilist|anitrend|both>\n' "${0##*/}" >&2
    exit 1
    ;;
esac

npx --yes @graphql-inspector/cli introspect \
  https://graphql.anilist.co \
  --headers '{"user-agent":"JS GraphQL"}' \
  --write "$DATA_DIR/schema.graphql"

npx --yes @graphql-inspector/cli introspect \
  https://api.anitrend.co/graphql \
  --headers '{"User-Agent":"JS GraphQL","Accept":"*/*","Host":"api.anitrend.co"}' \
  --write "$DATA_DIR/anitrend.schema.graphql"
```

Do not leave it in this unconditional form. Wrap the AniList call behind `anilist|both` and the AniTrend call behind `anitrend|both`, keeping AniList first when `both` is requested.

- [ ] **Step 2: Make the script executable**

Run: `chmod +x .github/scripts/sync-graphql-schemas.sh`

Expected: no output, file mode updated in git.

- [ ] **Step 3: Verify invalid-input behavior**

Run: `bash .github/scripts/sync-graphql-schemas.sh invalid`

Expected: exit code `1` and output matching `Usage: sync-graphql-schemas.sh <anilist|anitrend|both>`.

- [ ] **Step 4: Execute the narrow-path schema refreshes**

Run each command separately:

```bash
bash .github/scripts/sync-graphql-schemas.sh anilist
bash .github/scripts/sync-graphql-schemas.sh anitrend
```

Expected:
- first command only updates `data/schema.graphql`
- second command only updates `data/anitrend.schema.graphql`

- [ ] **Step 5: Execute the combined schema refresh**

Run: `bash .github/scripts/sync-graphql-schemas.sh both`

Expected: both schema files regenerate successfully, with AniList refreshed before AniTrend.

- [ ] **Step 6: Commit the script slice**

```bash
git add .github/scripts/sync-graphql-schemas.sh data/schema.graphql data/anitrend.schema.graphql
git commit -m "ci(graphql): add parameterized schema sync script"
```

### Task 3: Finalize The Workflow Trigger Contract And PR Automation

**Files:**
- Modify: `.github/workflows/graphql-schema-sync.yml`

- [ ] **Step 1: Replace the draft trigger block with the final contract**

Update the workflow `on:` block to this shape:

```yaml
on:
  workflow_dispatch:
    inputs:
      target:
        description: Schema target to refresh
        required: true
        type: choice
        options:
          - anilist
          - anitrend
          - both
        default: both
  repository_dispatch:
    types: [graphql-schema-sync]
  schedule:
    - cron: '0 6 * * 1'
```

- [ ] **Step 2: Normalize the target and call the script**

Add a target-resolution step and script execution using this pattern:

```yaml
      - name: Resolve schema target
        id: target
        env:
          MANUAL_TARGET: ${{ inputs.target }}
          DISPATCH_TARGET: ${{ github.event.client_payload.target }}
          EVENT_NAME: ${{ github.event_name }}
        run: |
          if [ "$EVENT_NAME" = "workflow_dispatch" ]; then
            TARGET="$MANUAL_TARGET"
          elif [ "$EVENT_NAME" = "repository_dispatch" ]; then
            TARGET="$DISPATCH_TARGET"
          else
            TARGET="both"
          fi

          case "$TARGET" in
            anilist|anitrend|both) ;;
            *)
              printf 'Unsupported target: %s\n' "$TARGET" >&2
              exit 1
              ;;
          esac

          printf 'target=%s\n' "$TARGET" >> "$GITHUB_OUTPUT"

      - name: Run GraphQL schema sync
        run: bash .github/scripts/sync-graphql-schemas.sh "${{ steps.target.outputs.target }}"
```

- [ ] **Step 3: Align runtime, token usage, and PR metadata with repo conventions**

Make the workflow follow this metadata shape:

```yaml
permissions:
  contents: read

jobs:
  sync-schemas:
    permissions:
      contents: write
      pull-requests: write
    runs-on: ubuntu-latest
    steps:
      - uses: actions/create-github-app-token@v3
        id: app-token
        with:
          app-id: ${{ secrets.APP_ID }}
          private-key: ${{ secrets.APP_PRIVATE_KEY }}
      - uses: actions/checkout@v6
        with:
          ref: develop
          token: ${{ steps.app-token.outputs.token }}
      - uses: actions/setup-node@v4
        with:
          node-version: 22
```

Update the PR step to use the agreed conventions:

```yaml
      - name: Create Pull Request
        uses: peter-evans/create-pull-request@v8
        with:
          token: ${{ steps.app-token.outputs.token }}
          signoff: true
          delete-branch: true
          commit-message: "ci(graphql): refresh GraphQL schemas"
          title: "ci(graphql): refresh GraphQL schemas"
          body: |
            This PR was automatically generated to refresh the tracked GraphQL schema files.
          branch: ci/update-graphql-schemas
          base: develop
          labels: ":skateboard: skip-changelog"
          add-paths: |
            data/schema.graphql
            data/anitrend.schema.graphql
```

- [ ] **Step 4: Validate the workflow file shape**

Run: `ruby -e "require 'yaml'; YAML.load_file('.github/workflows/graphql-schema-sync.yml')"`

Expected: exit code `0` and no YAML parse error.

- [ ] **Step 5: Dry-run the trigger logic mentally against each event**

Verify the workflow covers:
- `workflow_dispatch` with `target=anilist`
- `workflow_dispatch` with `target=anitrend`
- `workflow_dispatch` with `target=both`
- `repository_dispatch` with `client_payload.target=anilist`
- `repository_dispatch` with `client_payload.target=anitrend`
- `repository_dispatch` with `client_payload.target=both`
- `schedule` defaulting to `both`

The implementation is correct only if invalid `repository_dispatch` input fails rather than silently choosing a partial default.

- [ ] **Step 6: Commit the workflow slice**

```bash
git add .github/workflows/graphql-schema-sync.yml
git commit -m "ci(graphql): finalize schema sync workflow contract"
```

### Task 4: Regenerate Schemas And Verify The Full Integration

**Files:**
- Modify: `data/schema.graphql`
- Modify: `data/anitrend.schema.graphql`

- [ ] **Step 1: Run the full schema sync after the workflow and script are in place**

Run: `bash .github/scripts/sync-graphql-schemas.sh both`

Expected: both schema files regenerate successfully with no CLI errors.

- [ ] **Step 2: Verify only the intended files changed**

Run: `git status --short`

Expected: only these project files are changed for the implementation:
- `data/graphql.config.yml`
- `.github/scripts/sync-graphql-schemas.sh`
- `.github/workflows/graphql-schema-sync.yml`
- `data/schema.graphql`
- `data/anitrend.schema.graphql`
- the earlier approved docs already written in this session

No `node_modules/` directory and no replacement package manifests should remain under `data/`.

- [ ] **Step 3: Inspect the final diff for naming consistency**

Run: `git diff -- .github/workflows/graphql-schema-sync.yml .github/scripts/sync-graphql-schemas.sh data/graphql.config.yml CONTRIBUTING.md`

Expected:
- workflow uses `ci/update-graphql-schemas`
- commit and PR subject use `ci(graphql): refresh GraphQL schemas`
- script only accepts `anilist`, `anitrend`, `both`
- config uses `Host` and `introspect: false` for AniTrend

- [ ] **Step 4: Commit the generated schema refresh**

```bash
git add data/schema.graphql data/anitrend.schema.graphql
git commit -m "ci(graphql): refresh tracked schema snapshots"
```

### Task 5: Final Verification

**Files:**
- Review: `.github/workflows/graphql-schema-sync.yml`
- Review: `.github/scripts/sync-graphql-schemas.sh`
- Review: `data/graphql.config.yml`
- Review: `CONTRIBUTING.md`

- [ ] **Step 1: Re-run the local script verification matrix**

Run:

```bash
bash .github/scripts/sync-graphql-schemas.sh anilist
bash .github/scripts/sync-graphql-schemas.sh anitrend
bash .github/scripts/sync-graphql-schemas.sh both
```

Expected: all three commands exit `0`.

- [ ] **Step 2: Verify the workflow can be manually triggered with inputs**

Run:

```bash
gh workflow run graphql-schema-sync.yml --ref develop -f target=anilist
gh workflow run graphql-schema-sync.yml --ref develop -f target=anitrend
gh workflow run graphql-schema-sync.yml --ref develop -f target=both
```

Expected: all three dispatch commands are accepted by GitHub with no input validation errors.

- [ ] **Step 3: Verify the repository dispatch payload shape is documented and works**

Run this example against the target repository when credentials allow it:

```bash
gh api \
  --method POST \
  repos/AniTrend/anitrend-v2/dispatches \
  -f event_type='graphql-schema-sync' \
  -F client_payload[target]='both'
```

Expected: HTTP `204 No Content`.

- [ ] **Step 4: Review the final working tree before handoff**

Run: `git status --short && git log --oneline -5`

Expected:
- the intended files are staged or committed as planned
- recent commits reflect the agreed `chore(...)` and `ci(...)` message structure

- [ ] **Step 5: Prepare handoff notes**

Document these outcomes in the final response:
- which files were added, removed, and updated
- the exact workflow trigger contract
- how to manually dispatch the workflow
- how another repository should send `repository_dispatch`
- any verification that could not be completed locally
