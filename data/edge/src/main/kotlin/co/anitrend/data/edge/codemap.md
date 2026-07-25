# data/edge/src/main/kotlin/co/anitrend/data/edge/

## Responsibility

This package contains AniTrend Edge data implementations for config, core store support, episode, genre, image, media, navigation, network, news, season, theme, and trailer data.

## Design Patterns

- Each Edge subdomain uses model, mapper, entity, converter, datasource, and Koin packages according to its persistence needs.
- Repository and usecase packages are present for Edge config and Edge news flows.
- Shared `core/store` contracts group Edge Room store surfaces.

## Data & Control Flow

Remote Edge GraphQL responses are mapped into Edge entities. Local sources expose rows to Edge repositories or to main AniList data packages that need enrichment. Paged news uses a paging source and repository path, while media and config use source or repository flows specific to their use cases.

## Integration Points

- Generated Edge GraphQL classes come from `data/edge/src/main/graphql/`.
- Main media flows import Edge media source and local source classes for enrichment.
- Koin modules in subpackages wire Edge stores, mappers, converters, sources, and repositories.
