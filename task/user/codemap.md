# task/user/

## Responsibility

Runs user account sync, follow toggle, and user statistic sync tasks.

## Design Patterns

`UserAccountSyncWorker` calls `GetAuthenticatedInteractor`. `UserFollowToggleWorker` calls `ToggleFollowInteractor`. `UserStatisticSyncWorker` exists but currently contains a `TODO` body. `UserAccountScheduler` and `UserStatisticScheduler` schedule periodic account and statistic work.

## Data & Control Flow

Startup schedules account and statistic sync through `UserTaskRouter`. Follow actions can enqueue toggle work. Implemented workers call data interactors and map completion state to WorkManager results.

## Integration Points

Consumes `domain/user` params plus auth profile data. Exposed by `UserTaskRouter` and used by profile, account, and social flows.
