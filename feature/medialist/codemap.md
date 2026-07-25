# feature/medialist/

## Responsibility

Owns user media list screen, container state, and list content presentation.

## Design Patterns

- `MediaListScreen` hosts `MediaListContainer` and `MediaListCompose`.
- `UserViewModel` and `MediaListViewModel` split user profile state from list paging state.
- Provider and Koin modules expose media list navigation.

## Data & Control Flow

- Navigation enters `MediaListScreen` with user or list payloads.
- Container and Compose classes bind tab or list state to the ViewModels.
- ViewModels request user and media list data and emit UI state to the content.

## Integration Points

- Uses `common/shared` and `common/media`.
- Consumes media, media list, settings, and user interactors.
- Uses Paging Compose for list rendering.

## Key Paths

- `feature/medialist/src/main/kotlin/`
- `feature/medialist/src/main/AndroidManifest.xml`
- `feature/medialist/build.gradle.kts`
