# buildSrc/src/main/java/co/anitrend/buildSrc/plugins/

## Responsibility

`plugins/` contains the AniTrend convention plugin and its supporting configuration packages. It is responsible for applying plugins, Android defaults, compiler options, dependency sets, optional flavor plugins, and Spotless formatting.

Key files and packages:

- `CorePlugin.kt` is the main `Plugin<Project>` implementation.
- `components/` contains focused setup functions for plugins, Android configuration, dependencies, options, properties, and Spotless.
- `strategy/` contains reusable default dependency strategy logic.

## Design Patterns

- Orchestrator plugin pattern: `CorePlugin.apply` delegates to component functions instead of holding all build rules inline.
- Ordered configuration pipeline: plugin application happens before Android extension configuration and dependency setup.
- Lifecycle logging is used to make configuration decisions visible during Gradle sync and builds.

## Data & Control Flow

When a project applies the convention plugin, `CorePlugin` calls `configurePlugins`, `configureAndroid`, `configureOptions`, `configureDependencies`, `configureAdditionalPlugins`, diagnostic component and extension logging, then `configureSpotless`.

## Integration Points

- Uses `extensions/` to read Gradle extensions and module predicates.
- Uses `module/Modules.kt` and `gradle/libs.versions.toml` to wire module and external dependencies.
- Uses `gradle/version.properties` and optional `.config` files for generated build config values.
- Uses `spotless/` license templates for formatting enforcement.
