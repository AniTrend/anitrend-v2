---
name: koin-module-wiring
description: 'Koin dependency wiring pattern for AniTrend modules. Use when adding repository bindings, use case providers, module loaders, or app-level DI aggregation.'
---

# Skill: Koin Module Wiring

## Overview

Koin is the dependency injection framework used throughout AniTrend v2. Every feature or data
module exposes its bindings through a local `Modules.kt` file. Those local modules are collected
by the app-level aggregator and loaded at startup via `InjectorInitializer`.

For cross-layer Koin anchors, including Android platform modules, use the
[layer example matrix](../reference-map/references/layer-example-matrix.md). Pair Android helper
work with [android-platform-patterns](../android-platform-patterns/SKILL.md).

Registration precedence: register bindings in the `Modules.kt` file that belongs to the immediate
parent Gradle module first, then add that parent aggregator to `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` when the parent module is a new top-level module that must be loaded at startup.

## Key files to read

- `app/core/src/main/kotlin/co/anitrend/core/initializer/injector/InjectorInitializer.kt` —
  AndroidX Startup initializer that bootstraps Koin with all collected modules
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` — app-level Koin module
  aggregator; registers core singletons (Coil, Emoji, StateLayoutConfig, dispatchers, etc.)
- `android/core/src/main/kotlin/co/anitrend/android/core/koin/Modules.kt` — Android platform
  helpers such as settings, configuration, theme, notification, storage, shortcut, and power
  controllers
- `android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/koin/Modules.kt` —
  app-shell drawer presenter/viewmodel/fragment/provider composition
- `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/Modules.kt` — deep-link
  parser, presenter, viewmodel, and provider composition
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
   - Android platform helpers: bind controllers, helpers, providers, or shell fragments in the
     owning `:android:*` module instead of a feature module
4. Add the local module to the `Modules.kt` file in its immediate parent Gradle module so it gets
  loaded transitively.
5. If that parent Gradle module is new and must be loaded at app startup, also add its loader to
  `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.
6. Add or update a Koin resolution test for the changed binding. For central `:data` wiring,
   prefer a focused `src/test` case that starts Koin with the relevant modules and resolves the
   exact contract you changed.

## Rules

- Every public dependency must be exposed through Koin — no direct instantiation in feature code.
- In data modules, default to `factory` for sources, mappers, converters, repositories, and
  interactors unless the dependency is intentionally app-wide state or configuration. Use `single`
  only when the type is intentionally shared app-wide and an existing sibling module already binds
  it that way.
- Use `get()` to resolve transitive dependencies; never import concrete data-layer classes into a
  feature or task module's Koin file except for the worker or ViewModel class being declared.
- When binding reusable Android-side helpers, keep the binding in `:android:*` and let
  `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` include the Android aggregator.
- When a binding depends on a generic contract such as `graphQLController(mapper = ...)` or a
  constructor parameter is declared as a broad interface or typealias, request the concrete
  dependency explicitly with `get<ConcreteType>()` instead of bare `get()`. Cover that path with a
  Koin resolution test.
- Task Koin files should bind workers and router providers only. Repository, source, mapper, and
  controller bindings stay in the owning data module.
