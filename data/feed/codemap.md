# data/feed/

## Responsibility

`data/feed/` implements non-AniList feed integration for news and episode content, including feed API factory support, RSS-like models, remote and local sources, Room entities, cache policies, mappers, converters, repositories, sources, use cases, and Koin wiring.

## Design Patterns

- Feed API factory abstracts endpoint creation for feed remote sources.
- Episode and news packages mirror the data-layer pattern with models, local and remote datasources, entities, mappers, converters, cache, and repositories where present.
- Feed-specific extensions handle date and scope concerns close to the integration.

## Data & Control Flow

Feed repositories and sources request paged news or episode data from remote feed endpoints, map responses into local entities, cache request history, and expose converted domain entities through `DataState` or paging flows.

## Integration Points

- Uses shared infrastructure from `data/core/` for cache, controller, mapper, source, and paging patterns.
- Exposes DI through `data/feed/src/main/kotlin/co/anitrend/data/feed/koin/Modules.kt`.
- Supports feature and task layers through domain feed, news, or episode contracts.
