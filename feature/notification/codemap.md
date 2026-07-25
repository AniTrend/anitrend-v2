# feature/notification/

## Responsibility

Owns the notification destination and notification list presentation.

## Design Patterns

- Screen, Compose, ViewModel split through `NotificationScreen`, `NotificationCompose`, and `NotificationViewModel`.
- Provider and initializer wire the module to `NotificationRouter`.

## Data & Control Flow

- `NotificationRouter` enters `NotificationScreen` through the feature provider.
- `NotificationScreen` hosts Compose content.
- `NotificationViewModel` prepares notification state for rendering.

## Integration Points

- Uses `common/shared` and core screen infrastructure.
- Connects to app navigation through `NotificationRouter`.

## Key Paths

- `feature/notification/src/main/kotlin/`
- `feature/notification/src/main/AndroidManifest.xml`
- `feature/notification/build.gradle.kts`
