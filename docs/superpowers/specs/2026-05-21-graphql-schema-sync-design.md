# GraphQL Schema Sync Design

## Summary

Replace JetBrains live introspection for the AniTrend endpoint with a reliable, repo-owned schema refresh workflow. The workflow refreshes `data/schema.graphql` and `data/anitrend.schema.graphql` on demand, on schedule, or from another repository, then opens or updates a pull request with the generated changes.

## Goals

- Disable live introspection for the AniTrend endpoint in local GraphQL tooling.
- Refresh GraphQL schema files through automation instead of IDE introspection.
- Use one workflow contract for manual triggers and cross-repository dispatches.
- Keep refresh logic in `.github/scripts/` rather than under `data/`.
- Keep pull request automation aligned with existing repository workflow patterns.

## Non-Goals

- Reorganizing GraphQL assets outside their current `data/` locations.
- Supporting arbitrary endpoint selection beyond `anilist`, `anitrend`, or `both`.
- Introducing a reusable workflow unless another repository needs `workflow_call` later.

## Current Constraints

- `data/graphql.config.yml` is the active GraphQL config file.
- The AniTrend edge schema must remain at `data/anitrend.schema.graphql` because existing docs and tooling already point to that path.
- Existing GitHub automation in this repository uses app-token checkout and `peter-evans/create-pull-request`.
- Community guidance already defines branch prefixes in `CONTRIBUTING.md`, so automation should follow the same naming rules.

## Proposed Design

### Configuration

- Set `AniTrend.introspect: false` in `data/graphql.config.yml`.
- Keep the AniTrend request header name as `Host`, not `HOST`.

### Script

Add one script at `.github/scripts/sync-graphql-schemas.sh`.

Script contract:

- Required positional argument: `anilist`, `anitrend`, or `both`.
- Invalid input exits non-zero with a short usage message.
- `anilist` refreshes `data/schema.graphql`.
- `anitrend` refreshes `data/anitrend.schema.graphql`.
- `both` refreshes both files in a deterministic order.

Script responsibilities:

- Install or invoke the GraphQL introspection CLI required to fetch schemas.
- Use the AniTrend headers required by the endpoint:
  - `User-Agent: JS GraphQL`
  - `Accept: */*`
  - `Host: api.anitrend.co`
- Write generated files directly into `data/`.
- Fail fast if schema generation fails.

The script owns refresh behavior. The workflow owns trigger handling, credentials, checkout, and PR creation.

### Workflow

Keep one workflow at `.github/workflows/graphql-schema-sync.yml`.

Supported triggers:

- `workflow_dispatch`
- `repository_dispatch`
- `schedule`

Trigger contract:

- `workflow_dispatch` exposes an input named `target` with allowed values `anilist`, `anitrend`, `both`.
- `repository_dispatch` uses event type `graphql-schema-sync` and reads `github.event.client_payload.target` with the same allowed values.
- `schedule` runs without an external payload and defaults internally to `both`.

Target resolution rules:

- Manual run: use `inputs.target`.
- Cross-repo dispatch: use `client_payload.target`.
- Scheduled run: use `both`.

Workflow responsibilities:

- Start with top-level read-only permissions.
- Grant write permissions only to the job that opens the PR.
- Mint a short-lived GitHub App token.
- Check out `develop`.
- Set up the runtime needed by the schema sync script.
- Run `.github/scripts/sync-graphql-schemas.sh <target>`.
- Open or update a single automation PR with `peter-evans/create-pull-request`.

### PR Automation Conventions

Treat this as CI automation.

- Branch: `ci/update-graphql-schemas`
- Commit message: `ci(graphql): refresh GraphQL schemas`
- PR title: `ci(graphql): refresh GraphQL schemas`

The repository-wide contribution guide should be the canonical source for branch names, commit subjects, PR titles, and issue title structure. That guide should:

- reference `.github/release-drafter-config.yml` as the source of truth for supported branch-prefix automation
- document branch naming using the repo's existing `<type>/<issue>-<short-description>` style where an issue number exists
- document conventional commit and PR title formatting using `<type>(<scope>): <summary>`
- document issue titles using the existing scoped bracket pattern, such as `[area] Summary` or `[area:subarea] Summary`

The workflow should update one long-lived automation branch and one recurring PR rather than opening a new PR each run.

### Changed Paths in the Automation PR

The PR should include only intended generated files and directly related workflow/config/docs changes:

- `data/schema.graphql`
- `data/anitrend.schema.graphql`
- `data/graphql.config.yml` when configuration changes are part of the work
- `.github/workflows/graphql-schema-sync.yml` when the workflow itself changes
- `.github/scripts/sync-graphql-schemas.sh` when the sync implementation changes
- `CONTRIBUTING.md` when community guidance changes

## Error Handling

- Invalid `target` input fails the script before any generation work starts.
- Missing or malformed `repository_dispatch` payload defaults should not silently refresh the wrong scope; the workflow should either validate the payload or explicitly default only for `schedule`.
- If one schema refresh fails during `both`, the script should exit non-zero and allow the workflow to fail without opening a misleading PR.

## Testing And Verification

Before adopting the implementation, verify:

- local script execution for `anilist`
- local script execution for `anitrend`
- local script execution for `both`
- manual workflow dispatch for each `target` option
- repository dispatch using each allowed `target` value
- scheduled run path defaults to `both`
- PR branch, commit subject, and changed paths match the documented conventions

## Future Extension

If another repository later needs to reuse this logic through `workflow_call`, extract the orchestration into a reusable workflow and keep this workflow as the thin trigger wrapper. That is intentionally deferred until there is a concrete caller.
