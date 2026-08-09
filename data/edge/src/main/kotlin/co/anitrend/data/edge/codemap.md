# data/edge/src/main/kotlin/co/anitrend/data/edge/

## Responsibility

This package contains AniTrend Edge data implementations for config, core store support, episode, genre, image, media, navigation, network, news, theme, trailer, and retained Room season infrastructure.

## Design Patterns

- Each Edge family uses converter, datasource, entity, mapper, and Koin packages according to its persistence needs.
- Repository and usecase packages are present for Edge config and Edge news flows.
- Shared `core/store` contracts group Edge Room store surfaces.
- Generated GraphQL response DTOs are mapped into stable Edge entities by family converters; do not hand-write mirror models for generated response roots.

## Data & Control Flow

Remote Edge GraphQL responses arrive as generated response DTO roots: `GetConfigData` (config), `NewsConnectionData` (news), `GetMediaByIdData` (series media enrichment), and `EpisodesData` (episode). Converters translate these generated DTOs into stable Edge entities, mappers coordinate persistence, and local sources expose rows to Edge repositories or to main AniList data packages that need enrichment. Paged news uses a paging source and repository path, while media and config use source or repository flows specific to their use cases.

## Retained Room Season Infrastructure

The season transport subdomain (remote source, converter, mapper, model, and Koin module) was removed. What remains is Room-only infrastructure: `season/entity/EdgeSeasonEntity.kt`, `season/datasource/EdgeSeasonLocalSource.kt`, and `season/datasource/IEdgeSeasonStore.kt`, consumed through the shared `core/store/IEdgeStore.kt` contract by media and episode flows.

## Integration Points

- Generated Edge GraphQL classes come from `data/edge/src/main/graphql/`.
- Main media flows import Edge media source and local source classes for enrichment.
- Koin modules in subpackages wire Edge stores, mappers, converters, sources, and repositories.
