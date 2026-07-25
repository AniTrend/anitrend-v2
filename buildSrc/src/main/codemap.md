# buildSrc/src/main/

## Responsibility

`buildSrc/src/main/` is the production source set for build logic. It contains the Java source root used for Kotlin files that implement the project convention plugin and its helpers.

## Design Patterns

- Single production source set for build logic.
- Kotlin files are stored under the Gradle Java source root, which is common for Kotlin DSL `buildSrc` projects.
- No runtime app code belongs here, only build configuration code.

## Data & Control Flow

The Gradle build loads this source set, compiles it into build logic classes, and makes those classes available to project scripts and plugin application during configuration.

## Integration Points

- `buildSrc/src/main/java/` holds the `co.anitrend.buildSrc` package tree.
- `buildSrc/build.gradle.kts` provides the plugin and dependency classpath used by this source set.
