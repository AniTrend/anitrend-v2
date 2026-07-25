# domain/src/main/kotlin/co/anitrend/domain/genre/

## Responsibility

Defines media genre lookup contracts and genre entities.

## Design Patterns

`GenreUseCase` wraps `IGenreRepository`. `GenreParam` controls genre retrieval and `Genre` represents a media genre value.

## Data & Control Flow

Callers request media genres through the use case. Data returns genre state through the repository contract.

## Integration Points

Implemented by genre data and refreshed by `task/genre/`.
