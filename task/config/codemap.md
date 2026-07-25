# task/config/

## Responsibility

Refreshes application configuration through a scheduled WorkManager task.

## Design Patterns

`ConfigWorker` calls `GetConfigInteractor`. `ConfigScheduler` enqueues unique periodic work with connected-network constraints. Startup uses both feature and work scheduler initializers.

## Data & Control Flow

Startup loads task modules, then schedules config work. WorkManager creates `ConfigWorker`, which invokes config data and completes from the resulting state.

## Integration Points

Consumes `domain/config` and config data interactors. Exposed by `ConfigTaskRouter` and scheduled at startup.
