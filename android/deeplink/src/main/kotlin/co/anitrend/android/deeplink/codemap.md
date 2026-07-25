# android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/

## Responsibility

This package contains the concrete deep link feature implementation: screen, Compose UI, presenters, view model, route definitions, environment, provider, initializer, and Koin wiring.

## Design Patterns

- Route definitions are split between app routes and web routes under `component/route/`.
- Presenter classes provide splash and onboarding presentation decisions scoped to `DeepLinkScreen`.
- `FeatureProvider` adapts the parser to `DeepLinkRouter.Provider`.
- `FeatureInitializer` loads the feature Koin module helper.

## Data & Control Flow

The initializer loads deep link modules. `DeepLinkScreen` renders themed Compose content, pushes splash state into `DeepLinkViewModel`, parses incoming URI data, and delegates navigation through route outputs.

## Integration Points

- Koin wiring is in `koin/Modules.kt`.
- Route outputs use routers and params from `app/navigation`.
- Compose UI uses `android/core` Material3 theme and shared components.
