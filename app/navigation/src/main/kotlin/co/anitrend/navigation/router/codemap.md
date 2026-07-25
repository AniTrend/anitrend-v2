# app/navigation/src/main/kotlin/co/anitrend/navigation/router/

## Responsibility

This package contains the base router abstraction for all app navigation targets.

## Design Patterns

- `NavigationRouter` is a Koin component so route objects can inject providers lazily.
- The base class stores a module tag derived from the route class name.
- Provider contracts are kept in each route object to avoid a broad shared interface.

## Data & Control Flow

A route object extends `NavigationRouter`, injects its provider, and exposes helper functions that call provider methods. Consumers never instantiate feature screens directly.

## Integration Points

- Used by every route object in `app/navigation/src/main/kotlin/co/anitrend/navigation/NavigationTargets.kt`.
- Depends on `INavigationProvider` from `provider/`.
- Provider bindings are installed by feature, app, task, and Android modules.
