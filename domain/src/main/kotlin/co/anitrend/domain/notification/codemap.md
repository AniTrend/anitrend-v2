# domain/src/main/kotlin/co/anitrend/domain/notification/

## Responsibility

Defines notification entity, type enum, and notification query params.

## Design Patterns

`NotificationParam` models request input while `Notification` and `NotificationType` model app-facing notification content and categories.

## Data & Control Flow

Notification params are consumed by data or feature flows that fetch and display account notifications.

## Integration Points

Used by notification feature and account-driven data flows.
