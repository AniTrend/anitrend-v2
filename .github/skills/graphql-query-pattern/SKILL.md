---
name: graphql-query-pattern
description: >
  GraphQL controller and query lifecycle guide. Use when adding or refactoring AniList GraphQL
  requests, remote source bindings, mappers, and error propagation in the data layer. Covers
  @GraphQuery annotation, @GRAPHQL tag, QueryContainerBuilder, Response<GraphQLResponse<*>>,
  IGraphPayload.toMap(), and the full request-to-Room pipeline.
---

# Skill: GraphQL Query / Controller Pattern

## Overview

All AniList API calls use a custom Retrofit + GraphQL converter. Network calls follow a controller
pattern that wraps the raw `GraphQLResponse` into a standardised result handled by the data source.

## Key files to read

- `data/android/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`
  — the core controller: validates the response, extracts errors, maps result, handles threading
- `data/src/main/kotlin/co/anitrend/data/tag/source/` — example data source showing how a
  controller is constructed and called
- `data/src/main/kotlin/co/anitrend/data/medialist/` and
  `data/src/main/kotlin/co/anitrend/data/review/` — reference mutation modules for save/delete/
  rate flows, including source contracts, repository wiring, and concrete use-case bridges
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt` —
  where Retrofit, OkHttp, and the GraphQL converter are wired as shared data-module dependencies

## Request lifecycle

```
Retrofit interface method (suspend fun)
    → GraphQLController.invoke()
        → validates GraphQLResponse (non-null, no errors)
        → calls mapper.onResponseMapFrom()
        → mapper.persist() writes to Room
        → emits domain model to the DataState flow
```

- Errors are encapsulated as `RequestError` and emitted through the `DataState` error channel —
  never thrown as raw exceptions to the ViewModel.
- Threading is managed inside the controller/mapper; do not add extra `withContext` unless a
  custom controller is used.

## Adding a new GraphQL query

1. Define a Retrofit `interface` method annotated with `@GraphQuery("OperationName")` (from the
   `retrofit-graphql` library). Place it next to the existing API interfaces in the relevant data
   module.
2. Write the `.graphql` query file in the `assets/graphql/` directory of the same module.
3. Create or reuse a `GraphQLController` instance, passing the mapper and dispatcher.
4. In the data source `invoke()` / `getX()` method, call the Retrofit method then feed the result
   to the controller.

## Remote source binding — annotation and type contract

Every remote source method uses three cooperating pieces:

### `@GRAPHQL`
A Retrofit tag annotation defined in `data/android`. It signals the `retrofit-graphql` converter
to serialize the `QueryContainerBuilder` body as a GraphQL envelope
`{ operationName, query, variables }` rather than as plain JSON.

### `@GraphQuery("OperationName")`
Resolves and loads the matching `.graphql` asset file at runtime by operation name. The annotation
processor injects `operationName` and the query string into the `QueryContainerBuilder` so the
correct operation is sent to the server.

### `@Body queryContainer: QueryContainerBuilder`
`QueryContainerBuilder` carries the assembled `{ operationName, query, variables: Map }` envelope.
The data source creates the variables map by calling `IGraphPayload.toMap()` on the query object
(see Rule 6 in `mapping-graphql-models/SKILL.md`) and passes it to the builder before handing it
to Retrofit. **All three annotations must stay in sync with each other and with the variables
declared in the `.graphql` file.**

### `Response<GraphQLResponse<*>>`
- `Response<T>` is Retrofit's envelope — it exposes HTTP status code and headers while keeping the
  deserialized body separate.
- `GraphQLResponse<T>` is the standard GraphQL envelope `{ data: T?, errors: List<Error>? }` from
  `retrofit-graphql`.
- Both wrappers are stripped inside `GraphQLController` before the mapped domain type ever reaches
  the repository or ViewModel. Data sources should never unwrap them manually.

A complete method signature looks like:
```kotlin
@GRAPHQL
@GraphQuery("GetMediaDetail")
@POST(IEndpointType.BASE_ENDPOINT_PATH)
suspend fun getMediaDetail(
    @Body queryContainer: QueryContainerBuilder,
): Response<GraphQLResponse<MediaModelContainer.Detail>>
```

## Mutation flow rules

- For GraphQL mutation-only features, still define the repository contract and abstract use case in
  `:domain`; the data module should implement and wire them rather than inventing local contracts.
- Keep the module `Types.kt` limited to aliases. Put concrete use-case subclasses in the module
  `usecase/` package.
- When wiring `graphQLController(...)` in Koin, prefer `get<ConcreteMapper>()` over bare `get()`
  so generic mapper/controller resolution stays explicit and stable.

## Edge modeling rules

For `:data:edge` remote models, keep the serialized shape faithful to the upstream schema:

- **Converters** translate schema-shaped remote models into local entities.
- **Mappers** coordinate parsing, persistence, and cross-entity normalization.
- **Entities and entity views** represent the persisted local shape.
- Do **not** embed compatibility hacks or inferred IDs directly in the serialized model.
