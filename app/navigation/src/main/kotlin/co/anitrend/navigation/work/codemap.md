# app/navigation/src/main/kotlin/co/anitrend/navigation/work/

## Responsibility

This package defines scheduling contracts for background work routed through `app/navigation`.

## Design Patterns

- `WorkSchedulerController` abstracts WorkManager scheduling details from route callers.
- Task routers expose worker classes and scheduler instances through provider interfaces.

## Data & Control Flow

Feature code or startup code asks a task router for a worker or scheduler, builds input from route params, and delegates scheduling to the task module implementation.

## Integration Points

- Consumed by task routes in `NavigationTargets.kt`.
- Implemented by task modules such as media list, news, episode, config, genre, tag, and user sync.
