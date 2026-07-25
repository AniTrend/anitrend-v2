# app/core/src/main/kotlin/co/anitrend/core/initializer/

## Responsibility

This package contains core startup phases for dependency injection, migration, and logging.

## Design Patterns

- AndroidX Startup initializers compose boot order through explicit dependency lists.
- Abstract initializer contracts separate core, feature, and task initialization shapes.
- WorkManager factory integration bridges Koin into worker creation.
- Koin logger adapters route dependency graph diagnostics through Timber.

## Data & Control Flow

`InjectorInitializer` starts the Koin graph and loads core modules. Logger and migration initializers run after injection is available. Worker creation is delegated through a Koin aware factory so task modules can resolve dependencies at runtime.

## Integration Points

- App module `ApplicationInitializer` depends on `InjectorInitializer`.
- `koin/Modules.kt` supplies the core graph loaded by these initializers.
- Task modules rely on the WorkManager factory in `initializer/injector/factory/`.
