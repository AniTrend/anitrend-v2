# app/navigation/src/main/kotlin/co/anitrend/navigation/extensions/

## Responsibility

This package contains adapters that turn navigation contracts into Android operations.

## Design Patterns

- Extension functions keep Android framework calls outside route declarations.
- Reflection helpers instantiate fragment or activity classes returned by providers.
- Worker helpers adapt route params and worker classes to WorkManager requests.
- Deep link helpers connect parser results to route start operations.

## Data & Control Flow

A caller obtains a route target, calls an extension such as `startActivity`, `forFragment`, or work scheduling helper, and the extension builds the required Android object with route parameters attached.

## Integration Points

- Used by app shell, deep link routes, feature modules, and task schedulers.
- Bridges `NavigationTargets.kt` provider outputs to Android framework APIs.
