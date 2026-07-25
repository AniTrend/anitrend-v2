# common/media/

## Responsibility

Provides reusable media cards, carousel controllers, detail sections, metadata widgets, status, score, rank, synopsis, progress, rating, airing, releasing, and title components.

## Design Patterns

- Compose item, component, section, widget, preview, and extension packages support modern media surfaces.
- Classic widget packages with controllers support legacy XML or view-based surfaces.
- `MediaPresenter` centralizes media presentation formatting.

## Data & Control Flow

- Feature media, airing, search, profile, and media list modules pass media domain models into shared components.
- Presenter and extension helpers adapt model fields into display state.
- Components route media item actions to navigation helpers when needed.

## Integration Points

- Uses `common/genre`, `common/shared`, and `common/markdown`.
- Consumes media, airing, carousel, genre, settings, and user model types.
- Uses Paging Compose for browse and paged media content.

## Key Paths

- `common/media/src/main/kotlin/`
- `common/media/src/main/AndroidManifest.xml`
- `common/media/build.gradle.kts`
