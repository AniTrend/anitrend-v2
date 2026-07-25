# feature/media/

## Responsibility

Owns the main media detail destination, related sub-screens, media actions, schedule sheet, and media detail sections.

## Design Patterns

- Large feature module with screen hosts for characters, staff, studios, relations, recommendations, episodes, stats, and people.
- Compose routes and sections split detail summary, metadata, galleries, themes, trailer, connections, stats, people, and episode content.
- Action providers isolate manage-list and favourite actions.
- ViewModels are split by detail area to keep route state focused.

## Data & Control Flow

- `MediaRouter` payloads enter `MediaScreen` and related screen classes.
- `MediaScreenNavigation` and route classes choose the detail section or child surface.
- Section Composables render state provided by `MediaViewModel` and specialized ViewModels.
- Action providers hand user actions to media list or favourite flows.

## Integration Points

- Uses `common/character`, `common/staff`, `common/media`, `common/review`, `common/shared`, `common/genre`, `common/tag`, and `common/markdown`.
- Consumes media, airing, auth, favourite, media list, settings, and user interactors.
- Uses browser support, Media3 ExoPlayer UI, and Paging Compose.

## Key Paths

- `feature/media/src/main/kotlin/`
- `feature/media/src/main/AndroidManifest.xml`
- `feature/media/build.gradle.kts`
