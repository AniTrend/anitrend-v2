# feature/updater/

## Responsibility

Owns update check UI, update channel state, and update result presentation.

## Design Patterns

- `UpdateChannel`, `UpdateCheckState`, and `UpdateUiState` model update status.
- `UpdateViewModel` coordinates update state for `UpdateCompose`.
- Provider and Koin modules expose `UpdaterRouter.Provider`.

## Data & Control Flow

- `UpdaterRouter` enters `UpdateScreen`.
- `UpdateScreen` hosts `UpdateCompose`.
- `UpdateViewModel` checks update state and emits UI state.

## Integration Points

- Uses `common/shared` and `data/core` update infrastructure.
- Connects to app navigation through `UpdaterRouter`.

## Key Paths

- `feature/updater/src/main/kotlin/`
- `feature/updater/src/main/AndroidManifest.xml`
- `feature/updater/build.gradle.kts`
