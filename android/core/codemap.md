# android/core/

## Responsibility

`android/core/` is the shared Android platform toolkit. It owns settings, theme helpers, storage helpers, notification helpers, shortcuts, power state, view and Compose utilities, reusable resources, and Koin bindings.

## Design Patterns

- Interface based helpers expose settings, locale, theme, storage, shortcut, and power control contracts.
- Koin module aggregation binds Android services and helper implementations.
- Extension packages keep framework utilities reusable without forcing inheritance.
- Shared Compose and View widgets provide a consistent platform toolkit for feature modules.

## Data & Control Flow

`androidCoreModules` binds settings and helper contracts. App and feature code resolves these helpers through Koin, reads or writes settings, applies configuration to activities, creates notifications and shortcuts, manages cache storage, and renders reusable UI components.

## Integration Points

- Included by `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`.
- Consumed by app shell, `android/deeplink`, `android/navigation`, common modules, feature modules, and task modules.
- Resources under `android/core/src/main/res/` provide theme tokens, dimensions, animations, menus, icons, and shared strings.
