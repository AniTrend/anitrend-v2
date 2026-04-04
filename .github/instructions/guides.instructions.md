---
applyTo: **
description: This file provides guidelines for using and extending the architecture in AniTrend v2.
---

# Using and Extending the Architecture

## Reference Routing

This file is the high-level contributor playbook. For deep implementation specifics, jump to:

- `.github/skills/data-state-pattern/SKILL.md`
- `.github/skills/layered-module-patterns/SKILL.md`
- `.github/skills/room-entity-pattern/SKILL.md`
- `.github/skills/graphql-query-pattern/SKILL.md`
- `.github/skills/string-resources-convention/SKILL.md`
- `.github/skills/testing-guidelines/SKILL.md`
- `.github/skills/reference-map/SKILL.md`

Keep architectural intent and policy in this instruction file, and keep tactical implementation
detail in skill files to prevent context drift and duplication.

## Architectural patterns — quick reference

- **Domain use cases**: keep params, repository contracts, and abstract `*UseCase` /
  `*Interactor` wrappers in `:domain`. These contracts are usually generic over `UiState<T>`;
  the data layer specializes them to `DataState<T>`. Do not move contract shape into feature or
  data `Types.kt`.
- **Repository interfaces in domain, implementations in data**: define `IXxxRepository` in
  `:domain`, implement `XxxRepository` in `:data`. For hybrid modules, split the contract by
  operation (`Detail`, `Paged`, `Save`, `Delete`, `Rate`, etc.). Wire via Koin (see
  `.github/skills/koin-module-wiring/SKILL.md` and `.github/skills/layered-module-patterns/SKILL.md`).
- **DataState for public data contracts**: data-layer repository specializations return
  `DataState<T>`; feature and task code should never depend on raw repository values or `LiveData`.
  See `.github/skills/data-state-pattern/SKILL.md`.
- **Feature and task modules consume interactors, not repositories**: imports like
  `co.anitrend.data.media.GetDetailMediaInteractor` are acceptable because they alias domain use
  cases. Do not inject repositories, sources, mappers, controllers, or remote models into
  `feature`, `common`, or `task` code.
- **Mutation routing**: if the existing flow is task-backed or should survive process transitions,
  create params through the corresponding `*TaskRouter` and let the worker execute the mutation
  interactor. Current references: `task/medialist`, `task/review`, and `task/favourite`.
- **Room persistence**: follow the four-file entity/DAO/mapper/repository pattern. See
  `.github/skills/room-entity-pattern/SKILL.md`.
- **GraphQL networking**: use `GraphQLController` and the `retrofit-graphql` adapter. See
  `.github/skills/graphql-query-pattern/SKILL.md`.
- **New module**: register in `Modules.kt`, add Koin wiring, follow the full checklist in
  `.github/skills/new-module-checklist/SKILL.md`.

## Coding Conventions and Style

- **Kotlin + Spotless/ktlint**: run `./gradlew spotlessApply` before committing. 4-space indent,
  150-char line width (see `.editorconfig`).
- **Naming**:
  - Use cases: `SomethingUseCase` or `SomethingInteractor` (be consistent within a domain package).
  - Repository interfaces: `I` prefix (`IConfigRepository`); implementations drop the `I`.
  - Composable functions may break casing rules when annotated with `@Composable`.
- **KDoc**: write clear KDoc for all public classes and functions, especially in domain and data.
- **Error handling**: encapsulate network errors as `RequestError` via `GraphQLController`; let
  `DataState` propagate them to the UI error channel.
- **Threading**: `support-arch` base classes already dispatch to `Dispatchers.IO`. Avoid extra
  `withContext` unless bypassing the provided base.
- **Logging**: `Timber.d/e/w` only — never `Log.*` or `println`.
- **Imports**: no wildcard imports except for `R` classes and nested static imports.
- **Analytics**: gate Firebase Analytics calls behind a flavor check; use the analytics helper if
  available in `support-arch:analytics`.

## String Resource Naming Conventions

String resources follow a strict semantic prefix pattern. For the complete convention, examples,
migration guide, and POEditor translator comment requirements, see
`.github/skills/string-resources-convention/SKILL.md`.

**Pattern**: `{prefix}_{module_or_context}_{specific_identifier}`

Standard prefixes: `label_`, `title_`, `subtitle_`, `placeholder_`, `action_`, `message_`,
`error_`, `hint_`, `description_`.

## Testing Guidelines

For the full testing guide (unit vs instrumented, Turbine usage, WorkManager, and run commands),
see `.github/skills/testing-guidelines/SKILL.md`.

Summary:
- Unit tests: JUnit 4 + MockK + Turbine + coroutines-test; mirror production package names.
- UI tests: Espresso in `androidTest/`; document device requirements in PR.
- Run `./gradlew testDebugUnitTest --no-daemon` before submitting.
- Do not remove or modify unrelated existing tests to make a PR pass.

## Workflow and Contribution Tips

- **Gradle sync**: many modules means slow sync. Target a specific module with
  `:feature:xyz:assembleDebug` when iterating.
- **buildSrc changes**: reload Gradle after any change; mistakes affect the entire build.
- **Adding a dependency**: update `gradle/libs.versions.toml` first; if widely used, add to the
  appropriate `apply*` function in `ProjectDependencies.kt`; otherwise add only in the module's
  `build.gradle.kts`.
- **Updating support-arch / AniTrend libs**: versions in `gradle/libs.versions.toml`; run tests
  after bumping as API may change.
- **ProGuard**: update `proguard-common.pro` when adding reflection-heavy libraries.

## Getting Help from the Code

Pick the closest reference module instead of defaulting to `tag` for every task:

1. `domain/tag` + `data/tag` for the smallest query-only baseline.
2. `domain/media` + `data/media` for multi-contract read modules (`Detail`, `Paged`, `Network`).
3. `domain/medialist` + `data/medialist` + `task/medialist` for hybrid fetch plus save/delete/sync
   flows.
4. `domain/review` + `data/review` + `task/review` for paged/detail fetch plus vote/save/delete.
5. `domain/favourite` + `data/favourite` + `task/favourite` for mutation-only toggle flow.

If the module shape is unclear, read `.github/skills/layered-module-patterns/SKILL.md` first,
then inspect the closest code reference.

For GraphQL queries, search for existing usages of `@GraphQuery` in the data source files.

---
