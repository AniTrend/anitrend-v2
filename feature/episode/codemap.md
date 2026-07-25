# feature/episode/

## Responsibility

Owns episode browsing content and the episode bottom sheet destination.

## Design Patterns

- Separates sheet host files from list content files.
- `EpisodeContentViewModel` and `EpisodeSheetViewModel` split list state from sheet state.
- Provider, initializer, and Koin modules expose `EpisodeRouter` entry points.

## Data & Control Flow

- Episode navigation resolves the feature provider for sheet or content entry.
- `EpisodeContent` renders paged episode content through `EpisodeContentViewModel`.
- `EpisodeSheet` renders focused episode detail through `EpisodeSheetViewModel`.

## Integration Points

- Uses `common/episode` and `common/markdown` for episode cards and rich text.
- Consumes feed and episode interactors.
- Uses Paging Compose, browser support, and Jsoup.

## Key Paths

- `feature/episode/src/main/kotlin/`
- `feature/episode/src/main/AndroidManifest.xml`
- `feature/episode/build.gradle.kts`
