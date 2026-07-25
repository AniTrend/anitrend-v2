# app/navigation/src/main/kotlin/co/anitrend/navigation/

## Responsibility

This package is the central router registry. `NavigationTargets.kt` declares route objects for screens, fragments, sheets, deep links, and background workers.

## Design Patterns

- Each route is an object extending `NavigationRouter` and injects a provider interface.
- Nested provider interfaces describe only the capabilities needed by that route.
- Nested parcelable param classes carry strongly typed route input.
- Work routes expose worker class and scheduler accessors for background tasks.

## Data & Control Flow

Feature and app callers use route objects such as `MediaRouter`, `SettingsRouter`, `NavigationDrawerRouter`, or task routers. The route delegates to its provider implementation, and extension helpers convert the result into Android activity, fragment, sheet, or WorkManager operations.

## Integration Points

- `router/` contains the base `NavigationRouter` contract.
- `model/` contains shared parcelable interfaces and sorting wrappers.
- `extensions/` contains Bundle, intent, fragment, route, and worker adapters.
- Provider implementations live in feature modules and Android platform modules.
