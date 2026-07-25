# feature/image-viewer/

## Responsibility

Owns the full-screen image viewing destination and image page state.

## Design Patterns

- `ImageViewerScreen` hosts `ImageViewerContent`.
- `ImageViewerViewModel` owns image page request and state classes.
- `FeatureProvider` and Koin modules expose `ImageViewerRouter.Provider`.

## Data & Control Flow

- `ImageViewerRouter` passes image payloads into the screen.
- `ImageViewerScreen` creates the viewer host.
- `ImageViewerContent` displays pages from `ImageViewerViewModel` state.

## Integration Points

- Uses Coil and scaling image view dependencies.
- Uses navigation extensions for payload handling.
- Connects to app navigation through `ImageViewerRouter`.

## Key Paths

- `feature/image-viewer/src/main/kotlin/`
- `feature/image-viewer/src/main/AndroidManifest.xml`
- `feature/image-viewer/build.gradle.kts`
