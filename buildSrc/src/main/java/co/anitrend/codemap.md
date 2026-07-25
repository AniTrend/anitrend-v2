# buildSrc/src/main/java/co/anitrend/

## Responsibility

`buildSrc/src/main/java/co/anitrend/` is the AniTrend package namespace for custom Gradle build logic.

## Design Patterns

- Namespace boundary between organization packages and build logic implementation.
- Keeps build logic under `co.anitrend.buildSrc` rather than mixing it with app runtime packages.

## Data & Control Flow

Gradle compiles source under this package as part of the `buildSrc` bootstrap. Runtime application modules do not call this package directly.

## Integration Points

- `buildSrc/src/main/java/co/anitrend/buildSrc/` contains the convention plugin, module registry, extensions, and resolver code.
