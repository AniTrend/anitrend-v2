# data/edge/src/main/graphql/

## Responsibility

This directory contains AniTrend Edge GraphQL assets for generated Edge request and response types.

## Design Patterns

- Shared media enrichment selections live in `fragments/`.
- Query folders group Edge reads for config, episode, news, and series data.
- Edge assets are separate from AniList assets so generated namespaces and schemas remain distinct.

## Data & Control Flow

The GraphQL generator emits Edge operation classes. Edge remote sources use those generated classes to build request bodies, then mappers persist response data into Edge Room tables.

## Integration Points

- Used by Kotlin packages under `data/edge/src/main/kotlin/co/anitrend/data/edge/`.
- Series media queries and fragments support Edge media enrichment consumed by main media data flows.
