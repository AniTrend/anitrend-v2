# android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/

## Responsibility

This package contains the deep link presentation and route handling components.

## Design Patterns

- `screen/` hosts the activity entry point.
- `compose/` renders splash, onboarding, and navigation content.
- `presenter/` computes presentation state and resource backed content.
- `route/` contains parser routes that convert URI matches to destination intents.
- `viewmodel/` stores transient splash, onboarding, and intent state.

## Data & Control Flow

`DeepLinkScreen` passes view model and presenters to Compose content. URI handling flows through the view model and route parser, then the screen launches the resolved destination and finishes itself.

## Integration Points

- Uses `app/navigation` routers for destination intents.
- Uses `android/core` theme and app core screen injection helpers.
- Environment state from `environment/` influences route matching behavior.
