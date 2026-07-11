---
name: graphql-query-pattern
description: >
  GraphQL controller and request lifecycle guide. Use when adding or refactoring AniList or Edge
  GraphQL requests, generated operation wiring, mappers, and error propagation in the data layer.
  Covers generated `GraphQLRequest<...Variables>` usage, registry-backed document resolution,
  `QueryContainerBuilder` compatibility boundaries, `Response<GraphQLResponse<*>>`, and the full
  request-to-Room pipeline.
---

# Skill: GraphQL Query / Controller Pattern

## Overview

AniTrend GraphQL calls now use generated operation documents under `src/main/graphql/**` and
registry-backed request resolution through the `retrofit-graphql` codegen plugin. Network calls
still follow the same controller pattern: Retrofit returns `Response<GraphQLResponse<*>>`,
`GraphQLController` validates and maps the payload, and mapper persistence keeps Room as the local
source of truth.

## Key files to read

- `data/core/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`
  for the response pipeline and error handling
- `data/src/main/kotlin/co/anitrend/data/android/koin/Modules.kt`
  for registry-first GraphQL converter wiring
- `data/src/main/kotlin/co/anitrend/data/core/api/converter/AniTrendConverterFactory.kt`
  for request and response converter routing
- `data/src/main/kotlin/co/anitrend/data/core/api/converter/request/AniGraphRequestConverter.kt`
  for registry-backed document resolution and release minification
- `data/src/main/graphql/**` and `data/edge/src/main/graphql/**`
  for the generated AniList and Edge operation documents
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
  where shared data-module GraphQL dependencies are wired

## Request lifecycle

```
Generated operation document + variables
    → GraphQLRequest<...Variables>
        → AniTrendConverterFactory / AniGraphRequestConverter
            → registry-backed document resolution
            → GraphQLController.invoke()
                → validates GraphQLResponse (non-null, no errors)
                → calls mapper.onResponseMapFrom()
                → mapper.persist() writes to Room
                → emits domain model to the DataState flow
```

- Errors are encapsulated as `RequestError` and emitted through the `DataState` error channel.
- Threading is managed inside the controller and mapper pipeline. Do not add extra `withContext`
  unless a custom controller is required.

## Adding a new GraphQL operation

1. Write the `.graphql` operation under the generated source tree for the owning module:
   - AniList: `data/src/main/graphql/**`
   - Edge: `data/edge/src/main/graphql/**`
2. Keep fragment composition under the matching `fragments/**` tree and prefer fragment reuse over
   duplicated inline field sets.
3. Let the codegen task generate the operation object and typed variables class.
4. Define or update the Retrofit `interface` method in the existing remote source package, using a
   generated `GraphQLRequest<...Variables>` body.
5. Inject a `GraphQLController` per source class and feed the Retrofit response into it.

## Controller choice

- Use `graphQLController(...)` when the endpoint returns `Response<GraphQLResponse<*>>`.
- Use `defaultController(...)` for plain REST responses that are not wrapped in a GraphQL envelope.

## Remote source binding and type contract

### Generated `GraphQLRequest<...Variables>`
A generated request object carries the operation name, document identity, and typed variables.
This is the preferred production request shape for both AniList and Edge GraphQL calls.

### Registry-backed document resolution
Generated registries are composed in Koin and passed into the GraphQL converter. Production wiring
is registry-first, and the repo-local `RegistryOnlyGraphProcessor` intentionally blocks runtime
asset discovery fallback.

### `QueryContainerBuilder` compatibility boundary
`QueryContainerBuilder` is still supported by the converter for compatibility utilities, but it is
no longer the preferred production request path for migrated remote sources. New GraphQL work
should start from generated request types unless a documented compatibility gap requires otherwise.

### `Response<GraphQLResponse<*>>`
- `Response<T>` is Retrofit's HTTP envelope.
- `GraphQLResponse<T>` is the GraphQL data and errors envelope.
- Both wrappers are stripped inside `GraphQLController` before mapped domain types reach the
  repository or presentation layers.

A typical migrated method signature looks like:
```kotlin
@POST(IEndpointType.BASE_ENDPOINT_PATH)
suspend fun getMediaDetail(
    @Body request: GraphQLRequest<GetMediaDetailVariables>,
): Response<GraphQLResponse<MediaModelContainer.Detail>>
```

## Mutation flow rules

- For mutation-only features, still define repository contracts and abstract use cases in `:domain`.
- Keep the module `Types.kt` limited to aliases. Put concrete use-case subclasses in `usecase/`.
- When wiring `graphQLController(...)` or `defaultController(...)` in Koin, prefer
  `get<ConcreteMapper>()` over bare `get()` for mapper arguments.

## Edge modeling rules

For `:data:edge` remote models, keep the serialized shape faithful to the upstream schema:

- **Converters** translate schema-shaped remote models into local entities.
- **Mappers** coordinate parsing, persistence, and cross-entity normalization.
- **Entities and entity views** represent the persisted local shape.
- Do **not** embed compatibility hacks or inferred IDs directly in the serialized model.
