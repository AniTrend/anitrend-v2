# android/navigation/src/main/kotlin/co/anitrend/android/navigation/compose/

## Responsibility

This package contains the Compose implementation of the navigation drawer sheet.

## Design Patterns

- Compose sheet content is isolated under `compose/drawer/` while preserving the existing `INavigationDrawer` contract.
- Screen and content components render drawer account and navigation models from the shared drawer view model.
- Tests cover avatar content and sandwich animation behavior.

## Data & Control Flow

When the feature flag enables Compose UI, `NavigationDrawerHostFragment` creates `ComposeNavigationDrawerSheet`. The sheet renders Compose content, dispatches item selection to drawer view models, and forwards slide or state change callbacks.

## Integration Points

- Selected by `NavigationDrawerHostFragment` in `drawer/component/content/`.
- Uses models and view models from the `drawer/` package.
- Uses `android/core` Compose design and theme utilities.
