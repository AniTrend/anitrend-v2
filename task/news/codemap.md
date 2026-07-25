# task/news/

## Responsibility

Synchronizes news data in the background.

## Design Patterns

`NewsWorker` calls `NewsSyncInteractor`. `NewsScheduler` manages periodic work. Provider and initializers expose router scheduling and load Koin modules.

## Data & Control Flow

Startup schedules news sync. The worker invokes the news sync interactor and maps completion state to a WorkManager result.

## Integration Points

Consumes `domain/news` params and news data. Exposed by `NewsTaskRouter`.
