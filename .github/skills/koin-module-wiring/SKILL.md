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
- `data/src/main/kotlin/co/anitrend/data/tag/koin/` — example of a data-module Koin file that
  binds `ITagRepository → TagRepository` and provides `TagUseCaseImpl`
- `data/src/main/kotlin/co/anitrend/data/genre/koin/Modules.kt` — another concrete example
- `data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt` — mutation-heavy example
  showing explicit typed mapper lookup for generic `graphQLController(...)` bindings

## Wiring checklist for a new module

1. Create `<module>/src/main/kotlin/.../koin/Modules.kt` with a `val` that returns a Koin `module { }` block.
2. Declare bindings:
   - `single<IXxxRepository> { XxxRepository(get(), get()) }` — data layer concrete
   - `factory<XxxUseCase> { XxxUseCaseImpl(get()) }` — use-case bridge
3. Add the local module to the nearest feature / data aggregator so it gets loaded transitively.
4. If the module is a new `:data:*` or `:feature:*` top-level module, also add its loader to
   `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.

## Rules

- Every public dependency must be exposed through Koin — no direct instantiation in feature code.
- Prefer `single` for repositories and sources (one instance per app lifecycle), `factory` for use
  cases and ViewModels (new instance per consumer).
- Use `get()` to resolve transitive dependencies; never import concrete data-layer classes into a
  feature module's Koin file.
- When a binding depends on a generic contract such as `graphQLController(mapper = ...)`, prefer
  explicit typed lookup like `get<ConcreteMapper>()` instead of bare `get()` so Koin does not have
  to infer an ambiguous generic mapper.
