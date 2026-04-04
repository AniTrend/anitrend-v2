---
name: data-state-pattern
description: 'DataState and UiState workflow guide for repositories and data sources. Use when implementing or reviewing data flow, refresh/retry behavior, and repository return contracts.'
---

# Skill: DataState / UiState Pattern

## Overview

`DataState<T>` is the concrete data-layer specialization of the domain `UiState<T>` contract. It
pairs a `Flow` of the requested model with a `Flow` of loading/error status, and provides built-in
refresh and retry support.

## Key files to read

- `data/android/src/main/kotlin/co/anitrend/data/android/` — base data-source implementations
- `data/src/main/kotlin/co/anitrend/data/tag/repository/TagRepository.kt` — baseline example of a
  repository returning `DataState`
- `domain/src/main/kotlin/co/anitrend/domain/tag/repository/ITagRepository.kt` — matching domain
  interface that declares the `DataState`-typed contract
- `data/src/main/kotlin/co/anitrend/data/media/` — read-heavy module showing alias-based
  repositories and interactors for `Detail`, `Paged`, and `Network`
- `domain/src/main/kotlin/co/anitrend/domain/medialist/` and
  `data/src/main/kotlin/co/anitrend/data/medialist/` — hybrid query + mutation flow with
  operation-specific repository contracts, aliases, and concrete interactors
- `domain/src/main/kotlin/co/anitrend/domain/review/` and
  `data/src/main/kotlin/co/anitrend/data/review/` — paged/detail queries plus rate/save/delete
- `domain/src/main/kotlin/co/anitrend/domain/favourite/` and
  `data/src/main/kotlin/co/anitrend/data/favourite/` — compact mutation-only toggle flow
- `task/medialist/src/main/kotlin/co/anitrend/task/medialist/` and
  `task/review/src/main/kotlin/co/anitrend/task/review/` — workers waiting for terminal
  `loadState` after invoking mutation interactors
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` — how `StateLayoutConfig` and
  dispatchers are registered so UI can bind to `DataState` streams

## Usage pattern

```
ViewModel / Presenter / Worker
       →  data.*Interactor alias
       →  domain use case
       →  domain repository contract
       →  data repository
       →  source create source(params)   // infix helper from support-arch
       →  data source / controller / store
```

1. **Domain layer** — repository contracts and abstract use cases are generic over `UiState<T>`.
2. **Data layer** — `Types.kt` aliases specialize those contracts to `DataState<T>`, and the
   concrete repository calls `source create source(params)` to wrap a source into a `DataState`.
3. **Entry layers** — feature ViewModels or common presenters post/observe the `DataState`, while
   task workers typically await a terminal `loadState` before returning `Result.success()` or
   `Result.failure()`.

## Mutation-specific pattern

- Keep repository contracts in `:domain`, even for a single toggle or save/delete mutation.
- Keep abstract use cases in `:domain` as `XxxUseCase` / `XxxInteractor` base classes.
- Keep each module's `Types.kt` lean: use it for controller aliases, specialized repository
  aliases, and interactor aliases only. This is the public dependency surface that feature and
  task modules usually consume.
- Put the concrete data-layer use-case bridge in the module's `usecase/` package. Simple modules
  may use a single `XxxUseCaseImpl`; operation-heavy modules may use nested `XxxInteractor`
  classes such as `MediaListInteractor`, `ReviewInteractor`, or `FavouriteInteractor`.
- Query sources usually emit `Flow<Model>` or `Flow<PagedList<Model>>`; mutation sources often
  emit `Flow<Boolean?>` or a persisted model and then rely on the repository wrapper to expose the
  final `DataState`.

## Offline-first paged read pattern

Use this pattern whenever the UI should page over locally persisted data and refresh from the
network opportunistically.

- The source contract should extend `AbstractPagingSource<T>` and expose
  `observable(): Flow<PagedList<T>>`.
- `observable()` should be built from a local `DataSource.Factory` using `FlowPagedListBuilder`,
  with a converter mapping local entities or views into domain models.
- The source should orchestrate refresh timing only: initial cache-policy gating and
  `cacheIdentity(...)` paging callbacks for append or zero-item refreshes.
- If Room is the source of truth, the controller generic should resolve to the persisted entity
  shape, not the final domain model. Let the mapper produce local entities and persist them.
- Do not perform domain-model assembly, local-page merging, or ad hoc cache replacement inside the
  source implementation. Those responsibilities belong to converters and mappers.

### Relationship collection variant

Some paged reads are not a top-level entity table but a relationship collection, for example a
media detail screen exposing characters or staff.

- Persist those rows in dedicated connection tables keyed by the parent id plus the related id.
- Store explicit ordering information such as `sort_index` so local paging reproduces the remote
  list order.
- Allow the mapper to receive request context when needed, for example the parent id or current
  page, so it can clear or append the correct connection rows during persistence.
- Convert connection entities back to the domain model in a local converter that the source uses
  when building the `FlowPagedListBuilder` pipeline.

## Rules

- Never return a raw value or `LiveData` from a repository; always return `DataState`.
- Use `DataState.refresh()` / `DataState.retry()` to trigger re-fetches; do not re-create
  the entire `DataState`.
- Dispatching: the support-arch base classes already schedule network/DB work on `Dispatchers.IO`;
  avoid wrapping calls in an extra `withContext` unless the base class is not used.
- An import such as `co.anitrend.data.review.GetReviewPagedInteractor` in `feature` or `task`
  code is acceptable because it aliases a domain use case. Importing `ReviewRepository`,
  `ReviewSourceImpl`, or `ReviewMapper` into those layers is not.
- For DB-backed paged reads, do not choose `SupportPagingLiveDataSource` unless the flow is truly
  network-only. If a local source exists, prefer `AbstractPagingSource` plus a local observable
  flow.
