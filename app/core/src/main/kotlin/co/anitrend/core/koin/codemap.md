# app/core/src/main/kotlin/co/anitrend/core/koin/

## Responsibility

This package aggregates core dependency modules and defines app scope helpers for runtime injection.

## Design Patterns

- Module aggregation includes local core modules, `androidCoreModules`, and `dataModules`.
- Koin scopes model lifecycle tied objects such as the bottom navigation drawer.
- Helper classes load and unload dynamic feature module groups.
- Factories configure app wide singletons such as network messages, state layout config, custom tabs, emoji manager, and Coil.

## Data & Control Flow

Startup loads `coreModules`. Consumers later resolve settings, storage, dispatchers, state layout config, image loader, and data repositories through Koin. Dynamic feature module helpers extend the graph when app, feature, or task initializers run.

## Integration Points

- Includes `android/core/src/main/kotlin/co/anitrend/android/core/koin/Modules.kt`.
- Includes `data/android/koin` modules through `dataModules`.
- Exposes `AppScope.BOTTOM_NAV_DRAWER` for `android/navigation` drawer fragments.
