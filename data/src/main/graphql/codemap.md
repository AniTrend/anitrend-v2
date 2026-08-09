# data/src/main/graphql/

## Responsibility

This directory contains AniList GraphQL operation assets used to generate Kotlin operation objects, typed variables, and enum types for the main AniList data package.

## Design Patterns

- Fragment-first composition keeps shared model fields in `fragments/` and composes them into `queries/` and `mutations/`.
- Domain-grouped folders mirror data packages such as media, medialist, review, user, staff, studio, feed, forum, notification, and recommendation.
- Connection folders capture nested AniList connection shapes for media, character, staff, and studio relationships.

## Data & Control Flow

GraphQL files are consumed by code generation. Data sources then import generated operation objects (document and name) and typed variables, build `GraphQLOperationRequest<...Variables>` bodies, and submit them through Retrofit remote source interfaces. The neutral request carries its document through the shared `AniTrendConverterFactory` / `GraphQLConverterFactory` boundary, and `GraphQLController` validates responses before mappers persist to Room. AniList response payloads are declared against hand-written container models such as `MediaModelContainer.Detail`.

## Integration Points

- `fragments/` defines reusable response selections.
- `queries/` defines read operations used by repository source implementations.
- `mutations/` defines write operations used by task-backed or direct mutation sources.
- Generated classes are imported from `co.anitrend.data.graphql.anilist` in Kotlin data sources.
