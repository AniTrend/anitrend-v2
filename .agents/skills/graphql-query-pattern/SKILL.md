---
name: graphql-query-pattern
description: >
  GraphQL controller and request lifecycle guide. Use when adding or refactoring AniList or Edge
  GraphQL requests, generated operation wiring, mappers, and error propagation in the data layer.
  Covers generated `GraphQLOperationRequest<...Variables>` usage with carried documents, explicit
  `KotlinxGraphQLTransportCodec` and `GraphQLConverterFactory` wiring, the shared
  `AniTrendConverterFactory` mixed-protocol boundary, `Response<GraphQLResponse<*>>`, and the full
  request-to-Room pipeline.
---

# Skill: GraphQL Query / Controller Pattern

## Overview

AniTrend GraphQL calls use generated operation documents under `src/main/graphql/**`. Each request
is a neutral `GraphQLOperationRequest<...Variables>` that carries its own operation document and
name, so the generated document registry is not consulted for request conversion. Network calls
follow the same controller pattern: Retrofit returns `Response<GraphQLResponse<*>>`,
`GraphQLController` validates and maps the payload, and mapper persistence keeps Room as the local
source of truth.

## Key files to read

- `data/core/src/main/kotlin/co/anitrend/data/android/controller/graphql/GraphQLController.kt`
  for the response pipeline and error handling
- `data/src/main/kotlin/co/anitrend/data/android/koin/Modules.kt`
  for `GraphQLConverterFactory` and `KotlinxGraphQLTransportCodec` wiring
- `data/src/main/kotlin/co/anitrend/data/core/api/converter/AniTrendConverterFactory.kt`
  for the mixed-protocol request and response converter boundary
- `data/src/main/kotlin/co/anitrend/data/core/api/converter/CompositeGraphQLDocumentRegistry.kt`
  for how the generated AniList and Edge registries are combined
- `data/src/main/graphql/**` and `data/edge/src/main/graphql/**`
  for the generated AniList and Edge operation documents
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt`
  where shared data-module GraphQL dependencies are wired

## Request lifecycle

```
Generated operation document + typed variables
    → GraphQLOperationRequest<...Variables> (carries document and operation name)
        → AniTrendConverterFactory
            → GraphQLConverterFactory (KotlinxGraphQLTransportCodec)
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
3. Let the codegen task generate the operation object, its document, and the typed variables class.
4. Define or update the Retrofit `interface` method in the existing remote source package, using a
   generated `GraphQLOperationRequest<...Variables>` body.
5. Inject a `GraphQLController` per source class and feed the Retrofit response into it.

## Controller choice

- Use `graphQLController(...)` when the endpoint returns `Response<GraphQLResponse<*>>`.
- Use `defaultController(...)` for plain REST responses that are not wrapped in a GraphQL envelope.

## Remote source binding and type contract

### Generated `GraphQLOperationRequest<...Variables>`

A generated operation object exposes the document and operation name (for example
`GetMediaGenres.document` and `GetMediaGenres.name`), and the generated typed variables class covers
the operation arguments. Sources build a neutral `GraphQLOperationRequest` from these generated
pieces. The request carries its document, so the registry is not consulted for request conversion.
This is the preferred production request shape for both AniList and Edge GraphQL calls.

### Codec and converter wiring

`GraphQLConverterFactory.create(...)` is wired explicitly in Koin with a
`KotlinxGraphQLTransportCodec` that reuses the shared `Json` configuration and opts into null
omission for GraphQL request encoding (`explicitNulls = false`). A `CompositeGraphQLDocumentRegistry`
combines the generated AniList and Edge registries for the factory.

### Shared `AniTrendConverterFactory` mixed-protocol boundary

`AniTrendConverterFactory` routes converters by annotation and type:

- `XML` and `JSON` annotated methods go to the XML and JSON factories.
- `GraphQLOperationRequest` bodies and `GraphQLResponse` payloads go to the GraphQL factory.
- Everything else falls back to Gson.

### `Response<GraphQLResponse<*>>`

- `Response<T>` is Retrofit's HTTP envelope.
- `GraphQLResponse<T>` is the GraphQL data and errors envelope.
- Both wrappers are stripped inside `GraphQLController` before mapped domain types reach the
  repository or presentation layers.

A typical migrated method signature looks like:
```kotlin
@POST(IEndpointType.BASE_ENDPOINT_PATH)
suspend fun getMediaDetail(
    @Body request: GraphQLOperationRequest<GetMediaDetailVariables>,
): Response<GraphQLResponse<MediaModelContainer.Detail>>
```

## Mutation flow rules

- For mutation-only features, still define repository contracts and abstract use cases in `:domain`.
- Keep the module `Types.kt` limited to aliases. Put concrete use-case subclasses in `usecase/`.
- When wiring `graphQLController(...)` or `defaultController(...)` in Koin, prefer
  `get<ConcreteMapper>()` over bare `get()` for mapper arguments.

## Edge modeling rules

For `:data:edge` remote models, keep the generated shape faithful to the upstream schema:

- Edge response roots are generated operation DTOs: `GetConfigData` (config),
  `NewsConnectionData` (news), `GetMediaByIdData` (series media enrichment), and
  `EpisodesData` (episode).
- **Converters** translate these generated DTOs into stable local entities.
- **Mappers** coordinate parsing, persistence, and cross-entity normalization.
- **Entities and entity views** represent the persisted local shape.
- Do **not** embed compatibility hacks or inferred IDs directly in the serialized model, and do not
  hand-write mirror models for generated response roots.
