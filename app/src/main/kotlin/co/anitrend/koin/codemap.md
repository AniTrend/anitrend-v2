# app/src/main/kotlin/co/anitrend/koin/

## Responsibility

This package wires app module dependencies for the main activity shell.

## Design Patterns

- Koin modules are grouped by concern: analytics, presenter, view model, and feature provider.
- `scope<MainScreen>` keeps `MainPresenter` scoped to the main activity.
- `viewModel` DSL wires `MainScreenViewModel` with `SavedStateHandle`.
- `DynamicFeatureModuleHelper` exports one app module helper for startup loading.

## Data & Control Flow

`ApplicationInitializer` loads `appModules`. The resulting bindings make analytics, the main presenter, the main view model, and `MainRouter.Provider` available before `MainScreen` starts component initialization.

## Integration Points

- Uses flavor supplied `AnalyticsTree` implementations.
- Provides `MainRouter.Provider` through `app/src/main/kotlin/co/anitrend/provider/FeatureProvider.kt`.
- Consumes app core Koin helper utilities.
