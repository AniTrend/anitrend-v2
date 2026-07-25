# android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/

## Responsibility

This package is the main drawer implementation. It owns drawer actions, adapters, controller models, content fragments, presenters, view models, router provider, initializer, and Koin setup.

## Design Patterns

- Host fragment pattern switches between legacy and Compose drawer implementations behind `INavigationDrawer`.
- Adapter and controller model packages support legacy Groupie based rendering.
- Internal models and mappers represent drawer sections, selected state, resources, and config.
- Action contracts propagate bottom sheet slide and state changes to app shell UI chrome.

## Data & Control Flow

Koin creates drawer view models and fragments. The host fragment receives commands from `MainScreen`, delegates to legacy or Compose drawer content, maps drawer events to `Navigation.Menu`, and exposes them as a Flow.

## Integration Points

- Depends on `app/navigation` for `NavigationDrawerRouter` and drawer destination contracts.
- Depends on `android/core` for sheet actions, resources, and UI helpers.
- Consumes account and notification interactors plus settings contracts from data and domain layers through injected abstractions.
