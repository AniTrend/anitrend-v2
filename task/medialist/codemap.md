# task/medialist/

## Responsibility

Runs media list sync and media list mutation work for anime, manga, entries, entry batches, and custom lists.

## Design Patterns

Sync workers handle anime and manga collection refresh. Mutation workers handle save entry, save entries, delete entry, and delete custom list. Schedulers use `ISyncSettings` for repeat intervals and connected-network, battery-not-low constraints. Provider exposes all worker classes and scheduler instances.

## Data & Control Flow

Feature flows enqueue mutation workers with `MediaListTaskRouter` params, which workers transform into `MediaListParam` variants. Startup schedules anime and manga sync when authenticated. Workers call data media list interactors and return WorkManager results from terminal load state.

## Integration Points

Consumes `domain/medialist` params and data media list interactors. Integrated with media list editor flows and authenticated startup sync.
