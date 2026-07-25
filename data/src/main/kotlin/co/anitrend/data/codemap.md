# data/src/main/kotlin/co/anitrend/data/

## Responsibility

This package contains the main AniList data implementations and shared support packages. It includes repositories, sources, local Room stores, remote Retrofit sources, GraphQL response models, mappers, converters, cache policies, Koin modules, and data interactor aliases for AniList-backed domain contracts.

## Design Patterns

- Full repository packages follow the recurring `Types.kt`, `repository/`, `source/`, `datasource/`, `mapper/`, `converter/`, `entity/`, `model/`, `cache/`, `usecase/`, and `koin/` shape.
- Smaller shared packages provide entities, models, converters, or local stores that are embedded by larger flows.
- Source contracts split operations by read or write capability, such as detail, paged, save, delete, sync, rate, follow, and toggle.
- Room entity views are converted into domain entities after controller-driven persistence.

## Data & Control Flow

Domain params enter data interactors and repository implementations. Repositories call source contracts. Sources coordinate cache identities, local Room observables, remote GraphQL calls, controller execution, and mapper persistence. Converters return domain entities to repository streams.

## Integration Points

- Uses generated AniList GraphQL classes from `data/src/main/graphql/`.
- Uses shared infrastructure from `data/core/`.
- Uses Edge enrichment from `data/edge/` where media detail and studio network data need AniTrend Edge rows.
- Exposes implementations to app wiring through `koin/Modules.kt` files in each package.
