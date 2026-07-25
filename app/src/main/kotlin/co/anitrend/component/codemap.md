# app/src/main/kotlin/co/anitrend/component/

## Responsibility

This package contains the main activity presentation layer. It manages the main shell screen, action objects for UI chrome, the presenter used for external links and settings, and the view model state used to survive activity recreation.

## Design Patterns

- Screen, presenter, view model, and action packages mirror feature module presentation conventions.
- `MainScreen` is a lifecycle owner that observes drawer events through Kotlin Flow.
- `FragmentItem` and app navigation routers defer feature creation to registered providers.
- Actions encapsulate view state mutations for FAB visibility and menu visibility.

## Data & Control Flow

Drawer menu selections flow from `INavigationDrawer.navigationFlow` into `MainScreen.onNavigationItemSelected`. The selected menu ID updates `MainScreenViewModel` state, selects a router, builds optional navigation parameters, and commits the resolved fragment into the main content container.

## Integration Points

- Depends on `android/navigation` for drawer contracts and menu models.
- Depends on `app/navigation` for feature routers and parcelable parameters.
- Uses `app/core` screen, UI, and Koin helpers.
- Reads authenticated user settings through `MainPresenter` when opening anime and manga list destinations.
