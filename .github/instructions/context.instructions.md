---
applyTo: **
description: This file describes the overall architecture and module structure of the AniTrend v2 project.
---

# Clean Architecture Overview

## Reference Routing

This file defines architecture boundaries and module intent. For focused implementation details,
follow these companion skills:

- `.github/skills/data-state-pattern/SKILL.md`
- `.github/skills/layered-module-patterns/SKILL.md`
- `.github/skills/android-platform-patterns/SKILL.md`
- `.github/skills/navigation-architecture/SKILL.md`
- `.github/skills/product-designer/SKILL.md`
- `.github/skills/koin-module-wiring/SKILL.md`
- `.github/skills/new-module-checklist/SKILL.md`
- `.github/skills/string-resource-inline-comments/SKILL.md`
- `.github/skills/string-resources-convention/SKILL.md`
- `.github/skills/mapping-graphql-models/SKILL.md`
- `.github/skills/reference-map/SKILL.md`

Documentation policy: keep high-level boundaries here, move deep procedural detail into skills,
and validate links with `.github/scripts/audit-instruction-refs.sh`.
For concrete file anchors across layers, use
`.github/skills/reference-map/references/layer-example-matrix.md`.

AniTrend v2 follows a **multi-layered Clean Architecture** with domain and data layers consumed
through feature, common, and task entry points:

- **Domain layer** – Pure Kotlin: params, repository contracts, and abstract use cases
  (`*UseCase` / `*Interactor`). For multi-operation domains, prefer nested contracts such as
  `IMediaRepository.Detail/Paged` or `IReviewRepository.Entry/Rate/Delete`. No Android framework
  code. Read `domain/` for examples.
- **Data layer** – Implements domain contracts; handles GraphQL, Room, paging, and caching.
  Produces concrete `DataState<T>` specializations of the domain `UiState<T>` contracts and
  exposes alias-based interactors through each module `Types.kt`. Read `data/` for examples.
  For the DataState contract and pattern variants see `.github/skills/data-state-pattern/SKILL.md`
  and `.github/skills/layered-module-patterns/SKILL.md`.
- **Android/platform layer** – `:android:*` modules hold Android-specific shared helpers such as
  configuration, theme, notification, context/fragment utilities, drawer shell wiring, and
  deep-link entry. Read `android/` before inventing a new helper API, and see
  `.github/skills/android-platform-patterns/SKILL.md` for reuse rules and concrete anchors.
- **Entry layers** – `:feature:*` UI modules, shared `:common:*` presenters/controllers, and
  `:task:*` WorkManager modules. They consume interactors and routers, then observe `DataState`
  outputs or terminal worker states.

**String resources** follow semantic prefix conventions (`label_`, `title_`, `action_`, etc.),
and every resource block in `strings.xml` must have an XML comment immediately above it so
POEditor translators receive clear context. See `.github/skills/string-resources-convention/SKILL.md`,
`.github/skills/string-resource-inline-comments/SKILL.md`, and
`.github/skills/string-resources-convention/references/android-string-resource-best-practices.md`
for naming, audit workflow, and Android platform behavior.

## Module Organization and Naming

Module paths are centrally registered in
`buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`. Key groups:

| Prefix | Purpose |
|---|---|
| `:app:` | App entry point (`:app:main`), core init (`:app:core`), navigation (`:app:navigation`) |
| `:domain` | Use cases, repository interfaces, domain models |
| `:data:*` | Repository implementations, data sources, Room entities, external API integrations |
| `:android:*` | Android-specific platform helpers, shell navigation, theme/configuration, and deep-link entry |
| `:common:*` | Reusable UI logic/components shared across multiple feature screens |
| `:feature:*` | One module per screen or user-facing flow (Compose UI, ViewModels) |
| `:task:*` | WorkManager background jobs |

When adding a new module, register it in `Modules.kt` and follow the full checklist in
`.github/skills/new-module-checklist/SKILL.md`.

## Communication Between Layers

```
ViewModel / Presenter / Worker
        →  XxxInteractor alias
        →  Domain use case
        →  Domain repository contract
              ↑
        Data repository  →  Source / controller / cache  →  API / DB
```

- Feature, common, and task modules consume interactors only. Imports like
  `co.anitrend.data.media.GetDetailMediaInteractor` are acceptable because they alias domain use
  cases; importing data repositories, sources, mappers, controllers, or remote models is not.
- Koin wires domain repository specializations to data repositories and exposes alias-based
  interactors at runtime. See `.github/skills/koin-module-wiring/SKILL.md`.
- Before adding a new helper around Android system APIs, inspect `:android:*` and `:app:core`
  first. Shared helpers should live there instead of being recreated in entry-layer modules.
- Use the reference that matches the shape of the work:
  - `tag` for the smallest query-only baseline.
  - `media` for read-heavy multi-contract flows.
  - `medialist` and `review` for hybrid query plus mutation patterns.
  - `favourite` plus `task/favourite` for mutation-only task-backed flow.

## Edge Modeling Guidance

For `:data:edge`, the upstream schema is the source of truth for remote models. Keep serialized
edge models concrete and schema-faithful. Adapt data in the layers already built for it:

- **Converters** — translate schema-shaped remote models into local entities.
- **Mappers** — coordinate parsing, persistence, and cross-entity normalization.
- **Entities / entity views** — represent the persisted local shape.
- **Higher-level view converters** (e.g., `MediaEntityView`) — assemble the final app-facing graph.

Never embed compatibility hacks or inferred IDs directly in serialized models.

## Support-arch Integration

`support-arch` standardises patterns across all layers:

- `DataState<T>` — data stream with refresh/retry (see `.github/skills/data-state-pattern/SKILL.md`).
- `AbstractDataSource` — base for all data sources; handles IO threading internally.
- `StateLayoutConfig` — standard empty/loading/error UI config, registered via Koin.
- `SupportFragment` / `SupportFragmentList` — base fragment classes for non-Compose screens.
- **Timber** is used globally for logging; do not use `Log.*` or `println`.

## Jetpack Compose and UI Patterns

AniTrend UI is hybrid. Newer surfaces often use **Jetpack Compose** with Material3
(`AniTrendTheme3`), while many existing flows still bridge Compose hosts to fragments, controllers,
and `AniTrendViewModelState` observers. Navigation uses a mix of feature-local Compose flows,
fragment hosts, deep links, and shared router contracts.

Navigation remains hybrid: deep links enter through `:android:deeplink`, cross-feature routing
goes through shared router contracts in `:app:navigation`, and many feature screens still bridge
Compose hosts to fragment content while newer screens may keep navigation local to a feature.
Shared Android helpers such as theme/configuration, shell navigation, and notification flows live
in `:android:core` and `:android:navigation`, then get consumed by `:app:core` and the app shell.
See `.github/skills/navigation-architecture/SKILL.md` for the end-to-end flow.

Key UI conventions:
- Composables should be small and focused; include `@Preview` functions.
- State enters feature code through interactors and `DataState`, then flows through ViewModels,
  presenters, or workers to UI and background execution.
- Use `MaterialTheme` tokens for colors and typography; never hard-code values.
- Accompanist libraries are preconfigured for pager, system UI, etc.

---
