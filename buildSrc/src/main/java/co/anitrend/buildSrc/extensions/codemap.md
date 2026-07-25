# buildSrc/src/main/java/co/anitrend/buildSrc/extensions/

## Responsibility

`extensions/` contains Gradle helper extensions used by the convention plugin. These helpers classify projects, access Android and Gradle extensions, expose generated version catalog accessors, and add dependencies to typed configurations.

Key files:

- `ProjectExtensions.kt` maps project paths to module groups and wraps common Gradle extension lookups.
- `DependencyHandlerExtensions.kt` provides typed helpers for `implementation`, `runtimeOnly`, `ksp`, flavor configurations, platform BOMs, and test configurations.

## Design Patterns

- Path predicate helpers, such as `matchesDataModule`, `matchesFeatureModule`, and `hasComposeSupport`, keep module rules in one place.
- Gradle extension accessors hide `extensions.getByType` calls from plugin components.
- Dependency handler wrappers centralize configuration names and support platform dependencies.
- Internal visibility keeps these helpers scoped to build logic.

## Data & Control Flow

Plugin components call project predicates to decide which plugins, dependencies, Android options, compiler options, and formatting rules to apply. Dependency helpers translate catalog aliases and project paths into Gradle configuration entries.

## Integration Points

- Consumes `Modules.kt` for path comparisons.
- Consumes generated `LibrariesForLibs` accessors from `gradle/libs.versions.toml`.
- Used by `plugins/components/*` and `plugins/strategy/DependencyStrategy.kt`.
