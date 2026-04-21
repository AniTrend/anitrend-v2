# AniTrend Agent Playbook

This file is the entry point for contributors and coding agents.

## Why the docs are split

AniTrend now separates docs into two layers:

- `.github/instructions/*.instructions.md` for stable policy, architecture boundaries, and repo-wide rules.
- `.github/skills/*.md` for focused implementation detail, examples, and execution checklists.

This split replaced SHA-pinned GitHub permalink references with stable repo-relative links.
Before merging doc changes, run `.github/scripts/audit-instruction-refs.sh`.

## Start Here Workflow

1. Pick the instruction file by scope.
2. Follow linked skill files for implementation depth.
3. Validate changes with formatting, tests, and docs audit checks.

Reference index: `.github/skills/reference-map/SKILL.md`

## AI-Native SDLC Packs

- `docs/support-arch/instructions/*.md` contains autonomous operational playbooks for CI, migrations,
  API contract auditing, and QA/compliance routines.
- `docs/support-arch/prompts/*.md` contains paired prompt templates that invoke those playbooks.
- When adding a new pack, link it from `.github/skills/reference-map/SKILL.md`.

## Scope Routing

| Task | Read first | Then read |
|---|---|---|
| Architecture and module boundaries | `.github/instructions/context.instructions.md` | `.github/skills/reference-map/SKILL.md` |
| Build logic, Gradle, dependencies | `.github/instructions/build-system.instructions.md` | `.github/skills/new-module-checklist/SKILL.md` |
| DataState / repository flow | `.github/instructions/guides.instructions.md` | `.github/skills/data-state-pattern/SKILL.md` |
| DI setup and module wiring | `.github/instructions/context.instructions.md` | `.github/skills/koin-module-wiring/SKILL.md` |
| Room entities and migrations | `.github/instructions/data.guides.instructions.md` | `.github/skills/room-entity-pattern/SKILL.md` |
| GraphQL query/controller updates | `.github/instructions/guides.instructions.md` | `.github/skills/graphql-query-pattern/SKILL.md` |
| String naming and translator comments | `.github/instructions/guides.instructions.md` | `.github/skills/string-resources-convention/SKILL.md` |
| Test strategy and commands | `.github/instructions/guides.instructions.md` | `.github/skills/testing-guidelines/SKILL.md` |
| External integrations and stack context | `.github/instructions/project-scope.instructions.md` | `.github/skills/key-libraries/SKILL.md` |

## Engineering Defaults

- Keep clean boundaries: `feature/common/task -> interactor aliases -> domain contracts -> data implementations`.
  Never import data repositories, sources, mappers, controllers, or remote models into
  `feature`, `common`, or `task` code.
- Return `DataState<T>` for repository streams, not raw models or `LiveData`.
- Wire new implementations through Koin `Modules.kt` near the owning module.
- Register new modules in `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`.
- Prefer existing platform setup for Compose, paging, WorkManager, Retrofit/GraphQL, and Coil.
- Use `Timber` for logs. Do not use `Log.*` or `println`.

## Build, Test, and Quality Gates

- Format: `./gradlew spotlessApply`
- Static checks: `./gradlew lint spotlessCheck`
- Unit tests: `./gradlew testDebugUnitTest --no-daemon`
- Instrumented tests: `./gradlew connectedDebugAndroidTest --no-daemon`
- Docs audit: `.github/scripts/audit-instruction-refs.sh`

## Documentation Change Rules

- Keep intent and policy in instruction files.
- Keep implementation detail and examples in skill files.
- Add links to `.github/skills/reference-map/SKILL.md` instead of copying long prose across files.
- Use repo-relative paths. Do not add SHA-pinned `blob/<sha>#L..` links.
- When adding a new skill, link it from at least one instruction file and from the reference map.

## Security and Config

- Never commit secrets or tokens.
- Keep local credentials in `.config/*.properties` or `local.properties`.
- If adding reflection-heavy libraries, update `proguard-common.pro`.

## Canonical Code Anchors

- `domain/src/main/kotlin/co/anitrend/domain/tag/`
- `data/src/main/kotlin/co/anitrend/data/tag/`
- `data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`

<!-- code-review-graph MCP tools -->
## MCP Tools: code-review-graph

**IMPORTANT: This project has a knowledge graph. ALWAYS use the
code-review-graph MCP tools BEFORE using Grep/Glob/Read to explore
the codebase.** The graph is faster, cheaper (fewer tokens), and gives
you structural context (callers, dependents, test coverage) that file
scanning cannot.

### When to use graph tools FIRST

- **Exploring code**: `semantic_search_nodes` or `query_graph` instead of Grep
- **Understanding impact**: `get_impact_radius` instead of manually tracing imports
- **Code review**: `detect_changes` + `get_review_context` instead of reading entire files
- **Finding relationships**: `query_graph` with callers_of/callees_of/imports_of/tests_for
- **Architecture questions**: `get_architecture_overview` + `list_communities`

Fall back to Grep/Glob/Read **only** when the graph doesn't cover what you need.

### Key Tools

| Tool | Use when |
|------|----------|
| `detect_changes` | Reviewing code changes — gives risk-scored analysis |
| `get_review_context` | Need source snippets for review — token-efficient |
| `get_impact_radius` | Understanding blast radius of a change |
| `get_affected_flows` | Finding which execution paths are impacted |
| `query_graph` | Tracing callers, callees, imports, tests, dependencies |
| `semantic_search_nodes` | Finding functions/classes by name or keyword |
| `get_architecture_overview` | Understanding high-level codebase structure |
| `refactor_tool` | Planning renames, finding dead code |

### Workflow

1. The graph auto-updates on file changes (via hooks).
2. Use `detect_changes` for code review.
3. Use `get_affected_flows` to understand impact.
4. Use `query_graph` pattern="tests_for" to check coverage.
