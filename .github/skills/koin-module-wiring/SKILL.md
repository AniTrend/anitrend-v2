---
name: koin-module-wiring
description: 'Koin dependency wiring pattern for AniTrend modules. Use when adding repository bindings, use case providers, module loaders, or app-level DI aggregation.'
---

# Skill: Koin Module Wiring

## Overview

Koin is the dependency injection framework used throughout AniTrend v2. Every feature or data
module exposes its bindings through a local `Modules.kt` file. Those local modules are collected
by the app-level aggregator and loaded at startup via `InjectorInitializer`.

## Key files to read

- `app/core/src/main/kotlin/co/anitrend/core/initializer/injector/InjectorInitializer.kt` —
  AndroidX Startup initializer that bootstraps Koin with all collected modules
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` — app-level Koin module
  aggregator; registers core singletons (Coil, Emoji, StateLayoutConfig, dispatchers, etc.)
- `data/src/main/kotlin/co/anitrend/data/tag/koin/` — simple query-only example using
  `TagUseCaseImpl`
- `data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt` — mutation-heavy example
  showing explicit typed mapper lookup for generic `graphQLController(...)` bindings
- `data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt` — read-heavy example with
  operation-specific repository and interactor aliases
- `data/src/main/kotlin/co/anitrend/data/review/koin/Modules.kt` and
  `data/src/main/kotlin/co/anitrend/data/favourite/koin/Modules.kt` — hybrid and mutation-only
  examples
- `task/review/src/main/kotlin/co/anitrend/task/review/koin/Modules.kt` — worker bindings for
  task-backed mutation flows

## Wiring checklist for a new module

1. Create `<module>/src/main/kotlin/.../koin/Modules.kt` with a `val` that returns a Koin `module { }` block.
2. Declare bindings using the exported aliases from the module `Types.kt` where applicable:
   - `factory<MediaPagedRepository> { MediaRepository.Paged(source = get()) }`
   - `factory<GetPagedMediaInteractor> { MediaInteractor.Paged(repository = get()) }`
3. Add feature or task entry bindings as needed:
   - `viewModel { XxxViewModel(interactor = get(), ...) }`
   - `worker { scope -> XxxWorker(context = androidContext(), parameters = scope.get(), interactor = get()) }`
4. Add the local module to the nearest feature / data aggregator so it gets loaded transitively.
5. If the module is a new `:data:*` or `:feature:*` top-level module, also add its loader to
   `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.

## Rules

- Every public dependency must be exposed through Koin — no direct instantiation in feature code.
- In data modules, default to `factory` for sources, mappers, converters, repositories, and
  interactors unless the dependency is intentionally app-wide state or configuration. Mirror the
  surrounding module instead of forcing `single`.
- Use `get()` to resolve transitive dependencies; never import concrete data-layer classes into a
  feature or task module's Koin file except for the worker or ViewModel class being declared.
- When a binding depends on a generic contract such as `graphQLController(mapper = ...)`, prefer
  explicit typed lookup like `get<ConcreteMapper>()` instead of bare `get()` so Koin does not have
  to infer an ambiguous generic mapper.
- Task Koin files should bind workers and router providers only. Repository, source, mapper, and
  controller bindings stay in the owning data module.
