# data/edge/

## Responsibility

`data/edge/` implements AniTrend Edge GraphQL integration and local persistence for Edge-provided config, media enrichment, news, episodes, images, themes, trailers, seasons, navigation, network, and genre data.

## Design Patterns

- Schema-faithful remote models are kept under `model/remote` where remote payloads exist.
- Mappers persist Edge models into local entities and related embedded tables.
- Local sources expose Room-backed rows and views that main AniList data packages can join or enrich from.
- Edge GraphQL fragments and queries are separated from AniList GraphQL assets under `data/edge/src/main/graphql/`.

## Data & Control Flow

Edge sources build generated Edge GraphQL requests, call Edge remote sources, map responses into Room entities, and expose data through repository contracts or local sources. Main media flows can refresh Edge media side data and combine Edge rows with AniList results.

## Integration Points

- `data/edge/src/main/kotlin/co/anitrend/data/edge/` contains Kotlin data implementation packages.
- `data/edge/src/main/graphql/` contains Edge fragments and queries.
- `data/src/main/kotlin/co/anitrend/data/media/` imports Edge media source and local source types for media detail and studio network enrichment.
