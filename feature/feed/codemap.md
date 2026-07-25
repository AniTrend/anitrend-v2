# feature/feed/

## Responsibility

Owns the feed destination, content host, and feed ViewModel.

## Design Patterns

- Screen, content, ViewModel split through `FeedScreen`, `FeedContent`, and `FeedViewModel`.
- Feature provider and Koin modules expose `FeedRouter.Provider`.

## Data & Control Flow

- `FeedRouter` resolves `FeatureProvider` and launches `FeedScreen`.
- `FeedScreen` hosts `FeedContent`.
- `FeedContent` binds feed UI state from `FeedViewModel`.

## Integration Points

- Uses `common/shared` and core UI helpers.
- Connects to app navigation through `FeedRouter`.

## Key Paths

- `feature/feed/src/main/kotlin/`
- `feature/feed/src/main/AndroidManifest.xml`
- `feature/feed/build.gradle.kts`
