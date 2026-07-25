# feature/airing/

## Responsibility

Owns the airing schedule destination and paged schedule content.

## Design Patterns

- Feature provider and Koin initializer expose the destination through `AiringRouter`.
- `AiringScreen` hosts `AiringContent` and Compose route helpers.
- `AiringViewModel` consumes airing, settings, and user interactors.

## Data & Control Flow

- Navigation enters through `AiringRouter` and `FeatureProvider`.
- Screen and content classes bind UI events to `AiringViewModel`.
- The ViewModel requests paged airing data and updates content state for Compose and shared media UI components.

## Integration Points

- Uses `common/media`, `common/genre`, `common/tag`, and `common/shared`.
- Consumes airing, settings, and user interactors exported by data aliases.
- Uses Paging Compose for list presentation.

## Key Paths

- `feature/airing/src/main/kotlin/`
- `feature/airing/src/main/AndroidManifest.xml`
- `feature/airing/build.gradle.kts`
