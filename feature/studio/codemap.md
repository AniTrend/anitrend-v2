# feature/studio/

## Responsibility

Owns studio detail destination, detail UI state, and studio content presentation.

## Design Patterns

- `StudioDetailUiState` models screen state for `StudioCompose` and `StudioContent`.
- `StudioViewModel` prepares studio detail state.
- Provider and Koin modules expose `StudioRouter.Provider`.

## Data & Control Flow

- `StudioRouter` enters `StudioScreen` with studio payloads.
- `StudioScreen` hosts Compose content.
- `StudioViewModel` loads studio data and emits `StudioDetailUiState`.

## Integration Points

- Uses `common/shared` and browser support.
- Consumes studio and media interactors.
- Uses navigation extensions for payload handling.

## Key Paths

- `feature/studio/src/main/kotlin/`
- `feature/studio/src/main/AndroidManifest.xml`
- `feature/studio/build.gradle.kts`
