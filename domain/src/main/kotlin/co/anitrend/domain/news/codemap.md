# domain/src/main/kotlin/co/anitrend/domain/news/

## Responsibility

Defines news listing and news sync contracts.

## Design Patterns

`NewsUseCase` has paged and sync variants backed by `INewsRepository`. `NewsParam` carries source and paging request details. `News` is the app-facing article entity.

## Data & Control Flow

Screens request paged news, while task code requests sync. The repository returns paged results or a sync state.

## Integration Points

Implemented by news data and refreshed by `task/news/`.
