# task/

## Responsibility

`task/` contains Android WorkManager modules for background sync and mutation work. Source-bearing modules wrap data interactors in `SupportCoroutineWorker` classes, expose worker classes through navigation task routers, load their Koin worker bindings, and schedule periodic work where needed.

## Design Patterns

- `component/` contains `SupportCoroutineWorker` implementations that call data interactor aliases and convert terminal load states to `Result.success()` or `Result.failure()`.
- `provider/FeatureProvider` implements a `*TaskRouter.Provider` from navigation and returns worker classes or scheduler instances.
- `initializer/FeatureInitializer` loads dynamic Koin modules through `DynamicFeatureModuleHelper`.
- `initializer/WorkSchedulerInitializer` starts periodic jobs after Koin setup, often guarded by authentication settings.
- `scheduler/` classes extend `WorkSchedulerController`, configure WorkManager constraints, and enqueue unique periodic work.
- `koin/Modules.kt` registers workers through Koin WorkManager DSL and binds router providers.

## Data & Control Flow

A feature or startup initializer uses a navigation task router to get a worker class or scheduler. WorkManager creates the worker through Koin. The worker transforms router params into domain params when needed, calls a data interactor alias, waits for success or error load state when the interactor emits state, then returns the WorkManager result.

## Integration Points

- Depends on domain params from `domain/src/main/kotlin/co/anitrend/domain/` and data interactor aliases from `data:*` modules.
- Exposes task entry points through navigation routers such as `MediaListTaskRouter`, `ReviewTaskRouter`, `FavouriteTaskRouter`, `UserTaskRouter`, and sync routers for tags, genres, episodes, config, and news.
- Source-bearing modules are `task/account/`, `task/config/`, `task/episode/`, `task/favourite/`, `task/genre/`, `task/medialist/`, `task/news/`, `task/review/`, `task/tag/`, and `task/user/`.
- Shell modules with manifests and build files but no Kotlin task implementation are documented as placeholders in their module codemaps.
