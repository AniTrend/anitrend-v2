# common/genre/

## Responsibility

Provides reusable genre list adapter, controller model, differ, and Compose genre section.

## Design Patterns

- Recycler adapter and DiffUtil support legacy genre lists.
- Compose component supports media genre sections.
- Controller model wraps domain genre data for adapter use.

## Data & Control Flow

- Feature code passes genre models into adapter or Compose component.
- The shared component renders genre labels and handles click routing through media discover navigation.

## Integration Points

- Consumes genre domain models.
- Connects genre actions to `MediaDiscoverRouter` via navigation extensions.

## Key Paths

- `common/genre/src/main/kotlin/`
- `common/genre/src/main/AndroidManifest.xml`
- `common/genre/build.gradle.kts`
