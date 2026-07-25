# app/navigation/

## Responsibility

`app/navigation/` defines the app wide navigation and task routing contracts used to decouple feature modules, app shell code, and task modules.

## Design Patterns

- Router singletons expose typed provider interfaces that are bound by owning modules.
- Parcelable parameter models implement `IParam` for Bundle transfer.
- Extension functions convert params, launch activities, create fragments, and enqueue work.
- Work scheduler controller contracts abstract WorkManager scheduling for task routes.

## Data & Control Flow

Callers invoke a router, the router resolves a Koin bound provider, and provider implementations return an activity intent, fragment class, sheet class, worker class, or scheduler. Optional parameter objects are converted to `Bundle` or work input data.

## Integration Points

- Consumed by `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`.
- Implemented by feature, common, task, `android/deeplink`, and `android/navigation` modules.
- Uses domain enums in route parameter types without importing data layer implementations.
