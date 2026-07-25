# common/tag/

## Responsibility

Provides reusable media tag list adapter, controller model, differ, and Compose tag component.

## Design Patterns

- Recycler adapter and DiffUtil support legacy tag lists.
- Compose component supports newer tag sections.
- Controller model adapts tag data for adapter rendering.

## Data & Control Flow

- Feature code passes tag domain models into adapter or Compose components.
- The shared component renders tag labels and handles tag action routing through media discover navigation.

## Integration Points

- Consumes tag domain models.
- Connects tag actions to `MediaDiscoverRouter` through navigation extensions.

## Key Paths

- `common/tag/src/main/kotlin/`
- `common/tag/src/main/AndroidManifest.xml`
- `common/tag/build.gradle.kts`
