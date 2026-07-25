# domain/src/main/kotlin/co/anitrend/domain/medialist/

## Responsibility

Defines user media list reads, collection sync, list entry lookup, paged lists, save mutations, batch saves, custom list deletion, and entry deletion.

## Design Patterns

`IMediaListRepository` is split into sync, entry, paged, collection, save entries, save entry, delete custom list, and delete entry contracts. `MediaListParam` is a sealed request model for collection, entry, paged, save, and delete variants. Entities separate base list contracts from concrete list entry state.

## Data & Control Flow

UI or task code creates a `MediaListParam` variant. The use case forwards to the exact repository slice, allowing background workers and screens to share the same domain operation without sharing data implementation details.

## Integration Points

Implemented by media list data and consumed heavily by `task/medialist/`, media list screens, editor flows, and sync startup paths.
