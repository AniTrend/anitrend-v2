# data/core/

## Responsibility

`data/core/` supplies shared data-layer infrastructure for Android-backed stores and Retrofit-backed requests. It includes cache policy, controllers, Room helpers, mapper bases, network clients, logging, paging support, source base classes, and query-building helpers.

## Design Patterns

- Controller strategy pattern: `OnlineStrategy` and `OfflineStrategy` wrap request execution and load state emission through `ControllerStrategy`.
- Mapper contract pattern: `DefaultMapper`, `EmbedMapper`, and `PersistEmbedded` isolate remote-to-entity and entity persistence work from sources.
- Cache identity pattern: `CacheRequest`, `CacheIdentity`, and `CacheStorePolicy` decide when independent resources refresh.
- Network adapter pattern: `DeferrableNetworkClient`, Retrofit, OkHttp, and GraphQL clients isolate response extraction and error handling.
- Room utility pattern: common local source and transaction helpers keep DAO implementations consistent.

## Data & Control Flow

A data source builds a deferred Retrofit call, then passes it to `DefaultController` or `GraphQLController`. The controller fetches through a deferrable network client, delegates response conversion to a mapper, persists mapped output on the configured dispatcher, and lets the selected strategy publish request state. Cache helpers gate refresh attempts before controller execution.

## Integration Points

- Used by AniList data packages under `data/src/main/kotlin/co/anitrend/data/`.
- Used by `data/edge/` and `data/feed/` for the same controller, cache, mapper, paging, and source patterns where applicable.
- The files in `data/core/src/main/kotlin/co/anitrend/data/android/network/client/` are existing user-modified code in this worktree and were not edited for this codemap update.
