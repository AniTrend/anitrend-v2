# feature/profile/

## Responsibility

Owns profile overview, library, feed, stats, hero, and section state presentation.

## Design Patterns

- Compose tabs split profile overview, library, feed, and stats surfaces.
- Support and model files define profile section state, chart entries, hero metadata, and detail groups.
- ViewModels are split by profile detail area.

## Data & Control Flow

- Profile navigation enters `ProfileScreen` with user payloads.
- `ProfileCompose` and route support choose the current profile tab.
- ViewModels request user, media list, media, and activity related state for section Composables.

## Integration Points

- Uses `common/media`, `common/review`, `common/shared`, and `common/markdown`.
- Consumes auth, user, media, and media list interactors.
- Uses navigation to open image viewer and media destinations.

## Key Paths

- `feature/profile/src/main/kotlin/`
- `feature/profile/src/main/AndroidManifest.xml`
- `feature/profile/build.gradle.kts`
