# data/src/main/kotlin/co/anitrend/data/favourite/

## Responsibility

Implements favourite toggle mutations for anime, manga, character, staff, and studio targets.

## Design Patterns

Uses datasource, mapper, model, repository, source, usecase, and Koin packages without Room entity persistence in this package.

## Data & Control Flow

Data enters this package through domain params, generated GraphQL models, or embedded parent mappers depending on the package role. Repository packages delegate to source contracts when present. Source packages coordinate remote sources, local sources, cache policies, controllers, mappers, and converters before returning domain-facing data.

## Integration Points

- Uses shared infrastructure from `data/core/` when network, cache, controller, mapper, Room, or paging support is needed.
- Uses generated AniList GraphQL classes from `data/src/main/graphql/` where remote calls are present.
- Publishes bindings through its `koin/` package when one exists.
- Is consumed by domain interactors or by neighboring data packages when it is an embedded support component.
