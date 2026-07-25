# task/episode/

## Responsibility

Synchronizes episode data in the background.

## Design Patterns

`EpisodeWorker` calls `EpisodeSyncInteractor`. `EpisodeScheduler` handles periodic scheduling. Provider and initializers wire router access and module loading.

## Data & Control Flow

Startup schedules episode sync through `EpisodeTaskRouter`. WorkManager runs `EpisodeWorker`, which invokes episode sync data and maps completion state to a WorkManager result.

## Integration Points

Consumes `domain/episode` params and episode data interactors. Used by app startup sync behavior.
