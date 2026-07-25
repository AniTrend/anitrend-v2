# app/src/main/kotlin/co/anitrend/

## Responsibility

This package is the main application shell for the app module. It owns the concrete `App` class, `MainScreen`, app level DI, push registration coordination, and feature router providers for the home activity.

## Design Patterns

- `App` extends `AniTrendApplication` and delegates dependency injection startup to AndroidX Startup.
- `MainScreen` extends the app core bound screen base and coordinates classic fragments with a bottom drawer.
- Koin modules bind app shell presenters, view models, analytics, and `MainRouter.Provider` implementations.
- Small action and state classes isolate toolbar, FAB, and selected navigation state changes.

## Data & Control Flow

`App` starts `InjectorInitializer`, then `ApplicationInitializer` loads app modules. `MainScreen` observes `INavigationDrawer.navigationFlow`, resolves the selected menu item, and commits the matching feature fragment through router contracts from `app/navigation/`.

## Integration Points

- `app/src/main/kotlin/co/anitrend/koin/Modules.kt` connects analytics, presenters, view models, and the main router provider.
- `app/src/main/kotlin/co/anitrend/provider/FeatureProvider.kt` supplies the main activity entry point.
- `app/src/main/kotlin/co/anitrend/push/PushRegistrationCoordinator.kt` bridges notification settings with push registration services.
- Android resources under `app/src/main/res/` define the main shell layout and menu IDs used by `MainScreen`.
