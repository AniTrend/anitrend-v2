# data/

## Responsibility

`data/` contains AniTrend data-layer implementations, shared data infrastructure, local persistence, remote API adapters, GraphQL operation assets, and data module integrations used by domain contracts. The scope bridges domain repository interfaces to AniList GraphQL, AniTrend Edge GraphQL, RSS-style feed endpoints, Room stores, Retrofit clients, cache policies, and settings contracts.

## Design Patterns

- Clean data-layer split: repositories depend on source contracts, sources coordinate remote calls, cache policy, Room observables, and mappers.
- Offline-first reads: fixed-size detail flows expose Room-backed `DataState` streams while refresh logic writes network responses into local stores.
- Paging reads: `Pager` and `RemoteMediator` implementations keep Room as the paging source and use cache identities for refresh decisions.
- Registry style DI: each data area has `koin/Modules.kt` bindings for repositories, sources, controllers, mappers, converters, and stores.
- Fragment-first GraphQL: operations in `data/src/main/graphql/` and `data/edge/src/main/graphql/` compose reusable fragments into generated request and model types.

## Data & Control Flow

1. Feature, common, or task code calls a domain interactor alias exported from a data package `Types.kt` or use case package.
2. The data repository delegates to a source contract implementation.
3. The source builds a request from domain params, checks cache identity, calls a Retrofit remote source, and sends the deferred response through a controller.
4. Controllers use mapper objects to transform response models and persist entities in Room.
5. Repository streams return `DataState<T>` or `Flow<PagingData<T>>` after converters adapt entity views to domain entities.

## Integration Points

- `data/core/` provides controllers, cache policy, network clients, mapper bases, paging bases, and Room helpers shared by data packages.
- `data/src/main/kotlin/co/anitrend/data/` contains AniList-backed domain data implementations and shared model pieces.
- `data/src/main/graphql/` contains AniList fragments, queries, and mutations consumed by generated Kotlin request types.
- `data/edge/` contains AniTrend Edge config, media enrichment, news, episode, image, theme, season, navigation, and related persistence.
- `data/feed/` contains feed and episode endpoint integration outside the AniList GraphQL package set.
- `data/settings/` exposes persisted settings contracts consumed by data, feature, and task layers.
- `data/imgur/` currently contributes Imgur data DI surface only.
- `data/android/` is a minimal Android data module shell with manifest-only content.

## Scope Notes

Tests, generated outputs, schema export files, translations, build output, and configuration files are outside this codemap scope. No `data/jikan/`, `data/tmdb/`, or `data/trakt/` directories are present in this checkout.
