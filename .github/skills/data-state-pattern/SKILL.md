---
name: data-state-pattern
description: 'DataState and UiState workflow guide for repositories and data sources. Use when implementing or reviewing data flow, refresh/retry behavior, and repository return contracts.'
---

# Skill: DataState / UiState Pattern

## Overview

`DataState<T>` (a subtype of `UiState<T>`) is the standard wrapper for all data streams in the
data layer. It pairs a `Flow` of the requested model with a `Flow` of loading/error status, and
provides built-in refresh and retry support.

## Key files to read

- `data/android/src/main/kotlin/co/anitrend/data/android/` — base data-source implementations
- `data/src/main/kotlin/co/anitrend/data/tag/repository/TagRepository.kt` — canonical example of
  a repository returning `DataState`
- `domain/src/main/kotlin/co/anitrend/domain/tag/repository/ITagRepository.kt` — matching domain
  interface that declares the `DataState`-typed contract
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` — how `StateLayoutConfig` and
  dispatchers are registered so UI can bind to `DataState` streams

## Usage pattern

```
domain interface  →  DataState<T>
       ↑
data repository   →  source create source(params)   // infix helper from support-arch
       ↑
data source       →  extends AbstractDataSource, performs network + DB ops
```

1. **Domain layer** — the repository interface returns `DataState<DomainModel>`.
2. **Data layer** — the concrete repository calls `source create source(params)` (an infix
   function from `support-arch`) to wrap a data source into a `DataState`.
3. **Presentation layer** — the ViewModel collects the `DataState`, exposes it as `StateFlow`,
   and the Compose UI observes it via `collectAsState()`.

## Rules

- Never return a raw value or `LiveData` from a repository; always return `DataState`.
- Use `DataState.refresh()` / `DataState.retry()` to trigger re-fetches; do not re-create
  the entire `DataState`.
- Dispatching: the support-arch base classes already schedule network/DB work on `Dispatchers.IO`;
  avoid wrapping calls in an extra `withContext` unless the base class is not used.
