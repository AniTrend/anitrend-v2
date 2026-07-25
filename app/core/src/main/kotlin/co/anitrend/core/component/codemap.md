# app/core/src/main/kotlin/co/anitrend/core/component/

## Responsibility

This package provides reusable app component base classes for screens, sheets, content containers, adapters, and view models.

## Design Patterns

- Bound screen and sheet classes centralize ViewBinding setup.
- Content classes wrap list, selection, and compose surfaces behind common lifecycle hooks.
- View model state classes encapsulate save and restore behavior.
- Load state adapter support standardizes paged list error and retry behavior.

## Data & Control Flow

Feature and app screens subclass these components, initialize binding or Compose content, and use injected presenters and view models. List content consumes paging load state through shared presenter and adapter helpers.

## Integration Points

- Used by `app/src/main/kotlin/co/anitrend/component/screen/MainScreen.kt`.
- Used by `android/deeplink` for the splash and deep link screen.
- Shared by feature and common modules that need app consistent lifecycle behavior.
