# buildSrc/

## Responsibility

`buildSrc/` owns the custom Gradle build logic that is compiled before the main AniTrend build. It centralizes Android plugin application, dependency bundles, version propagation, module registry values, lint and test defaults, Java and Kotlin toolchain settings, and Spotless formatting setup.

Key files:

- `buildSrc/build.gradle.kts` declares the Kotlin DSL build, version catalog access, and build logic dependencies.
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/CorePlugin.kt` is the main plugin entry point used by project modules.
- `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt` is the canonical typed module registry.
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt` maps module groups to shared dependency sets.
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectSpotless.kt` wires formatting rules to templates in `spotless/`.

## Design Patterns

- Convention plugin pattern: modules apply one build plugin and receive common Android, Kotlin, dependency, test, and formatting defaults.
- Typed module registry: `Modules.kt` keeps project path constants grouped by app, data, domain, android, common, feature, and task scopes.
- Module path predicates: `extensions/ProjectExtensions.kt` classifies projects by exact path or path prefix, then plugin components make decisions from those predicates.
- Version catalog access: build logic reads `gradle/libs.versions.toml` through generated `libs.*` accessors instead of hardcoding dependency coordinates.
- Componentized configuration: plugin work is split across `ProjectPlugins.kt`, `ProjectConfiguration.kt`, `ProjectOptions.kt`, `ProjectDependencies.kt`, and `ProjectSpotless.kt`.

## Data & Control Flow

1. Gradle compiles `buildSrc/` before project configuration.
2. `CorePlugin.apply` runs plugin setup, Android configuration, build options, dependency setup, optional Google plugin setup, diagnostic logging, and Spotless setup.
3. Project path checks in `extensions/ProjectExtensions.kt` determine whether a module is app, data, domain, android, feature, common, or task scoped.
4. `ProjectDependencies.kt` combines default dependencies from `DependencyStrategy` with module group dependencies from `Modules.kt` and `libs.*` aliases.
5. `ProjectOptions.kt` reads `gradle/version.properties` and optional `.config/*.properties` files to emit build config fields, Room schema options, and signing configuration.
6. `ProjectSpotless.kt` connects Gradle Spotless targets with license templates under `spotless/`.

## Integration Points

- `gradle/libs.versions.toml` supplies plugin, library, and version aliases.
- `gradle/version.properties` supplies `versionName`, `versionCode`, and release naming values.
- `spotless/copyright.kt`, `spotless/copyright.kts`, and `spotless/copyright.xml` provide license header templates.
- `proguard-common.pro` is added to app and library build types by `ProjectConfiguration.kt`.
- `.editorconfig` supplies ktlint formatting options through Spotless.
- `.config/secrets.properties`, `.config/configuration.properties`, and `.config/keystore.properties` are optional local inputs and are not owned by this directory.
