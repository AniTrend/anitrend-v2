# app/core/src/main/kotlin/co/anitrend/core/

## Responsibility

This package contains app runtime primitives: `AniTrendApplication`, coroutine services, base screen and content types, migration manager, Koin module helpers, Coil integration, crash handling, and UI extension utilities.

## Design Patterns

- Abstract base classes establish app wide lifecycle and UI contracts.
- Initializer classes provide modular AndroidX Startup entry points.
- DI helper classes load dynamic feature module groups and define shared Koin scopes.
- Extension packages keep Android framework glue near the runtime layer.

## Data & Control Flow

Application startup enters `AniTrendApplication`, which triggers Koin initialization. Koin loads core, platform, and data modules. Feature screens use core base classes to bind views, inject scoped presenters, observe view models, and commit fragment transactions.

## Integration Points

- `koin/Modules.kt` integrates Android core settings, storage, network messages, and data modules.
- `component/` is consumed by screens in `app/`, `android/deeplink/`, and feature modules.
- `ui/UiExtensions.kt` supports fragment creation and transaction helpers for app navigation.
- `coil/` plugs app specific request image handling into Coil.
