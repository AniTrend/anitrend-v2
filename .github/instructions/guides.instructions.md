---
applyTo: **
description: This file provides guidelines for using and extending the architecture in AniTrend v2.
---

# Using and Extending the Architecture

## Reference Routing

This file is the high-level contributor playbook. For deep implementation specifics, jump to:

- `.github/skills/data-state-pattern/SKILL.md`
- `.github/skills/room-entity-pattern/SKILL.md`
- `.github/skills/graphql-query-pattern/SKILL.md`
- `.github/skills/string-resources-convention/SKILL.md`
- `.github/skills/testing-guidelines/SKILL.md`
- `.github/skills/reference-map/SKILL.md`

Keep architectural intent and policy in this instruction file, and keep tactical implementation
detail in skill files to prevent context drift and duplication.

## Architectural patterns — quick reference

- **Domain use cases**: business logic lives in `*UseCase` / `*Interactor` in `:domain`. Return
  `UiState<T>` or `DataState<T>`. Never place substantial logic in a ViewModel or repository.
- **Repository interfaces in domain, implementations in data**: define `IXxxRepository` in
  `:domain`, implement `XxxRepository` in `:data`. Wire via Koin (see
  `.github/skills/koin-module-wiring/SKILL.md`).
- **DataState for all data streams**: repositories return `DataState<T>` — never raw values or
  `LiveData`. See `.github/skills/data-state-pattern/SKILL.md`.
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

The `tag` domain + data package is the canonical reference implementation. For any new entity:

1. Read `domain/src/main/kotlin/co/anitrend/domain/tag/` — use case, repository interface.
2. Read `data/src/main/kotlin/co/anitrend/data/tag/` — entity, DAO, mapper, source, repository,
   use-case impl, Koin module.
3. Apply the same structure to the new entity, adjusting names.

For GraphQL queries, search for existing usages of `@GraphQuery` in the data source files.

---
