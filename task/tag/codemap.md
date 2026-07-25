# task/tag/

## Responsibility

Refreshes media tag metadata in the background.

## Design Patterns

`TagWorker` calls `TagInteractor`. `TagScheduler` enqueues periodic work. Provider and initializers expose scheduler and worker wiring.

## Data & Control Flow

Startup schedules tag refresh. WorkManager runs the worker, which calls the tag interactor and completes from the resulting state.

## Integration Points

Consumes `domain/tag` params and tag data. Exposed by `TagTaskRouter`.
