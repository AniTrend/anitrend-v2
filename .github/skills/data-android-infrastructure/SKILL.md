---
name: data-android-infrastructure
description: >
  data/android module reference. Use when working with ControllerStrategy, OnlineStrategy,
  OfflineStrategy, ScopeExtensions, graphQLController Koin wiring, DefaultController,
  DeferrableNetworkClient, ICacheStore, or when asking what the data/android module provides to
  other data modules and why it is a shared dependency.
---

# `data/android` — Shared Data Infrastructure

## Purpose

`:data:android` is the shared Android infrastructure layer consumed transitively by every
`data:*` module. It is never imported directly from `:feature:*`, `:common:*`, or `:task:*`.
Its role is to provide the reusable plumbing that keeps individual data modules thin — they
import entities, sources, mappers, and repositories; `data/android` provides the machinery
those build on.

## Key residents

| Package | What lives here |
|---|---|
| `controller/graphql` | `GraphQLController` — the full response pipeline |
| `controller/core` | `DefaultController` — non-GraphQL response pipeline |
| `controller/strategy` | `ControllerStrategy` contract + `OnlineStrategy` / `OfflineStrategy` |
| `mapper` | `DefaultMapper` abstract base for all data-layer mappers |
| `network/graphql` | `GraphNetworkClient` + `DeferrableNetworkClient` |
| `network/default` | `DefaultNetworkClient` |
| `cache/datasource` | `ICacheStore` / `CacheDao` — shared cache-log store |
| `extensions/ScopeExtensions.kt` | Koin `Scope` helpers: `graphQLController`, `defaultController`, `online`, `offline`, `cacheLocalSource` |
| `database` | `MigrationHelper`, `AniTrendStore` base |

## `ControllerStrategy` — choosing the right policy

`ControllerStrategy<D>` is an abstract wrapper around a coroutine block that standardises error
capturing and `RequestCallback` signalling.

| Strategy | When to use |
|---|---|
| `OnlineStrategy` | Default. Checks `ISupportConnectivity.isConnected` before executing. Throws `RequestError(connectivityError)` if offline. Use for any source that makes a live network call. |
| `OfflineStrategy` | Skips the connectivity check. Use for sources backed by network-level caching (cache-control headers, OkHttp interceptor cache) where the network client handles offline fallback transparently. |

Both strategies:
- Catch all `Throwable`s from the block and convert them to `RequestError` via
  `Throwable.generateForError()` (maps `SocketTimeoutException`, `HttpException`, etc. to
  human-readable messages from `NetworkMessage`).
- Call `callback.recordSuccess()` or `callback.recordFailure()` so `DataState` subscribers
  receive the correct loading state.

## `ScopeExtensions` — Koin wiring helpers

`ScopeExtensions.kt` lives in `data/android/extensions` and provides `Scope` extension functions
so individual module `KoinModule.kt` files don't repeat controller-construction boilerplate.

```kotlin
// Constructs a GraphQLController with the standard GraphNetworkClient
fun <S, D> Scope.graphQLController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),  // OnlineStrategy by default
    dispatcher: ISupportDispatcher = get(),
): GraphQLController<S, D>

// Constructs a DefaultController (non-GraphQL endpoints)
fun <S, D> Scope.defaultController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get(),
): DefaultController<S, D>

// Convenience wrappers for strategies
fun <T> Scope.online(): OnlineStrategy<T>
fun <T> Scope.offline(): OfflineStrategy<T>

// Shared cache DAO
fun Scope.cacheLocalSource(): CacheDao
```

Always prefer `get<ConcreteMapper>()` over bare `get()` inside `graphQLController(...)` Koin
bindings so generic type resolution stays explicit.

## `data/android` as a transitive dependency

Individual `data:*` modules add `data:android` to their `build.gradle.kts` via the shared
`applyDataDependencies()` helper in `ProjectDependencies.kt`. They never re-declare Retrofit,
OkHttp, or the `retrofit-graphql` converter themselves — those come through `data:android`.

## Related skills

- `graphql-query-pattern/SKILL.md` — annotation and query-lifecycle details (`@GRAPHQL`,
  `@GraphQuery`, `QueryContainerBuilder`, `Response<GraphQLResponse<*>>`)
- `data-state-pattern/SKILL.md` — how `DataState` and `RequestCallback` interact
- `koin-module-wiring/SKILL.md` — how module `KoinModule.kt` files call `graphQLController`
