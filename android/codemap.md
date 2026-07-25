# android/

## Responsibility

`android/` contains Android platform modules shared by the app shell and features. It owns core Android helpers, resources, settings, theme, deep link entry, navigation drawer UI, and common i18n strings.

## Design Patterns

- Platform modules live outside feature modules so reusable Android helpers do not leak data layer implementations.
- Koin module helpers expose platform bindings to app core startup.
- Hybrid UI support keeps legacy fragment and view code beside newer Compose surfaces.
- Route provider implementations connect platform entry points to `app/navigation` router contracts.

## Data & Control Flow

App startup loads `androidCoreModules` through `app/core`. Deep link startup enters `android/deeplink`, parses incoming URIs, and redirects to app or feature routes. The main app screen hosts `android/navigation` drawer fragments, which emit selected drawer items back to the app shell.

## Integration Points

- `android/core/` provides settings, storage, theme, notification, shortcut, and helper APIs.
- `android/deeplink/` provides splash, onboarding, and URI route parsing.
- `android/navigation/` provides bottom navigation drawer UI and drawer state.
- `android/i18n/` provides shared Android string resources.
