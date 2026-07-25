# feature/suggestion/

## Responsibility

Owns the suggestion destination and suggestion content presentation.

## Design Patterns

- Screen, content, ViewModel split through `SuggestionScreen`, `SuggestionContent`, and `SuggestionViewModel`.
- Provider and Koin modules expose `SuggestionRouter.Provider`.

## Data & Control Flow

- `SuggestionRouter` enters `SuggestionScreen`.
- `SuggestionScreen` hosts `SuggestionContent`.
- `SuggestionViewModel` prepares state for the content surface.

## Integration Points

- Uses `common/shared`, core UI helpers, and app navigation contracts.

## Key Paths

- `feature/suggestion/src/main/kotlin/`
- `feature/suggestion/src/main/AndroidManifest.xml`
- `feature/suggestion/build.gradle.kts`
