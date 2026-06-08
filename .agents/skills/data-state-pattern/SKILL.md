---
name: data-state-pattern
description: 'DataState and UiState workflow guide for repositories and data sources. Use when implementing or reviewing data flow, refresh/retry behavior, and repository return contracts.'
---

# Skill: DataState / UiState Pattern

## Overview

`DataState<T>` is the concrete data-layer specialization of the domain `UiState<T>` contract. It
pairs a `Flow` of the requested model with a `Flow` of loading/error status, and provides built-in
refresh and retry support.

The read guidelines below apply only to non-mutation source shapes. Mutation-only variants are
covered in the mutation section and are exempt from the Room-first read contract.

## Key files to read

- `data/android/src/main/kotlin/co/anitrend/data/android/` - base data-source implementations
- `data/src/main/kotlin/co/anitrend/data/tag/repository/TagRepository.kt` - baseline example of a
  repository returning `DataState`
- `domain/src/main/kotlin/co/anitrend/domain/tag/repository/ITagRepository.kt` - matching domain
  interface that declares the `DataState`-typed contract
- `data/src/main/kotlin/co/anitrend/data/media/` - read-heavy module showing alias-based
  repositories and interactors for `Detail`, `Paged`, and `Network`
- `domain/src/main/kotlin/co/anitrend/domain/medialist/` and
  `data/src/main/kotlin/co/anitrend/data/medialist/` - hybrid query + mutation flow with
  operation-specific repository contracts, aliases, and concrete interactors
- `domain/src/main/kotlin/co/anitrend/domain/review/` and
  `data/src/main/kotlin/co/anitrend/data/review/` - paged/detail queries plus rate/save/delete
- `domain/src/main/kotlin/co/anitrend/domain/favourite/` and
  `data/src/main/kotlin/co/anitrend/data/favourite/` - compact mutation-only toggle flow
- `task/medialist/src/main/kotlin/co/anitrend/task/medialist/` and
  `task/review/src/main/kotlin/co/anitrend/task/review/` - workers waiting for terminal
  `loadState` after invoking mutation interactors
- `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` - how `StateLayoutConfig` and
  dispatchers are registered so UI can bind to `DataState` streams

## Usage pattern

```
ViewModel / Presenter / Worker
  -> data.*Interactor alias
  -> domain use case
  -> domain repository contract
  -> data repository
  -> source create source(params)   // infix helper from support-arch
  -> data source / controller / store
```

1. Domain layer - repository contracts and abstract use cases are generic over `UiState<T>`.
2. Data layer - `Types.kt` aliases specialize those contracts to `DataState<T>`, and the
  concrete repository calls `source create source(params)` to wrap a source into a `DataState`.
3. Entry layers - feature ViewModels or common presenters post/observe the `DataState`, while
  task workers typically await a terminal `loadState` before returning `Result.success()` or
  `Result.failure()`.

## Mutation-specific pattern

- Keep repository contracts in `:domain`, even for a single toggle or save/delete mutation.
- Keep abstract use cases in `:domain` as `XxxUseCase` / `XxxInteractor` base classes.
- Keep each module's `Types.kt` lean: use it for controller aliases, specialized repository
  aliases, and interactor aliases only.
- Put the concrete data-layer use-case bridge in the module's `usecase/` package. Modules that
  expose one operation should use a single `XxxUseCaseImpl`. Modules that expose two or more
  distinct operations should use nested `XxxInteractor` classes such as `MediaListInteractor`,
  `ReviewInteractor`, or `FavouriteInteractor`.
- Query sources usually emit `Flow<Model>`, `Flow<List<Model>>`, or `Flow<PagedList<Model>>`;
  mutation sources often emit `Flow<Boolean?>` or a persisted model and then rely on the
  repository wrapper to expose the final `DataState`.

## Offline-first non-paged read pattern

Use this pattern for single-entity reads or fixed-size collections that should render from Room
first and refresh from the network opportunistically.

- The source contract should extend `AbstractCoreDataSource` and expose
  `observable(): Flow<Model>` or `observable(): Flow<List<Model>>`.
- The source `invoke(...)` operator should store any query context it needs, call `cachePolicy(...)`
  with the source cache identity, and return `observable()` immediately.
- `observable()` should read local Room state and project it into the domain model.
- Include `filterNotNull()` when the Room query can emit null before the first insert.
- Include `distinctUntilChanged()` when downstream collectors act on repeated emissions.
- Omit `distinctUntilChanged()` when the UI layer already performs change detection or the source
  changes infrequently.

### Source contract

- The source contract should extend `AbstractCoreDataSource` and expose
  `observable(): Flow<Model>` or `observable(): Flow<List<Model>>`.

### invoke() contract

- The source `invoke(...)` operator should store any query context it needs, call `cachePolicy(...)`
  with the source cache identity, and return `observable()` immediately.

### observable() pipeline

- `observable()` should read local Room state and project it into the domain model.
- Include `filterNotNull()` when the Room query can emit null before the first insert.
- Include `distinctUntilChanged()` when downstream collectors act on repeated emissions.
- Omit `distinctUntilChanged()` when the UI layer already performs change detection or the source
  changes infrequently.

### Persistence

- `get*()` methods should only orchestrate the remote refresh path through the controller and
  return `Boolean` success so `cachePolicy(...)` can update the last request timestamp.
- Persistence still belongs to the controller and mapper chain. Do not manually merge cached rows,
  build domain models from remote payloads inline, or make `observable()` depend on network work.

### clearDataSource()

- `clearDataSource(...)` for read flows should invalidate the relevant cache identity and clear the
  local rows that back `observable()`.

### Non-paged source shapes

- Use `Flow<List<Model>>` for immutable or fixed-size collections such as `TagSource` and
  `GenreSource`.
- Use `Flow<Model>` for singleton or detail reads such as `EdgeConfigSource`,
  `MediaSource.Detail`, and `ReviewSource.Entry`.
- For entity families with multiple read contexts, define separate source variants for each
  contract. A family is multi-context when the same entity type is fetched through at least two
  structurally different query shapes, such as a detail lookup by id and a paged list filtered by
  criteria.

### Ordering

- If the same parent resource is requested with a filter or limit that materially changes the
  expected result count, use a variant cache key while continuing to read from the same local
  table.
- Use the same cache key when only sort order or display format differs without affecting which
  rows are fetched.

### Fixed-size detail reads

Non-paged child collections and aggregate detail reads should still follow the same Room-first
contract.

- Persist fixed-size collections in dedicated connection or detail tables keyed by the parent id.
- Store a stable display order such as `sort_index` so every cached route renders consistently.
- Keep the parent context needed for persistence inside the mapped response model. The source should
  trigger refreshes, not rebuild local rows from remote models itself.
- Clear parent-scoped rows from `clearDataSource()` before forcing the next refresh.
- Do not use `MutableStateFlow` as the primary backing store when a Room table already exists for
  the same read.

### Red flags

- Mapper `persist(...)` methods left empty for a read that claims to be offline-first.
- Source contracts that clear `MutableStateFlow` and fire the network on every `invoke(...)`.
- Local tables without ordering columns for remote relationship collections.
- Response models that omit parent identity and force mutable source or mapper request state to
  rebuild persistence context.
- Cache identities keyed too broadly for request variants that intentionally fetch different result
  sizes.

### Contrast with mutation-only variants

- Mutation sources such as `ReviewSource.Rate/Delete/Save` and
  `UserSource.ToggleFollow/Update` are not offline-first read baselines.
- They commonly expose terminal state streams such as `MutableStateFlow<Boolean?>` or reuse a
  persisted entity stream after the controller mutates local state.
- Keep their request orchestration and `observable()` contracts separate from the non-paged read
  pattern so read guidance does not get conflated with mutation-only flows.

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
- Allow the mapper to receive request context whenever the persistence operation must scope writes
  to a specific parent entity or determine which existing rows to clear before appending new ones.
- Convert connection entities back to the domain model in a local converter that the source uses
  when building the `FlowPagedListBuilder` pipeline.

### Mapper sidecar persistence

When a parent mapper needs to persist related local-only rows as part of the same response, keep
those writes behind `EmbedMapper` helpers such as `UserMapper.GeneralOptionEmbed` or
`UserMapper.NotificationEmbed`.

- Prefer introducing a new `XxxEmbed` mapper over injecting an additional DAO directly into the
  parent mapper.
- Keep placeholder or sidecar persistence rules inside that embed helper, even when the helper uses
  custom DAO methods instead of a plain `upsert`.
- Let the parent mapper coordinate embeds with `onEmbedded(...)` and `persistEmbedded()` so mapper
  wiring stays consistent across query and mutation flows.

## Rules

The following rules are authoritative; where they overlap with pattern-section guidance, the rule
below governs.

- Never return a raw value or `LiveData` from a non-paged repository; always return `DataState`.
- Paged repository contracts may return `Flow<PagingData<T>>` through the existing paging
  interactor aliases. Do not convert paging flows to `DataState` unless the existing module
  pattern already does so.
- Use `DataState.refresh()` / `DataState.retry()` to trigger re-fetches; do not re-create the
  entire `DataState`.
- Dispatching: the support-arch base classes already schedule network/DB work on `Dispatchers.IO`;
  avoid wrapping calls in an extra `withContext` unless the base class is not used.
- An import such as `co.anitrend.data.review.GetReviewPagedInteractor` in `feature` or `task`
  code is acceptable because it aliases a domain use case. Importing `ReviewRepository`,
  `ReviewSourceImpl`, or `ReviewMapper` into those layers is not.
- For DB-backed non-paged reads, prefer `AbstractCoreDataSource` plus a cache-policy-gated local
  observable flow. Do not model them as paging sources or network-only live-data sources.
- For DB-backed paged reads, do not choose `SupportPagingLiveDataSource` unless the flow is truly
  network-only. If a local source exists, prefer `AbstractPagingSource` plus a local observable
  flow.
- When `get*()` catches a network or parsing exception, emit the error into the source's
  `loadState` flow via the base-class error helper rather than returning `false` silently.
  Returning `false` is reserved for expected cache-miss or empty-result conditions.
- Non-paged repository streams return `DataState<T>`.
