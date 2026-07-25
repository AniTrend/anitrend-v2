# buildSrc/src/main/java/co/anitrend/buildSrc/module/

## Responsibility

`module/` owns the typed registry of AniTrend Gradle project paths. It is the source of truth for module group prefixes and enumerated app, domain, data, android, common, feature, and task modules.

Key file:

- `Modules.kt` defines prefix constants, a shared `Module` interface, and enum groups whose `path` value formats Gradle project paths.

## Design Patterns

- Enum backed registry for module paths, which avoids scattering raw strings across build logic.
- Prefix constants for module family detection.
- Shared `Module.path` derived property for Gradle path formatting.

## Data & Control Flow

Build logic calls enum values and prefix constants when classifying projects, adding inter module dependencies, and exposing module aliases through `Libraries.kt`.

## Integration Points

- Used by `extensions/ProjectExtensions.kt` for module group matching.
- Used by `plugins/components/ProjectDependencies.kt` for automatic dependency wiring.
- Used by `Libraries.kt` for script friendly access to common module paths.
- Must stay aligned with included modules in the root Gradle settings.
