# android/navigation/

## Responsibility

`android/navigation/` owns the main bottom navigation drawer platform module. It provides drawer UI, drawer state, drawer adapters, Compose drawer support, router provider, and Koin bindings.

## Design Patterns

- Hybrid drawer implementation supports legacy fragment drawer and experimental Compose drawer.
- Host fragment implements `INavigationDrawer` to hide implementation choice from app shell.
- View models map user and config state into account and navigation drawer models.
- Koin scopes drawer fragments to `AppScope.BOTTOM_NAV_DRAWER`.

## Data & Control Flow

`MainScreen` requests `NavigationDrawerRouter.forFragment()` and embeds `NavigationDrawerHostFragment`. The host selects legacy or Compose drawer based on feature flags, emits selected navigation menu events, and receives show, dismiss, checked item, menu visibility, and slide action requests from the app shell.

## Integration Points

- Implements `NavigationDrawerRouter.Provider` from `app/navigation`.
- Consumes `android/core` sheet action contracts and Compose components.
- Reads account, authentication, config, notification, and feature flag state through injected interactors and settings.
