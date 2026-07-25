# android/navigation/src/main/kotlin/co/anitrend/android/navigation/

## Responsibility

This package contains the Android navigation drawer source code, including Compose drawer UI and legacy drawer implementation.

## Design Patterns

- `drawer/` contains router provider, feature initializer, Koin modules, View based drawer content, adapters, models, actions, and view models.
- `compose/drawer/` contains the Compose drawer sheet and screen components.
- Internal drawer mappers convert settings and user state into UI models.
- Legacy adapter bridges newer drawer models to the older `Navigation.Menu` contract used by the app shell.

## Data & Control Flow

Drawer view models load account and navigation state. Drawer content renders items, selected item changes become `DrawerEvent.Navigate`, and `NavigationDrawerHostFragment` maps events into legacy navigation menu selections for `MainScreen`.

## Integration Points

- Koin module helper is loaded by the module initializer.
- `NavigationDrawerRouter.Provider` returns `NavigationDrawerHostFragment` to the app shell.
- Compose UI uses `android/core` Material3 theme and design helpers.
