# android/navigation/src/main/kotlin/co/anitrend/android/navigation/drawer/component/

## Responsibility

This package contains drawer UI components, presenter, and view models for both host and drawer content.

## Design Patterns

- `NavigationDrawerHostFragment` implements `INavigationDrawer` as the stable app shell contract.
- `BottomDrawerContent` hosts the legacy drawer surface.
- `DrawerPresenter` handles presentation helpers and settings access.
- View models map account, config, auth, and notification state into drawer UI state.

## Data & Control Flow

The app shell calls methods on `INavigationDrawer`. The host routes commands to the active drawer implementation. View models produce account and navigation state, adapters render legacy rows, and drawer events flow back to the app shell.

## Integration Points

- Fragment bindings are scoped in `drawer/koin/Modules.kt`.
- Uses `android/core` bottom sheet action contracts.
- Connects to route contracts in `app/navigation` through selected `Navigation.Menu` IDs.
