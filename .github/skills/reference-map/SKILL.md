---
name: reference-map
description: 'Instruction-to-skill navigation map. Use when routing tasks to the right instruction and skill files or migrating legacy guidance to the split-doc model.'
---

# Skill: Instruction-to-Skill Reference Map

## Why this file exists

This repository moved from monolithic instruction files to a split model:

- **Instructions** provide stable, high-level policy and architecture constraints.
- **Skills** provide focused, task-oriented deep dives.

The goal is to reduce documentation drift and avoid brittle SHA-pinned GitHub links.
Use repo-relative paths only, then run `.github/scripts/audit-instruction-refs.sh`.

## How to use the split

1. Start from the instruction file that matches your scope (`.github/instructions/*.instructions.md`).
2. Jump to the linked skill file for implementation details.
3. Validate references and formatting before opening a PR.

## Legacy topic to new location map

| Legacy topic | Primary instruction | Primary skill |
|---|---|---|
| Architecture boundaries | `.github/instructions/context.instructions.md` | `.github/skills/data-state-pattern/SKILL.md` |
| Layered module plumbing | `.github/instructions/context.instructions.md` | `.github/skills/layered-module-patterns/SKILL.md` |
| UI navigation architecture | `.github/instructions/context.instructions.md` | `.github/skills/navigation-architecture/SKILL.md` |
| Build system conventions | `.github/instructions/build-system.instructions.md` | `.github/skills/new-module-checklist/SKILL.md` |
| DataState / UiState behavior | `.github/instructions/guides.instructions.md` | `.github/skills/data-state-pattern/SKILL.md` |
| Koin DI wiring | `.github/instructions/context.instructions.md` | `.github/skills/koin-module-wiring/SKILL.md` |
| Room entity + migration pattern | `.github/instructions/guides.instructions.md` | `.github/skills/room-entity-pattern/SKILL.md` |
| GraphQL controller lifecycle | `.github/instructions/guides.instructions.md` | `.github/skills/graphql-query-pattern/SKILL.md` |
| String naming and POEditor context | `.github/instructions/guides.instructions.md` | `.github/skills/string-resources-convention/SKILL.md` |
| Test strategy and commands | `.github/instructions/guides.instructions.md` | `.github/skills/testing-guidelines/SKILL.md` |
| Library stack and integrations | `.github/instructions/project-scope.instructions.md` | `.github/skills/key-libraries/SKILL.md` |

## Task-first routing

| If your task is... | Read this first | Then read |
|---|---|---|
| Add a new feature module | `.github/instructions/context.instructions.md` | `.github/skills/new-module-checklist/SKILL.md` |
| Evaluate or add domain/data/feature/task plumbing | `.github/instructions/context.instructions.md` | `.github/skills/layered-module-patterns/SKILL.md` |
| Trace or add screen navigation | `.github/instructions/context.instructions.md` | `.github/skills/navigation-architecture/SKILL.md` |
| Add a new repository or source | `.github/instructions/guides.instructions.md` | `.github/skills/data-state-pattern/SKILL.md` |
| Add DI bindings | `.github/instructions/context.instructions.md` | `.github/skills/koin-module-wiring/SKILL.md` |
| Add or update Room entities | `.github/instructions/data.guides.instructions.md` | `.github/skills/room-entity-pattern/SKILL.md` |
| Add or change GraphQL query flow | `.github/instructions/guides.instructions.md` | `.github/skills/graphql-query-pattern/SKILL.md` |
| Update user-facing strings | `.github/instructions/context.instructions.md` | `.github/skills/string-resources-convention/SKILL.md` |
| Add tests for data/domain logic | `.github/instructions/guides.instructions.md` | `.github/skills/testing-guidelines/SKILL.md` |
| Update dependencies or build logic | `.github/instructions/build-system.instructions.md` | `.github/skills/key-libraries/SKILL.md` |

## AI-Native SDLC Packs

These support-arch prompt packs are repo-local execution playbooks for autonomous operational work.
Use them alongside the `.github` instructions and skills when the task is broader than one module.

- CI/CD pipeline intervention:
  `docs/support-arch/instructions/ci-pipeline-intervention.md` and
  `docs/support-arch/prompts/ci-pipeline-intervention.md`
- Room migration fail-safes:
  `docs/support-arch/instructions/room-migration-failsafe.md` and
  `docs/support-arch/prompts/room-migration-failsafe.md`
- GraphQL contract auditing:
  `docs/support-arch/instructions/graphql-schema-contract-audit.md` and
  `docs/support-arch/prompts/graphql-schema-contract-audit.md`
- QA and license autofix:
  `docs/support-arch/instructions/qa-license-autofix.md` and
  `docs/support-arch/prompts/qa-license-autofix.md`

## Canonical code anchors

Use these as concrete implementation references:

- `domain/src/main/kotlin/co/anitrend/domain/tag/`
- `data/src/main/kotlin/co/anitrend/data/tag/`
- `domain/src/main/kotlin/co/anitrend/domain/media/`
- `data/src/main/kotlin/co/anitrend/data/media/`
- `domain/src/main/kotlin/co/anitrend/domain/medialist/`
- `data/src/main/kotlin/co/anitrend/data/medialist/`
- `domain/src/main/kotlin/co/anitrend/domain/review/`
- `data/src/main/kotlin/co/anitrend/data/review/`
- `domain/src/main/kotlin/co/anitrend/domain/favourite/`
- `data/src/main/kotlin/co/anitrend/data/favourite/`
- `task/medialist/src/main/kotlin/co/anitrend/task/medialist/`
- `task/review/src/main/kotlin/co/anitrend/task/review/`
- `task/favourite/src/main/kotlin/co/anitrend/task/favourite/`
- `data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
- `data/schemas/`

## Maintenance checklist for docs changes

- Keep architecture intent in instruction files.
- Keep implementation detail in skill files.
- Prefer adding links to this map instead of duplicating long prose.
- Never add SHA-pinned `github.com/.../blob/<sha>/...#L..` links.
- Run `.github/scripts/audit-instruction-refs.sh` before merging.
