# feature/settings/

## Responsibility

Owns settings navigation, preference construction, account settings, AniList settings, developer settings, feature flags, filters, locale, logs, notifications, power, sync, and theme settings surfaces.

## Design Patterns

- Local settings navigation is centered on `SettingsScreen` and `SettingsCompose`.
- `PreferenceBuilder` and `IPreferenceBuilder` centralize preference row creation.
- Content packages split settings areas by concern, each with local Compose, presenter, state, mapper, or ViewModel files as needed.
- Koin modules and feature provider expose the settings destination.

## Data & Control Flow

- `SettingsRouter` enters `SettingsScreen`.
- Local Compose navigation chooses a settings destination or content package.
- Presenters, mappers, and ViewModels adapt persisted settings and user actions into settings UI state.

## Integration Points

- Uses `common/markdown` and `common/shared`.
- Consumes auth, settings, user, and media list related interactors.
- Uses AndroidX Preference KTX and core configuration helpers.

## Key Paths

- `feature/settings/src/main/kotlin/`
- `feature/settings/src/main/AndroidManifest.xml`
- `feature/settings/build.gradle.kts`
