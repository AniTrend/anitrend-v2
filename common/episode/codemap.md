# common/episode/

## Responsibility

Provides reusable episode cards, sheet Compose content, paging adapter, controller models, rating widget, and summary controller.

## Design Patterns

- Recycler adapter and DiffUtil support legacy paged lists.
- Compose card and sheet files support newer episode surfaces.
- Widget controllers encapsulate summary and rating binding.

## Data & Control Flow

- Feature episode or media surfaces pass domain episode models into shared adapter, Compose card, or widget classes.
- Shared components format summary, rating, and browse card state for display.
- Episode navigation extensions route item actions to episode destinations.

## Integration Points

- Uses `common/markdown` for episode text.
- Consumes episode and common domain model types.
- Connects item actions to `EpisodeRouter`.

## Key Paths

- `common/episode/src/main/kotlin/`
- `common/episode/src/main/AndroidManifest.xml`
- `common/episode/build.gradle.kts`
