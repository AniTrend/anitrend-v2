# android/core/src/main/kotlin/co/anitrend/android/core/ui/

## Responsibility

This package defines AniTrend Material3 theme setup, color tokens, typography, typeface tokens, and preview helpers.

## Design Patterns

- `AniTrendTheme3` selects dynamic color when supported and falls back to static light or dark color schemes.
- Theme helper injection applies app theme and night mode from persisted settings.
- Typography and color tokens centralize visual constants for Compose components.

## Data & Control Flow

Compose entry points wrap content in `AniTrendTheme3`. The theme reads the active `IThemeHelper`, chooses a color scheme, applies activity theme side effects outside preview mode, and provides MaterialTheme values to child components.

## Integration Points

- Used by `android/deeplink/src/main/kotlin/co/anitrend/android/deeplink/component/screen/DeepLinkScreen.kt`.
- Used by Compose drawer and feature Compose screens.
- Reads theme behavior from settings helpers in `android/core/settings/`.
