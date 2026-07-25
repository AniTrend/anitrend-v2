# buildSrc/src/main/java/co/anitrend/buildSrc/plugins/strategy/

## Responsibility

`plugins/strategy/` contains reusable dependency strategy logic shared by module specific dependency setup.

Key file:

- `DependencyStrategy.kt` applies baseline Kotlin, Timber, test, Android test, lifecycle, coroutine, Koin, and selected support architecture dependencies.

## Design Patterns

- Strategy object per project, created with the current `Project`.
- Private dependency group functions keep default, lifecycle, coroutine, Koin, and app specific additions separate.
- Version catalog aliases and dependency helper extensions prevent raw configuration names in callers.

## Data & Control Flow

`ProjectDependencies.configureDependencies` creates `DependencyStrategy(project)` and calls `applyDependenciesOn(dependencies)`. The strategy adds common dependencies, then conditionally applies lifecycle, Android test, coroutine, Koin, and other dependencies based on project path predicates.

## Integration Points

- Uses `extensions/DependencyHandlerExtensions.kt` for configuration helper calls.
- Uses `extensions/ProjectExtensions.kt` for module predicates.
- Uses `Modules.kt` for exact project path checks.
- Uses `gradle/libs.versions.toml` through generated `libs.*` accessors.
