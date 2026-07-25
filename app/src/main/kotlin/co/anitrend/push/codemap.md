# app/src/main/kotlin/co/anitrend/push/

## Responsibility

This package coordinates push registration from the app shell without owning notification delivery UI.

## Design Patterns

- Coordinator pattern keeps push setup separate from activity and service classes.
- Settings interfaces provide persisted registration state.
- Flavor source sets can provide platform specific push connectors.

## Data & Control Flow

Push registration reads persisted push settings, decides whether registration is needed, and delegates registration or cleanup to the active connector implementation.

## Integration Points

- Integrates with notification settings from `android/core` and data settings contracts.
- Flavor code under `app/src/google/` and `app/src/github/` supplies platform specific services.
- Notification permission requests are initiated by `MainScreen` through `android/core` helpers.
