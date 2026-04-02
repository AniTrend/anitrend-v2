---
applyTo: **
description: This file describes the overall architecture and module structure of the AniTrend v2 project.
---

# Clean Architecture Overview

## Reference Routing

This file defines architecture boundaries and module intent. For focused implementation details,
follow these companion skills:

- `.github/skills/data-state-pattern/SKILL.md`
- `.github/skills/koin-module-wiring/SKILL.md`
- `.github/skills/new-module-checklist/SKILL.md`
- `.github/skills/string-resources-convention/SKILL.md`
- `.github/skills/reference-map/SKILL.md`

Documentation policy: keep high-level boundaries here, move deep procedural detail into skills,
and validate links with `.github/scripts/audit-instruction-refs.sh`.

AniTrend v2 follows a **multi-layered Clean Architecture** with three distinct layers:

- **Domain layer** – Pure Kotlin: use cases (`*UseCase` / `*Interactor`), repository interfaces
  (`IXxxRepository`), and domain models. No Android framework code. Read `domain/` for examples.
- **Data layer** – Implements domain interfaces; handles network, Room DB, and caching. Produces
  `DataState<T>` / `UiState<T>` streams (from `support-arch`). Read `data/` for examples.
  For the full DataState pattern see `.github/skills/data-state-pattern/SKILL.md`.
- **Presentation layer** – Feature modules with Composables, ViewModels, and Activities/Fragments.
  Consumes domain use cases and observes `DataState` outputs.

**String resources** follow semantic prefix conventions (`label_`, `title_`, `action_`, etc.).
See `.github/skills/string-resources-convention/SKILL.md` for the full convention.

## Module Organization and Naming

Module paths are centrally registered in
`buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`. Key groups:

| Prefix | Purpose |
|---|---|
| `:app:` | App entry point (`:app:main`), core init (`:app:core`), navigation (`:app:navigation`) |
| `:domain` | Use cases, repository interfaces, domain models |
| `:data:*` | Repository implementations, data sources, Room entities, external API integrations |
| `:android:*` | Android-specific shared utilities (context helpers, deep links) |
| `:common:*` | Reusable UI logic/components shared across multiple feature screens |
| `:feature:*` | One module per screen or user-facing flow (Compose UI, ViewModels) |
| `:task:*` | WorkManager background jobs |

When adding a new module, register it in `Modules.kt` and follow the full checklist in
`.github/skills/new-module-checklist/SKILL.md`.

## Communication Between Layers

```
ViewModel  →  XxxUseCase (domain)  →  IXxxRepository (domain interface)
                                            ↑
                                      XxxRepository (data)  →  XxxSource → API / DB
```

- Feature modules call domain use cases only; they never import data-layer classes directly.
- Koin wires `IXxxRepository → XxxRepository` at runtime. See `.github/skills/koin-module-wiring/SKILL.md`.
- The **Tag** package (`domain/src/main/kotlin/co/anitrend/domain/tag/` and
  `data/src/main/kotlin/co/anitrend/data/tag/`) is the canonical reference implementation.

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

Most screens are built with **Jetpack Compose** using Material3 (`AniTrendTheme3`). Navigation
uses **AndroidX Navigation Compose** (NavHost in `:app:navigation`). ViewModels expose
`StateFlow` consumed via `collectAsState()`.

Key UI conventions:
- Composables should be small and focused; include `@Preview` functions.
- State hoisting: UI state flows from ViewModel down to Composables.
- Use `MaterialTheme` tokens for colors and typography; never hard-code values.
- Accompanist libraries are preconfigured for pager, system UI, etc.

---
