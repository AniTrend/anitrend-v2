# android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/koin/

## Responsibility

This package wires the drawer feature dependencies.

## Design Patterns

- Presenter, view model, fragment, and feature provider modules are grouped for dynamic loading.
- Drawer fragments are scoped to `AppScope.BOTTOM_NAV_DRAWER`.
- View models receive mappers and interactors through constructor injection.
- Provider binding exposes `NavigationDrawerHostFragment` as the drawer fragment target.

## Data & Control Flow

The drawer feature initializer loads `moduleHelper`. Koin then creates drawer scoped fragments, presenters, adapters, view models, notification provider view model, and `NavigationDrawerRouter.Provider`.

## Integration Points

- Consumes app core `AppScope`.
- Implements `NavigationDrawerRouter.Provider` from `app/navigation`.
- Uses account, config, notification, auth settings, and state layout dependencies from the global Koin graph.
