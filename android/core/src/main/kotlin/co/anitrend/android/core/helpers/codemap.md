# android/core/src/main/kotlin/co/anitrend/android/core/helpers/

## Responsibility

This package contains focused Android helper utilities for color, date, image, and notification behavior.

## Design Patterns

- Helper packages are grouped by Android capability rather than feature ownership.
- Notification helpers isolate Android version checks and notification channel behavior.
- Image helpers define shared request image data used by app image loading.
- Date helper implements the support architecture date helper contract.

## Data & Control Flow

Consumers call helpers directly or through Koin bindings. Notification permission checks account for API level and channel state. Date formatting and image request models flow into presentation and image loading code.

## Integration Points

- Notification helpers are used by `MainScreen` and settings notification surfaces.
- Image request models are consumed by `app/core` Coil integration.
- Date helper is bound in `android/core` Koin modules.
