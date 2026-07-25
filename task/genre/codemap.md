# task/genre/

## Responsibility

Refreshes media genre metadata in the background.

## Design Patterns

`GenreWorker` calls `GenreInteractor`. `GenreScheduler` enqueues periodic work. Provider and initializers wire the scheduler and worker.

## Data & Control Flow

Startup schedules genre refresh. WorkManager runs the worker, which calls the genre interactor and completes from the resulting state.

## Integration Points

Consumes `domain/genre` params and genre data. Exposed by `GenreTaskRouter`.
