# app/

## Responsibility

`app/` owns the application entry scope. It contains the installable app module, shared app runtime infrastructure in `app/core/`, and cross-feature navigation contracts in `app/navigation/`.

## Design Patterns

- Android application shell built on `app/core/src/main/kotlin/co/anitrend/core/AniTrendApplication.kt`.
- AndroidX Startup initializers load Koin, logging, migration, and feature modules in a fixed order.
- Router provider contracts in `app/navigation/` keep feature modules decoupled from app and Android platform code.
- Flavor specific implementations live under app source sets and are wired through Koin modules.

## Data & Control Flow

1. `app/src/main/kotlin/co/anitrend/App.kt` starts dependency injection through `InjectorInitializer`.
2. `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` includes Android core and data modules.
3. `app/src/main/kotlin/co/anitrend/initializer/ApplicationInitializer.kt` loads app shell modules and creates first install shortcuts.
4. `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt` hosts navigation drawer output and swaps feature fragments by router selection.

## Integration Points

- `app/core/` provides runtime, Koin, Coil, screen base classes, and migration support.
- `app/navigation/` provides `MainRouter`, feature routers, payload models, and task worker router contracts.
- `android/core/`, `android/deeplink/`, and `android/navigation/` provide Android platform helpers and entry surfaces.
- Feature, common, task, domain, and data modules are consumed through router, interactor, and Koin boundaries.
