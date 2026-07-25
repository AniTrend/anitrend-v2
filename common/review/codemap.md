# common/review/

## Responsibility

Provides reusable review card Compose presentation.

## Design Patterns

- `ReviewCompose` defines review card variants and shared review card rendering.

## Data & Control Flow

- Feature review, media, or profile surfaces pass review domain models into the shared card.
- The card renders review summary, related media, and author information with markdown support.

## Integration Points

- Uses `common/markdown`.
- Consumes review, media, media list, user, and common domain model types.

## Key Paths

- `common/review/src/main/kotlin/`
- `common/review/src/main/AndroidManifest.xml`
- `common/review/build.gradle.kts`
