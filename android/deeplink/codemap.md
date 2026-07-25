# android/deeplink/

## Responsibility

`android/deeplink/` owns the app deep link, splash, and onboarding entry flow. It parses AniList and AniTrend URIs, builds destination intents, and redirects into the main app or feature routes.

## Design Patterns

- A dedicated `DeepLinkScreen` Compose activity hosts splash and onboarding state.
- Route objects implement URI matching and produce nullable intents through a parser pipeline.
- Koin modules bind presenters, view model, environment, parser, and `DeepLinkRouter.Provider`.
- Environment abstraction carries authentication and app settings into route matching.

## Data & Control Flow

Incoming intents enter `DeepLinkScreen`. The view model processes `intent.data`, the parser checks registered routes, and the screen launches the resolved intent or falls back to `MainRouter`. Splash and onboarding state are rendered through Compose content.

## Integration Points

- Implements `DeepLinkRouter.Provider` from `app/navigation`.
- Uses `android/core` theme, environment, and screen base classes.
- Routes target app and feature routers declared in `app/navigation/NavigationTargets.kt`.
