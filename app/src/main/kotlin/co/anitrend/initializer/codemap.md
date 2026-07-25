# app/src/main/kotlin/co/anitrend/initializer/

## Responsibility

This package contains app module AndroidX Startup integration after core dependency injection has started.

## Design Patterns

- `ApplicationInitializer` extends `AbstractCoreInitializer` from app core.
- Dependency ordering declares `InjectorInitializer` as a required initializer.
- Dynamic module loading uses `DynamicFeatureModuleHelper.loadModules()`.
- First install shortcut setup is guarded by SDK level and persisted settings.

## Data & Control Flow

AndroidX Startup calls `ApplicationInitializer.create`. The initializer loads app shell modules, checks `ISupportPreference.isNewInstallation`, and creates shortcuts through `IShortcutController` when supported.

## Integration Points

- Depends on `app/core/src/main/kotlin/co/anitrend/core/initializer/injector/InjectorInitializer.kt`.
- Loads modules from `app/src/main/kotlin/co/anitrend/koin/Modules.kt`.
- Uses `android/core` shortcut contracts and settings abstractions.
