# buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/

## Responsibility

`plugins/components/` contains the focused functions that implement the AniTrend convention plugin. Each file owns one configuration concern for all modules that apply the plugin.

Key files:

- `ProjectPlugins.kt` applies Android application or library plugins, Spotless, Parcelize, KSP, Compose, and optional Google services or Crashlytics plugins.
- `ProjectConfiguration.kt` sets SDK levels, flavors, build types, lint, packaging, source sets, toolchains, compiler options, test tasks, and a `makeProguard` helper task.
- `ProjectOptions.kt` wires version build config fields, optional local config fields, Room schema options, and release signing configuration.
- `ProjectDependencies.kt` applies shared dependencies by module family and Compose support.
- `ProjectProperties.kt` reads `gradle/version.properties`.
- `ProjectSpotless.kt` configures Kotlin, Kotlin Gradle, and XML formatting targets.

## Design Patterns

- One concern per file, called from `CorePlugin` in a fixed order.
- Project path predicates drive conditional behavior instead of per module Gradle script duplication.
- Local config files are optional inputs and are only read when present.
- Shared dependency bundles are grouped by module family to preserve architecture boundaries.

## Data & Control Flow

`CorePlugin` invokes these functions after the Android plugin is applied. The components read project path, version catalog aliases, `Modules.kt` paths, Gradle extension objects, `gradle/version.properties`, and optional local config files, then mutate Gradle plugin state for the current project.

## Integration Points

- `gradle/libs.versions.toml` supplies all `libs.*` dependencies and plugin classpath aliases.
- `gradle/version.properties` supplies app and library version build fields.
- `spotless/` supplies license header templates.
- `.editorconfig` supplies ktlint options.
- `.config/secrets.properties`, `.config/configuration.properties`, and `.config/keystore.properties` are optional local inputs.
