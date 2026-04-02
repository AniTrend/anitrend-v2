---
name: graphql-query-pattern
description: 'GraphQL controller and query lifecycle guide. Use when adding or refactoring AniList GraphQL requests, mappers, and error propagation in the data layer.'
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

## Edge modeling rules

For `:data:edge` remote models, keep the serialized shape faithful to the upstream schema:

- **Converters** translate schema-shaped remote models into local entities.
- **Mappers** coordinate parsing, persistence, and cross-entity normalization.
- **Entities and entity views** represent the persisted local shape.
- Do **not** embed compatibility hacks or inferred IDs directly in the serialized model.
