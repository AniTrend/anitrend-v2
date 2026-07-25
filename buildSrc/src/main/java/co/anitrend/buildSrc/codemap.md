# buildSrc/src/main/java/co/anitrend/buildSrc/

## Responsibility

`co.anitrend.buildSrc` is the root package for AniTrend build conventions. It defines public build logic anchors, typed project path constants, Gradle extension helpers, plugin components, and dependency resolution rules.

Key files and packages:

- `Libraries.kt` exposes selected module paths through nested objects for script friendly access.
- `module/Modules.kt` is the canonical module registry used by build logic.
- `extensions/` provides project classification, Gradle extension accessors, and dependency helper functions.
- `plugins/` contains the convention plugin entry point and componentized configuration steps.
- `resolver/` centralizes dependency conflict handling.

## Design Patterns

- Package facade pattern through `Libraries.kt` and `Modules.kt` for stable path aliases.
- Extension helper pattern for Gradle APIs, so plugin components can stay declarative.
- Component pipeline pattern, where `CorePlugin` calls small configuration functions in a fixed order.
- Catalog backed dependency declarations through `libs.*` generated accessors.

## Data & Control Flow

`CorePlugin` orchestrates setup. It uses extension helpers to identify the current project, then plugin components apply Android options, module dependencies, Google flavor plugins, Spotless rules, and diagnostic logging. `Modules.kt` provides module path data and `gradle/libs.versions.toml` provides dependency aliases.

## Integration Points

- Project modules are classified by paths from `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt`.
- Version and dependency aliases come from `gradle/libs.versions.toml`.
- Release version values come from `gradle/version.properties`.
- Formatting templates are read from `spotless/`.
