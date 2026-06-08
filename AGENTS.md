# AniTrend Agent Playbook

This file is the repository entry point for contributors and coding agents.

## Repository Context Structure

AniTrend uses three repo-local context layers:

- `AGENTS.md` for stable repository policy, architecture boundaries, build conventions, and task routing.
- `.agents/skills/*/SKILL.md` for focused implementation guidance, examples, and execution checklists.
- `docs/support-arch/instructions/*.md` and `docs/support-arch/prompts/*.md` for AI-native SDLC packs.

Some optional skills may be installed globally under `~/.agents/skills`. Repository docs must
label those as global or optional instead of linking to them as guaranteed repo-local files.

Use repo-relative paths only. Do not add SHA-pinned GitHub blob links.

## Start Here Workflow

1. Read `AGENTS.md` for repository policy and routing.
2. Open `.agents/skills/reference-map/SKILL.md` for task-first skill routing.
3. Load the skill files that match the current task.
4. Validate changes with formatting, tests, and targeted link or path checks when docs are edited.

Reference index: `.agents/skills/reference-map/SKILL.md`

## Instruction Priority and Tool Routing

Follow instruction precedence in this order:

1. System and developer constraints.
2. Repository policy and skills (`AGENTS.md`, `.agents/skills/*`).
3. Task-specific plans and support-arch docs.

## Scope Routing

| Task | Read first | Then read |
|---|---|---|
| Architecture and module boundaries | `AGENTS.md` | `.agents/skills/reference-map/SKILL.md` |
| Choose the closest existing module or layer example | `AGENTS.md` | `.agents/skills/layered-module-patterns/SKILL.md` |
| Navigate concrete cross-layer examples | `AGENTS.md` | `.agents/skills/reference-map/references/layer-example-matrix.md` |
| Build logic, Gradle, dependencies | `AGENTS.md` | `.agents/skills/new-module-checklist/SKILL.md` |
| DataState / repository flow | `AGENTS.md` | `.agents/skills/data-state-pattern/SKILL.md` |
| DI setup and module wiring | `AGENTS.md` | `.agents/skills/koin-module-wiring/SKILL.md` |
| Android platform/helper reuse | `AGENTS.md` | `.agents/skills/android-platform-patterns/SKILL.md` |
| Navigation, deep links, or router/provider flows | `AGENTS.md` | `.agents/skills/navigation-architecture/SKILL.md` |
| Room entities and migrations | `AGENTS.md` | `.agents/skills/room-entity-pattern/SKILL.md` |
| Shared `:data:android` infrastructure or controller strategy | `AGENTS.md` | `.agents/skills/data-android-infrastructure/SKILL.md` |
| GraphQL query/controller updates | `AGENTS.md` | `.agents/skills/graphql-query-pattern/SKILL.md` |
| GraphQL fragments, model variants, or mapping decisions | `AGENTS.md` | `.agents/skills/mapping-graphql-models/SKILL.md` |
| Silent empty UI or `CacheRequest` collisions | `AGENTS.md` | `.agents/skills/cache-request-isolation/SKILL.md` |
| String naming and translator comments | `AGENTS.md` | `.agents/skills/string-resources-convention/SKILL.md` |
| Missing XML translator comments in `strings.xml` | `AGENTS.md` | `.agents/skills/string-resource-inline-comments/SKILL.md` |
| Test strategy and commands | `AGENTS.md` | `.agents/skills/testing-guidelines/SKILL.md` |
| MockK-specific test patterns or repo test examples | `AGENTS.md` | `.agents/skills/mockk-testing-patterns/SKILL.md` |
| Runtime Android investigation on device or emulator | `AGENTS.md` | `.agents/skills/android-runtime-investigation/SKILL.md` |
| Quick UI evidence capture with dumps and screenshots | `AGENTS.md` | `.agents/skills/android-ui-automator-preview/SKILL.md` |
| ADB install, package, or device connectivity troubleshooting | `AGENTS.md` | `.agents/skills/adb-device-workflow/SKILL.md` |
| UI planning, hierarchy, or product-facing screen refinements | `AGENTS.md` | `.agents/skills/anitrend-product-designer/SKILL.md` |
| External integrations and stack context | `AGENTS.md` | `.agents/skills/key-libraries/SKILL.md` |

## High-Value Skill Anchors

These skill files are the primary implementation and navigation references. Prefer them before
inventing a new pattern or reading broad areas of the codebase:

- `.agents/skills/reference-map/SKILL.md` for task-first routing across the repo.
- `.agents/skills/reference-map/references/layer-example-matrix.md` for concrete cross-layer code anchors.
- `.agents/skills/layered-module-patterns/SKILL.md` for choosing the right module shape before implementation.
- `.agents/skills/data-state-pattern/SKILL.md` for repository return contracts and offline-first flow shape.
- `.agents/skills/koin-module-wiring/SKILL.md` for DI bindings, aggregators, and module loaders.
- `.agents/skills/graphql-query-pattern/SKILL.md` for request lifecycle and controller wiring.
- `.agents/skills/mapping-graphql-models/SKILL.md` and
  `.agents/skills/mapping-graphql-models/references/graphql-model-mapping-matrix.md` for fragment-to-model decisions.
- `.agents/skills/navigation-architecture/SKILL.md` for deep link, router, and screen flow tracing.
- `.agents/skills/android-platform-patterns/SKILL.md` for deciding whether work belongs in `:android:*`.
- `.agents/skills/room-entity-pattern/SKILL.md` for entity, DAO, mapper, and repository structure.
- `.agents/skills/testing-guidelines/SKILL.md`,
  `.agents/skills/testing-guidelines/references/koin-testing.md`, and
  `.agents/skills/mockk-testing-patterns/SKILL.md` for test shape, DI graph checks, and MockK examples.
- `.agents/skills/android-runtime-investigation/SKILL.md` and
  `.agents/skills/android-runtime-investigation/references/chucker-sqlite-queries.md` for runtime evidence-first debugging.
- `.agents/skills/anitrend-product-designer/SKILL.md` and its `references/` folder for UI planning output and reviewable design handoff.

## Project Purpose and Scope

AniTrend v2 is an Android client for AniList. The app supports media discovery and tracking,
lists, profile and social features, news and forum content, recommendations, trending and airing
views, and AniList account management.

### External Data Sources

| Source | Module | Purpose |
|---|---|---|
| AniList GraphQL API | `:data` | Primary media, list, social, and profile source |
| MyAnimeList via Jikan | `:data:jikan` | Supplementary MAL data |
| Imgur | `:data:imgur` | Image uploads |
| AniTrend Edge Functions | `:data:edge` | Aggregation, recommendations, and news-related endpoints |
| TMDB | `:data:tmdb` | Additional media metadata and images |
| Trakt | `:data:trakt` | Watch-history sync |
| Firebase | `google` flavor only | Analytics and Crashlytics |

### Domain Model Context

| Concept | Module hint |
|---|---|
| Media | `:feature:media:*`, `:common:media`, `:data:media` |
| Characters, Staff, Studio | `:feature:character`, `:feature:staff`, `:common:character` |
| Airing schedule | `:feature:airing` |
| News / Forum | `:feature:news`, `:feature:forum` |
| Notifications | `:feature:notification` |
| Profile / Social | `:feature:profile` |
| Media lists | `:feature:media-list` |
| Search / Discover | `:feature:search` |
| Settings | `:feature:settings`, `:data:settings` |
| Updates | `:feature:updater` |

Navigation rule: data logic lives in `:data:<entity>`, UI in `:feature:<name>`, shared UI
components in `:common:<name>`.

## Architecture and Module Boundaries

AniTrend v2 follows a layered clean architecture with domain and data layers consumed through
feature, common, and task entry points.

### Layer Roles

- **Domain layer**: pure Kotlin params, repository contracts, and abstract use cases. No Android
  framework code.
- **Data layer**: repository implementations, GraphQL, Room, paging, cache policy, and alias-based
  interactors exported through `Types.kt`.
- **Android platform layer**: `:android:*` modules for theme, configuration, notification,
  context/fragment helpers, drawer shell wiring, and deep-link entry.
- **Entry layers**: `:feature:*` UI modules, `:common:*` shared presentation layers, and `:task:*`
  WorkManager modules.

### Module Organization

Module paths are registered in
`buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`.

| Prefix | Purpose |
|---|---|
| `:app:` | App entry point, app core init, navigation |
| `:domain` | Use cases, repository interfaces, domain models |
| `:data:*` | Repository implementations, sources, Room entities, integrations |
| `:android:*` | Shared Android-specific helpers and platform surfaces |
| `:common:*` | Shared UI logic and components |
| `:feature:*` | Screen and flow modules |
| `:task:*` | WorkManager background jobs |

### Communication Between Layers

```text
ViewModel / Presenter / Worker
        -> XxxInteractor alias
        -> Domain use case
        -> Domain repository contract
             ^
        Data repository -> Source / controller / cache -> API / DB
```

### Hard Boundary Rules

- Feature, common, and task modules consume interactors only.
- Never import data repositories, sources, mappers, controllers, or remote models into `feature`,
  `common`, or `task` code.
- Return `DataState<T>` for repository streams, not raw models or `LiveData`.
- Wire implementations through Koin `Modules.kt` near the owning module.
- Register new modules in `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`.
- Before adding a new Android helper, inspect `:android:*` and `:app:core` first.
- For multi-operation domains, prefer split contracts such as `Detail`, `Paged`, `Save`, `Delete`,
  `Rate`, or `Viewer` instead of one broad interface.

### Reference Shapes

Use the closest existing module shape before inventing a new pattern:

- `tag` for the smallest query-only baseline.
- `media` for read-heavy multi-contract flows.
- `medialist` and `review` for hybrid query plus mutation flows.
- `favourite` plus `task/favourite` for mutation-only task-backed flow.

### Edge Modeling Guidance

For `:data:edge`, keep remote models schema-faithful and adapt data in the existing layers:

- converters translate remote models into local entities
- mappers coordinate parsing, persistence, and normalization
- entities and entity views represent persisted local shape
- higher-level converters assemble the final app-facing graph

Never embed compatibility hacks or inferred IDs directly in serialized models.

## Contribution Conventions

### Data and Repository Patterns

- Define repository interfaces in `:domain` and implementations in `:data`.
- Keep domain contracts generic over `UiState<T>`; the data layer specializes them to `DataState<T>`.
- For non-paged offline-first reads, use Room as the source of truth via `observable(): Flow<T>` or
  `Flow<List<T>>`.
- For DB-backed paged reads, keep Room as the source of truth and refresh through the
  controller/mapper chain.
- Do not implement local entity mapping or cache merging directly inside a source class when the
  pattern already exists in the controller and mapper layers.
- If writes should be background-safe or survive process transitions, route them through the
  corresponding `:task:*` worker and router.
- When one entity has multiple distinct read contexts, define separate source variants instead of
  overloading one broad source contract.

### GraphQL Rules

- Use `GraphQLController` and the `retrofit-graphql` adapter.
- Keep query and mutation payloads composed from reusable fragments under
  `data/src/main/assets/graphql/fragments/**`.
- Keep remote models aligned with fragment composition for reuse and controlled inheritance.
- If a task requires deviating from fragment-first composition, stop and discuss before
  implementing it.

### Room and Cache Rules

- Follow the four-file entity/DAO/mapper/repository pattern for Room persistence.
- For join or connection tables with auto-generated surrogate keys, use nullable auto-generated
  IDs and composite unique indices for the logical relationship.
- Persist related side-effect rows through dedicated embed mappers instead of injecting extra Room
  sources directly into a parent mapper.
- `cache_log` identity is `request + cache_item_id` only. Every independently fetchable resource
  variant must have its own `CacheRequest` enum value.
- Sidecar source variants must not reuse the parent detail source request identity.

### String Resources

- Use semantic prefixes such as `label_`, `title_`, `subtitle_`, `placeholder_`, `action_`,
  `message_`, `error_`, `hint_`, and `description_`.
- Every resource block in `strings.xml` must have an XML comment immediately above it for POEditor
  translator context.

### UI and Compose

- AniTrend UI is hybrid: Compose + Material3 on newer surfaces, with fragment and controller
  bridges still present in existing flows.
- Navigation is hybrid: feature-local flows, fragment hosts, deep links, and shared router
  contracts coexist.
- Composables should be small and focused and include previews where practical.
- Use `MaterialTheme` tokens. Never hard-code colors or typography.
- Prefer existing platform setup for Compose, paging, WorkManager, Retrofit/GraphQL, and Coil.

### Code Style and Logging

- Run `./gradlew spotlessApply` before committing.
- Use 4-space indent and follow `.editorconfig`.
- Write clear KDoc for public domain and data APIs.
- Use `Timber` for logs. Do not use `Log.*` or `println`.
- Avoid wildcard imports except for `R` classes and required nested static imports.

## Build System and Dependencies

All dependency versions are centrally managed in `gradle/libs.versions.toml`. Use generated
`libs.*` accessors instead of hardcoding artifact coordinates.

When adding or upgrading a library:

1. Add or update the version under `[versions]`.
2. Add the coordinate under `[libraries]` or `[plugins]`.
3. Reference it via `libs.*` in build files.

### buildSrc Anchors

- `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/CorePlugin.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectPlugins.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectOptions.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/DependencyStrategy.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectSpotless.kt`
- `buildSrc/src/main/java/co/anitrend/buildSrc/resolver/ConfigurationResolver.kt`

### Automatic Dependencies by Module Type

| Module prefix | Auto-included libraries |
|---|---|
| `:feature:*` | support-arch UI/domain/data, Compose, AndroidX core, Koin, Timber |
| `:data:*` | Room + KSP, Retrofit, OkHttp, Kotlinx Serialization, Chucker in debug |
| `:common:*` | support-arch UI, Compose when applicable, Koin |
| `:task:*` | WorkManager, support-arch, Koin |
| `:android:*` | AndroidX core, support-arch, Koin |

### Build Flavors and Variants

- `google`: includes Firebase Analytics and Crashlytics when `google-services.json` is present.
- `oss` or equivalent: open-source variant without proprietary services.

Guard flavor-specific libraries and runtime behavior appropriately.

## Testing, Migration, and Verification

### Build, Test, and Quality Gates

- Format: `./gradlew spotlessApply`
- Static checks: `./gradlew lint spotlessCheck`
- Unit tests: `./gradlew testDebugUnitTest --no-daemon`
- Instrumented tests: `./gradlew connectedDebugAndroidTest --no-daemon`

### Testing Defaults

- Unit tests use JUnit 4, MockK, Turbine, and `kotlinx-coroutines-test`.
- UI tests belong in `androidTest/` and should document emulator or device requirements.
- For DI changes, add focused Koin validation coverage instead of relying only on broad graph checks.
- Do not remove or weaken unrelated tests to make a change pass.

### Room Migration Checklist

When changing schema-impacting Room code:

- Bump `DATABASE_SCHEMA_VERSION` and declare the correct migration path.
- Export and inspect schema JSON under
  `data/schemas/co.anitrend.data.android.database.AniTrendStore/`.
- Build the affected module or app to confirm schema export and annotation processing.
- Run a runtime smoke test against an older on-device database.
- Confirm join tables still preserve multiple rows and composite uniqueness when required.

### Runtime Investigation Default

Prefer Argent-first runtime investigation for Android regressions before changing serializers,
mappers, or UI assumptions. Use ADB and Chucker fallback only when Argent evidence is insufficient.

## Optional Global Skills

If global skills are installed under `~/.agents/skills`, prefer these before using the matching
MCP tools directly:

- code-review-graph-explore
- code-review-graph-debug
- code-review-graph-refactor
- code-review-graph-review

These are optional global skills. Do not treat them as repository-local files.

## AI-Native SDLC Packs

- `docs/support-arch/instructions/*.md` contains autonomous operational playbooks for CI,
  migrations, API contract auditing, and QA or compliance routines.
- `docs/support-arch/prompts/*.md` contains paired prompt templates that invoke those playbooks.
- When adding a new pack, link it from `.agents/skills/reference-map/SKILL.md`.

## Documentation Change Rules

- Keep stable repository policy and architecture intent in `AGENTS.md`.
- Keep implementation detail, examples, and checklists in skill files.
- Keep support-arch operational workflows in `docs/support-arch/**`.
- Add links to `.agents/skills/reference-map/SKILL.md` instead of copying long prose across files.
- When adding a new skill, link it from `AGENTS.md` routing or the reference map.
- When changing a workflow that already has a repo-local skill, update that skill's examples,
  reference files, and "Key files to read" anchors in the same change.
- When a skill becomes the canonical implementation or navigation guide for a workflow, add an
  explicit reference to it in `AGENTS.md`.
- Do not leave `AGENTS.md` routing stale when a skill is renamed, split, gains stronger examples,
  or becomes the preferred path for implementation.
- If a referenced skill includes example matrices, templates, scripts, or `references/` guidance,
  keep those assets aligned with the current code paths and repo conventions.

## Context Receipt Requirement

Before editing code for any non-trivial task, agents must identify:

- task scope
- loaded repo policy files
- loaded skill files
- closest reference implementation
- expected layer changes
- verification commands

If a required repo-local path is missing, stop and report the missing path before editing.

## Security and Config

- Never commit secrets or tokens.
- Keep local credentials in `.config/*.properties` or `local.properties`.
- Update `proguard-common.pro` when adding reflection-heavy libraries.

## Canonical Code Anchors

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
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
