# feature/forum/

## Responsibility

Owns the forum destination and forum content presentation.

## Design Patterns

- Screen, Compose, content, ViewModel split supports a staged UI migration.
- `FeatureProvider`, initializer, and Koin module wire the feature into `ForumRouter`.

## Data & Control Flow

- `ForumRouter` enters `ForumScreen` through the provider.
- `ForumScreen` hosts `ForumCompose` and `ForumContent`.
- `ForumViewModel` feeds state into the content surface.

## Integration Points

- Uses `common/shared` for shared Compose scaffolding.
- Uses core UI helpers and app navigation contracts.

## Key Paths

- `feature/forum/src/main/kotlin/`
- `feature/forum/src/main/AndroidManifest.xml`
- `feature/forum/build.gradle.kts`
