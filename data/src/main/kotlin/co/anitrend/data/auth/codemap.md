# data/src/main/kotlin/co/anitrend/data/auth/

## Responsibility

Implements authentication data, token settings, auth helper logic, and auth repository wiring.

## Design Patterns

Combines data source, entity, helper, mapper, model, repository, settings, source, usecase, and Koin packages.

## Data & Control Flow

Data enters this package through domain params, generated GraphQL models, or embedded parent mappers depending on the package role. Repository packages delegate to source contracts when present. Source packages coordinate remote sources, local sources, cache policies, controllers, mappers, and converters before returning domain-facing data.

## Integration Points

- Uses shared infrastructure from `data/core/` when network, cache, controller, mapper, Room, or paging support is needed.
- Uses generated AniList GraphQL classes from `data/src/main/graphql/` where remote calls are present.
- Publishes bindings through its `koin/` package when one exists.
- Is consumed by domain interactors or by neighboring data packages when it is an embedded support component.
