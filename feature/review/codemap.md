# feature/review/

## Responsibility

Owns review browsing or detail content and review route state.

## Design Patterns

- Screen, content, route, and ViewModel split through review component files.
- `FeatureProvider`, initializer, and Koin module expose review navigation.
- Uses shared review cards from common UI.

## Data & Control Flow

- `ReviewRouter` enters `ReviewScreen` with review payloads.
- `ReviewContent` renders state from `ReviewViewModel`.
- Review task routing is used where review actions cross into task-backed flows.

## Integration Points

- Uses `common/review` and `common/shared`.
- Consumes auth, review, user, media list, and review domain interactors.
- Connects to `ReviewRouter` and `ReviewTaskRouter`.

## Key Paths

- `feature/review/src/main/kotlin/`
- `feature/review/src/main/AndroidManifest.xml`
- `feature/review/build.gradle.kts`
