# android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/koin/

## Responsibility

This package wires all deep link feature dependencies.

## Design Patterns

- Presenter, core, view model, router, and feature modules are grouped before aggregation.
- `DeepLinkParser` is configured by route registration order.
- `AniTrendEnvironment` is bound to both concrete and interface types.
- `DynamicFeatureModuleHelper` exports modules for feature initialization.

## Data & Control Flow

`FeatureInitializer` loads `moduleHelper`. Koin then provides scoped presenters, the deep link view model, parser environment, parser instance, and `DeepLinkRouter.Provider`.

## Integration Points

- Implements provider binding for `app/navigation` deep link router.
- Uses user settings from data settings contracts.
- Uses Android application and context from Koin.
