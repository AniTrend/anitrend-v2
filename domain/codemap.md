# domain/

## Responsibility

`domain/` is the pure Kotlin contract layer for AniTrend features. It defines repository interfaces, use case wrappers, request parameter models, domain entities, shared contracts, and sort or enum types for app concepts such as media, media lists, reviews, users, staff, studios, news, and configuration.

## Design Patterns

- Repository contracts expose domain operations and are generic over `UiState<*>` when a stream or mutation has UI state semantics.
- Use cases in `interactor/` wrap repository contracts and keep callers decoupled from concrete data implementations.
- `model/` packages contain params, inputs, and sealed request shapes that travel from UI or task modules into repositories.
- `entity/`, `entity/contract/`, and `common/` packages hold app-facing domain objects and reusable interfaces such as identity, page info, names, cover images, favourites, and sort ordering.
- Large domains split contracts by operation, for example detail, paged, save, delete, sync, rate, network, and collection.

## Data & Control Flow

Feature, common, or task code calls a data-provided interactor alias, which delegates to a domain use case, then to a domain repository contract. Data modules implement the contract and return `DataState` or paging types through the generic domain return type. Params from `model/` carry identifiers, filters, sort selections, pagination state, and mutation payloads.

## Integration Points

- Implemented by matching `data:<entity>` modules, for example `data/src/main/kotlin/co/anitrend/data/media/` and `data/src/main/kotlin/co/anitrend/data/medialist/`.
- Consumed by `feature:*`, `common:*`, and `task:*` modules through interactor aliases.
- Shared contracts under `domain/src/main/kotlin/co/anitrend/domain/common/` keep feature and data models aligned without Android framework dependencies.
- Closest reference packages are `domain/src/main/kotlin/co/anitrend/domain/tag/`, `domain/src/main/kotlin/co/anitrend/domain/media/`, `domain/src/main/kotlin/co/anitrend/domain/medialist/`, `domain/src/main/kotlin/co/anitrend/domain/review/`, and `domain/src/main/kotlin/co/anitrend/domain/favourite/`.
