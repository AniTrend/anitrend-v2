---
name: qa-license-autofix
category: qa-compliance
trigger-intent: Use when style violations, static-analysis issues, or dependency-license findings must be fixed autonomously while preserving GPL-3.0 project requirements and Apache-2.0 compatibility where applicable.
---

# QA and License Autofix

## Purpose

Run local quality gates, integrate with Codacy and FOSSA when available, and auto-fix issues that
do not require product decisions. Preserve GPL-3.0 obligations and refuse incompatible dependency
changes.

## Trigger Intent

- Spotless, lint, or unit-test failures need autonomous remediation.
- Codacy or local static-analysis findings need batch cleanup.
- FOSSA or dependency-license review needs a local verification pass.
- A dependency addition or upgrade may create GPL or Apache compatibility risk.

## Repo Anchors

- `LICENSE.md`
- `gradle/libs.versions.toml`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
- `.github/PULL_REQUEST_TEMPLATE.md`

## Execution Steps

### 1. Preflight the available QA tooling

```bash
find . -maxdepth 2 \( -name '.codacy.yml' -o -name '.codacy.yaml' -o -name '.fossa.yml' -o -name '.fossa.yaml' -o -name 'LICENSE*' \) -print
command -v codacy-analysis-cli || true
command -v fossa || true
```

### 2. Run the repo-native quality gates first

```bash
./gradlew spotlessApply
./gradlew lint spotlessCheck testDebugUnitTest --no-daemon --stacktrace
```

### 3. Audit license-sensitive files and headers

```bash
rg -n 'GNU General Public License|Apache License|SPDX-License-Identifier|Copyright \\(C\\)' \
  AGENTS.md .github app buildSrc common data domain feature task
rg -n 'implementation\\(|api\\(|kapt\\(' --glob '**/build.gradle.kts' .
```

### 4. Run Codacy local analysis if the CLI is available

For Codacy Analysis CLI:

```bash
codacy-analysis-cli analyze \
  --provider gh \
  --username <org> \
  --project <repo> \
  --tool ktlint \
  --allow-network \
  --upload \
  --verbose
```

If Codacy CLI access is unavailable, use the repo-native results from Spotless, lint, and tests as
the local remediation source and report the missing external CLI explicitly.

### 5. Run FOSSA if available

```bash
fossa analyze
fossa test
fossa test --diff <base-revision>
```

Use `fossa test --diff <base-revision>` to focus on net-new issues introduced by the current
branch.

## Autonomous Fix Rules

- Auto-fix formatting, import order, dead-simple lint issues, missing headers, and dependency
  declarations that clearly violate repo conventions.
- Preserve existing GPL headers in source files.
- Treat dependency-license changes as blocked until compatibility is clear. Do not silently swap in
  a dependency with an incompatible license.
- If Codacy or FOSSA flags a dependency, trace it through `libs.versions.toml`, `ProjectDependencies.kt`,
  and the consuming module before editing anything.

## Hard Stop Conditions

Stop and escalate instead of auto-fixing when:

- a new dependency appears to conflict with GPL-3.0 obligations
- a license exception or policy waiver is required
- the QA tool report cannot be reproduced locally
- a style issue is actually masking a behavioral regression

## Deliverable

Return:

1. Which local gates were run.
2. Which external tools were available.
3. Every issue auto-fixed.
4. Any remaining license or policy blocker.
5. The exact command to rerun the clean verification pass.
