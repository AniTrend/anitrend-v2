---
name: data-android-infrastructure
description: >
  data/android module reference. Use when working with ControllerStrategy, OnlineStrategy,
  OfflineStrategy, ScopeExtensions, graphQLController Koin wiring, DefaultController,
  DeferrableNetworkClient, ICacheStore, or when asking what the data/android module provides to
  other data modules and why it is a shared dependency.
---

# `data/android` , Shared Data Infrastructure

## Purpose

`:data:android` is the shared Android infrastructure layer consumed transitively by every
`data:*` module. It is never imported directly from `:feature:*`, `:common:*`, or `:task:*`.
Its role is to provide the reusable plumbing that keeps individual data modules thin. They
import entities, sources, mappers, and repositories, while `data/android` provides the machinery
those build on.

## Key residents

| Package | What lives here |
|---|---|
| `controller/graphql` | `GraphQLController`, the full response pipeline |
| `controller/core` | `DefaultController`, non-GraphQL response pipeline |
| `controller/strategy` | `ControllerStrategy` contract plus `OnlineStrategy` and `OfflineStrategy` |
| `mapper` | `DefaultMapper` abstract base for all data-layer mappers |
| `network/graphql` | `GraphNetworkClient` plus `DeferrableNetworkClient` |
| `network/default` | `DefaultNetworkClient` |
| `cache/datasource` | `ICacheStore` and `CacheDao`, the shared cache-log store |
| `extensions/ScopeExtensions.kt` | Koin `Scope` helpers: `graphQLController`, `defaultController`, `online`, `offline`, `cacheLocalSource` |
| `database` | `MigrationHelper`, `AniTrendStore` base |

## `ControllerStrategy` , choosing the right policy

`ControllerStrategy<D>` is an abstract wrapper around a coroutine block that standardises error
capturing and `RequestCallback` signalling.

| Strategy | When to use |
|---|---|
| `OnlineStrategy` | Default. Checks `ISupportConnectivity.isConnected` before executing. Throws `RequestError(connectivityError)` if offline. Use for any source that makes a live network call. |
| `OfflineStrategy` | Use only when the endpoint is backed by OkHttp cache or cache-control headers so the client can serve a cached response without a live connection. Do not use it for endpoints that have no network-level cache. |

Both strategies:
- Catch all `Throwable`s from the block and convert them to `RequestError` via `Throwable.generateForError()`.
- Call `callback.recordSuccess()` or `callback.recordFailure()` so `DataState` subscribers receive the correct loading state.

## `ScopeExtensions` , Koin wiring helpers

`ScopeExtensions.kt` lives in `data/android/extensions` and provides `Scope` extension functions
so individual module `KoinModule.kt` files do not repeat controller-construction boilerplate.

```kotlin
fun <S, D> Scope.graphQLController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get(),
): GraphQLController<S, D>

fun <S, D> Scope.defaultController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get(),
): DefaultController<S, D>

fun <T> Scope.online(): OnlineStrategy<T>
fun <T> Scope.offline(): OfflineStrategy<T>
fun Scope.cacheLocalSource(): CacheDao
```

## Controller choice

- Use `graphQLController(...)` when the source returns `Response<GraphQLResponse<*>>`.
- Use `defaultController(...)` for plain REST responses that are not wrapped in a GraphQL envelope.
- When passing a mapper argument inside either controller binding, always use `get<ConcreteMapper>()` instead of bare `get()`.

## GraphQL converter wiring

The shared GraphQL wiring now lives in `data/src/main/kotlin/co/anitrend/data/android/koin/Modules.kt`.
Key points:
- generated AniList and Edge document registries are composed into one registry surface
- `RegistryOnlyGraphProcessor` blocks runtime asset discovery fallback
- `AniTrendConverterFactory` routes by request and response type, while preserving explicit JSON and XML converter overrides

## `data/android` as a transitive dependency

Individual `data:*` modules add `data:android` to their `build.gradle.kts` via the shared
`applyDataDependencies()` helper in `ProjectDependencies.kt`. They do not re-declare Retrofit,
OkHttp, or the `retrofit-graphql` converter themselves. Those come through `data:android`.

## Related skills

- `graphql-query-pattern/SKILL.md` for generated request and query-lifecycle details
- `data-state-pattern/SKILL.md` for how `DataState` and `RequestCallback` interact
- `koin-module-wiring/SKILL.md` for how module `KoinModule.kt` files call `graphQLController`
