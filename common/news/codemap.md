# common/news/

## Responsibility

Provides reusable news controller model and differ support.

## Design Patterns

- `NewsItem` adapts news domain data for controller or adapter use.
- `ControllerDiffers` supplies DiffUtil behavior.

## Data & Control Flow

- Feature news surfaces pass news models into shared controller items.
- Differs compare news items for efficient list updates.
- Item actions can route to news navigation.

## Integration Points

- Consumes news domain models.
- Connects item actions to `NewsRouter` through navigation extensions.

## Key Paths

- `common/news/src/main/kotlin/`
- `common/news/src/main/AndroidManifest.xml`
- `common/news/build.gradle.kts`
