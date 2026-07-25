# app/core/

## Responsibility

`app/core/` is the shared app runtime layer used by the app shell and feature modules. It provides base activity, screen, content, view model, Koin, migration, logging, Coil, and crash handling infrastructure.

## Design Patterns

- Base classes standardize lifecycle, binding, presenter, and view model behavior.
- AndroidX Startup initializers compose boot phases for injection, logging, and migrations.
- Koin module aggregation includes platform core and data modules in one runtime graph.
- Coil customization centralizes cache limits, request image handling, and decoder selection.

## Data & Control Flow

Core initializers create the Koin application, load `coreModules`, plant Timber trees, and execute migrations. Screens and feature modules then consume core base classes and helper extensions. Image requests flow through Coil, custom fetchers, and storage-backed cache directories.

## Integration Points

- Includes `android/core` and `data` Koin modules in `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.
- Provides screen base classes consumed by `app/`, `android/deeplink/`, feature modules, and common modules.
- Provides `AppScope.BOTTOM_NAV_DRAWER` used by `android/navigation` drawer fragments.
