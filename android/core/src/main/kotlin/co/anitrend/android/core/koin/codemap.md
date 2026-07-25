# android/core/src/main/kotlin/co/anitrend/android/core/koin/

## Responsibility

This package defines the Koin module aggregate for Android platform services and helpers.

## Design Patterns

- One `Settings` implementation is bound to multiple settings interfaces.
- Android system services are resolved from `androidContext()` through typed helpers.
- Core, configuration, and controller modules separate settings, locale and theme, and service backed controllers.
- Singleton and factory lifetimes reflect whether state should be shared or recreated.

## Data & Control Flow

`androidCoreModules` is included by app core. Koin then resolves settings interfaces, `ISupportDispatcher`, `IStorageController`, date and notification helpers, locale and theme helpers, configuration helper, power controller, and shortcut controller.

## Integration Points

- Included by `app/core` Koin modules.
- Supplies settings contracts used by data, task, feature, app, and Android modules.
- Supplies `IConfigurationHelper` used by app core screen lifecycle code.
