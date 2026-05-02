---
applyTo: **
description: This file provides guidelines for using and extending the architecture in AniTrend v2.
---

# Using and Extending the Architecture

## Reference Routing

This file is the high-level contributor playbook. For deep implementation specifics, jump to:

- `.agents/skills/data-state-pattern/SKILL.md`
- `.agents/skills/layered-module-patterns/SKILL.md`
- `.agents/skills/room-entity-pattern/SKILL.md`
- `.agents/skills/graphql-query-pattern/SKILL.md`
- `.agents/skills/data-android-infrastructure/SKILL.md`
- `.agents/skills/product-designer/SKILL.md`
- `.agents/skills/string-resources-convention/SKILL.md`
- `.agents/skills/string-resource-inline-comments/SKILL.md`
- `.agents/skills/testing-guidelines/SKILL.md`
- `.agents/skills/reference-map/SKILL.md`

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
  `.agents/skills/koin-module-wiring/SKILL.md` and `.agents/skills/layered-module-patterns/SKILL.md`).
- **DataState for public data contracts**: data-layer repository specializations return
  `DataState<T>`; feature and task code should never depend on raw repository values or `LiveData`.
  See `.agents/skills/data-state-pattern/SKILL.md`.
- **Non-paged offline-first reads**: for single entities or fixed-size collections, use
  `observable(): Flow<T>` or `observable(): Flow<List<T>>` with Room as the source of truth.
  Cache policy should gate refresh at the source boundary while the observable flow continues to
  emit local state. See `.agents/skills/data-state-pattern/SKILL.md`.
- **Offline-first paged reads**: for DB-backed paged query flows, treat Room as the source of
  truth. The source contract should expose `observable(): Flow<PagedList<T>>` from a local
  `DataSource.Factory`, and network refreshes should persist through the controller/mapper chain.
  Do not implement local entity mapping or cache-merging logic directly inside the source class,
  and do not fall back to `SupportPagingLiveDataSource` for a flow that already has a local store.
- **Feature and task modules consume interactors, not repositories**: imports like
  `co.anitrend.data.media.GetDetailMediaInteractor` are acceptable because they alias domain use
  cases. Do not inject repositories, sources, mappers, controllers, or remote models into
  `feature`, `common`, or `task` code.
- **Mutation routing**: if the existing flow is task-backed or should survive process transitions,
  create params through the corresponding `*TaskRouter` and let the worker execute the mutation
  interactor. Current references: `task/medialist`, `task/review`, and `task/favourite`.
- **Room persistence**: follow the four-file entity/DAO/mapper/repository pattern. See
  `.agents/skills/room-entity-pattern/SKILL.md`.
- **Relationship collections**: when a screen needs an offline-first related collection such as
  media characters or staff, persist the collection in dedicated connection tables keyed to the
  parent entity and order the rows explicitly for paging. Convert local connection entities back
  to domain models with a converter, and keep request-specific persistence decisions inside the
  mapper rather than the source.
- **Fixed-size detail reads**: non-paged detail children and aggregate reads should follow the
  same Room-first rule as larger collections. Back them with dedicated tables keyed to the parent,
  keep refresh orchestration inside the controller + mapper chain, and use source-level
  `clearDataSource()` to invalidate cache identity and local rows together.
- **Query-shape cache variants**: if two callers intentionally request different result sizes from
  the same parent resource, keep one local table but use distinct cache identities so each shape
  can refresh independently without fragmenting persistence.
- **Context-specific source variants**: when one entity type has multiple distinct read contexts,
  define separate source variants for those contexts instead of overloading one broad contract.
  `UserSource.Identifier`, `Viewer`, `Profile`, and `Statistic` are the clearest reference.
- **GraphQL networking**: use `GraphQLController` and the `retrofit-graphql` adapter. See
  `.agents/skills/graphql-query-pattern/SKILL.md`.
  - Keep query/mutation payloads composed from reusable fragments under
    `data/src/main/assets/graphql/fragments/**` instead of inlining duplicated field sets.
  - Keep remote models aligned to fragment composition (smaller, shareable models) to support
    composition-first reuse and controlled inheritance where appropriate.
  - If a task requires deviating from this fragment-first convention (for example inlining fields
    or redefining model boundaries), stop and open a discussion before implementing the change.
- **New module**: register in `Modules.kt`, add Koin wiring, follow the full checklist in
  `.agents/skills/new-module-checklist/SKILL.md`.

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
- **Android runtime debugging**: on-device investigation should start by identifying the exact
  installed package and using pid-scoped `adb logcat --pid` when the process is alive. Prefer
  recorded debug traffic evidence such as Chucker before changing serializers, mappers, or UI
  assumptions. See `.agents/skills/android-runtime-investigation/SKILL.md`.
- **Imports**: no wildcard imports except for `R` classes and nested static imports.
- **Analytics**: gate Firebase Analytics calls behind a flavor check; use the analytics helper if
  available in `support-arch:analytics`.

## String Resource Naming Conventions

String resources follow a strict semantic prefix pattern. For the complete convention, examples,
migration guide, and POEditor translator comment requirements, see
`.agents/skills/string-resources-convention/SKILL.md` and
`.agents/skills/string-resource-inline-comments/SKILL.md`.

For Android platform-level behavior such as escaping, formatting, plurals, arrays, and styled
text handling, also consult
`.agents/skills/string-resources-convention/references/android-string-resource-best-practices.md`.

Every resource block in `strings.xml` must have an XML comment immediately above it. Those
comments are surfaced to POEditor translators and should explain context, placeholders,
tone, and any UI constraints.

**Pattern**: `{prefix}_{module_or_context}_{specific_identifier}`

Standard prefixes: `label_`, `title_`, `subtitle_`, `placeholder_`, `action_`, `message_`,
`error_`, `hint_`, `description_`.

## Testing Guidelines

For the full testing guide (unit vs instrumented, Turbine usage, WorkManager, and run commands),
see `.agents/skills/testing-guidelines/SKILL.md`.

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

1. `domain/tag` + `data/tag` for the smallest non-paged query-only baseline.
2. `domain/media` + `data/media` for multi-contract read modules (`Detail`, `Paged`, `Network`).
3. `domain/medialist` + `data/medialist` + `task/medialist` for hybrid fetch plus save/delete/sync
   flows.
4. `domain/review` + `data/review` + `task/review` for paged/detail fetch plus vote/save/delete.
5. `domain/favourite` + `data/favourite` + `task/favourite` for mutation-only toggle flow.

If the module shape is unclear, read `.agents/skills/layered-module-patterns/SKILL.md` first,
then inspect the closest code reference.

For GraphQL queries, search for existing usages of `@GraphQuery` in the data source files.

---
