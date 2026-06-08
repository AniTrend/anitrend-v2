---
name: ci-pipeline-intervention
category: ci-cd
trigger-intent: Use when GitHub Actions jobs fail, hang, or block downstream diagnostics and the agent must inspect logs, reproduce locally, quarantine non-critical steps, or rerun failed jobs without greenwashing real failures.
---

# CI Pipeline Intervention

## Purpose

Stabilize AniTrend GitHub Actions runs while preserving failure signal. Prefer deferred failure,
artifact capture, and targeted reruns over blanket `|| true`.

## Trigger Intent

- `ci.yml` or another workflow failed on a branch or PR.
- A step must exit `0` so later jobs can still publish reports, summaries, or cleanup artifacts.
- The workflow needs a repair that preserves diagnostics while preventing a transient blocker from
  collapsing the entire run.

## Repo Anchors

- `.github/workflows/ci.yml`
- `.github/workflows/gradle-cache-cleanup.yml`
- `.github/actions/android/action.yml`
- `.github/scripts/staging-config.sh`

## Execution Steps

### 1. Inspect the failing workflow

```bash
ls -1 .github/workflows
gh run list --workflow ci.yml --limit 10
gh run view <run-id> --json jobs --jq '.jobs[] | {name, databaseId, conclusion}'
gh run view <run-id> --log-failed
gh run download <run-id> -D /tmp/anitrend-gh-artifacts
```

For a single job:

```bash
gh run view --job <job-database-id> --log
```

### 2. Reproduce locally with the narrowest task

```bash
./gradlew spotlessApply
./gradlew lint spotlessCheck --no-daemon --stacktrace
bash .github/scripts/staging-config.sh
./gradlew test --no-daemon --stacktrace
```

If the failure is module-specific, prefer a narrower task such as:

```bash
./gradlew :data:compileDebugKotlin --no-daemon --stacktrace
./gradlew :feature:media:assembleDebug --no-daemon --stacktrace
```

### 3. Choose the repair mode

- **Code fix**: compilation, test, or behavior is actually broken. Patch code first.
- **Workflow quarantine**: the step is flaky, diagnostic-only, or cleanup-only. Keep the run alive
  while preserving exit code, summary, and artifacts.
- **Cache cleanup / rerun**: local repro is clean but CI is polluted. Use the cache-cleanup
  workflow, then rerun failed jobs.

### 4. Use the safe exit-0 pattern

Use this when a step must not stop downstream diagnostics:

```yaml
- name: Capture Gradle outcome without blocking later jobs
  id: gradle-test
  shell: bash
  run: |
    set +e
    ./gradlew test --no-daemon --stacktrace
    status=$?
    echo "exit_code=$status" >> "$GITHUB_OUTPUT"
    exit 0

- name: Publish diagnostics
  if: always()
  uses: actions/upload-artifact@v4
  with:
    name: gradle-diagnostics
    path: |
      **/build/reports/**
      **/build/test-results/**

- name: Summarize deferred failure
  if: always()
  shell: bash
  run: |
    echo "gradle_exit_code=${{ steps.gradle-test.outputs.exit_code }}" >> "$GITHUB_STEP_SUMMARY"
```

If the pipeline must still fail eventually, move the decision into a dedicated gate job:

```yaml
jobs:
  unit-test:
    outputs:
      gradle_exit_code: ${{ steps.gradle-test.outputs.exit_code }}

  qa-gate:
    needs: [unit-test]
    if: always()
    runs-on: ubuntu-24.04
    steps:
      - name: Fail only after downstream diagnostics ran
        shell: bash
        run: test "${{ needs.unit-test.outputs.gradle_exit_code }}" = "0"
```

### 5. Rerun the exact failed scope

```bash
gh run rerun <run-id> --failed
gh run view <run-id> --json jobs --jq '.jobs[] | {name, databaseId}'
gh run rerun <run-id> --job <job-database-id>
gh run watch <run-id> --compact --exit-status
```

### 6. Clear stale Gradle caches only when CI-specific corruption is likely

```bash
gh workflow run gradle-cache-cleanup.yml -f branch=<branch> -f allCaches=false
```

## Guardrails

- Never mask a real compile or functional test failure without surfacing the captured exit code.
- Never replace a broken test step with unconditional success if branch protection still depends on
  that signal.
- Restrict `continue-on-error: true` to diff checks, uploads, or non-critical cleanup.
- Preserve artifact and summary publishing with `if: always()`.
- Prefer rerunning failed jobs over rerunning the entire workflow when the failing scope is known.

## Deliverable

Return:

1. The failing job and step.
2. The exact local repro command.
3. The patch or YAML intervention chosen.
4. The rerun command.
5. Any remaining blocker that still requires a real code fix.
