# data/feed/src/main/kotlin/co/anitrend/data/feed/

## Responsibility

This package contains feed API support plus news and episode data implementations for feed-backed content.

## Design Patterns

- `api/` and `api/factory/` isolate endpoint construction.
- `contract/` contains shared RSS container and channel model contracts.
- `episode/` and `news/` follow repository, source, datasource, entity, model, mapper, converter, cache, and Koin patterns according to each flow.

## Data & Control Flow

Remote feed models are fetched through feed remote sources, mapped into Room entities, and converted into domain entities. Paging sources and cache identities coordinate page refreshes where the flow is paged.

## Integration Points

- Shares infrastructure with `data/core/`.
- Publishes feed bindings through `koin/`.
- Serves news and episode feature or task consumers through domain contracts.
