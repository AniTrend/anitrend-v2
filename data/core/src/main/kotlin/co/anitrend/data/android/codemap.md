# data/core/src/main/kotlin/co/anitrend/data/android/

## Responsibility

This package is the shared Android data infrastructure layer. It supplies reusable cache, controller, database, mapper, network, paging, logger, source, filter, and device info support for concrete data modules.

## Design Patterns

- Base classes and contracts keep data sources consistent across Room, Retrofit, and paging flows.
- Strategy and controller classes centralize request execution and state handling.
- Cache helpers separate request identity from source implementation details.
- Network package separates cookies, clients, interceptors, default adapters, GraphQL adapters, and response models.

## Data & Control Flow

Concrete data modules inject these helpers through Koin. Sources use the cache and controller helpers before remote calls, mappers persist results through local sources, and converters return domain-facing models from stored entity views.

## Integration Points

- `controller/` is referenced by data package `Types.kt` aliases such as media and medialist controller aliases.
- `network/` supports Retrofit and GraphQL remote sources.
- `cache/` stores and invalidates request history in Room.
- `paging/` provides a shared mediator base for paged resources.
