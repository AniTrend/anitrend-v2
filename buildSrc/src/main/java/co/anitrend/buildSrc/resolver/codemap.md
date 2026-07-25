# buildSrc/src/main/java/co/anitrend/buildSrc/resolver/

## Responsibility

`resolver/` contains dependency resolution helpers used to normalize versions and targets during Gradle dependency resolution.

Key file:

- `ConfigurationResolver.kt` defines `Configuration.handleConflicts(project)`.

## Design Patterns

- Configuration extension function pattern for applying resolution strategy rules.
- Version catalog backed substitutions so conflict rules point at the same aliases used elsewhere.
- Group and module based matching for common transitive dependency conflicts.

## Data & Control Flow

Gradle configurations can call `handleConflicts(project)`. The helper inspects each requested dependency and forces Kotlin modules, kotlinx serialization JSON, kotlinx datetime, Material, Timber, and AndroidX Startup toward catalog backed targets.

## Integration Points

- Reads versions and library aliases from `gradle/libs.versions.toml` through `libs.*`.
- Intended to be used by Gradle configuration blocks that need consistent dependency resolution.
