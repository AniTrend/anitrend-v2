# android/core/src/main/kotlin/co/anitrend/android/core/

## Responsibility

This package contains shared Android core source. It includes settings, configuration helpers, notification utilities, storage and power controllers, shortcuts, extensions, reusable views, RecyclerView helpers, Compose design components, and Material theme setup.

## Design Patterns

- Contract plus implementation packages keep Android services replaceable and testable.
- Settings class implements multiple domain and data setting interfaces as one preference backed source.
- Compose design components use Material3 theme tokens from `ui/`.
- Resource and helper packages isolate Android framework operations from app and feature code.

## Data & Control Flow

Koin creates `Settings`, locale and theme helpers, storage controller, dispatchers, date helper, notification helper, shortcut controller, and power controller. Activity base classes and screens use configuration helpers during lifecycle callbacks. UI code consumes reusable components and theme tokens.

## Integration Points

- Koin bindings are in `koin/Modules.kt`.
- Settings interfaces connect to contracts from `data/settings` and `data/auth`.
- Compose theme is used by `android/deeplink` and newer feature surfaces.
- Notification helpers are called by `app` and settings surfaces.
