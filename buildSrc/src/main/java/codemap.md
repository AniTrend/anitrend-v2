# buildSrc/src/main/java/

## Responsibility

`buildSrc/src/main/java/` is the source root for the build logic package tree. Despite the directory name, the implementation files are Kotlin source files for Gradle plugin code.

## Design Patterns

- Java source root reused for Kotlin Gradle DSL implementation.
- Package namespace starts at `co/anitrend/buildSrc`.
- Directory level codemaps below this root describe the build logic by package responsibility.

## Data & Control Flow

Source files under this root compile into the `buildSrc` build artifact, then Gradle loads the compiled plugin classes before configuring project modules.

## Integration Points

- `buildSrc/src/main/java/co/anitrend/buildSrc/` owns the actual AniTrend build logic.
- The resulting plugin code consumes `gradle/` and `spotless/` root configuration files.
