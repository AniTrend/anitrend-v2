---
applyTo: buildSrc/**
description: This file describes the Gradle build system and dependency management for AniTrend v2.
---

# Gradle Version Catalog and Dependencies

## Reference Routing

This instruction file captures build-system policy and conventions. For deeper implementation
guides, use:

- `.github/skills/new-module-checklist/SKILL.md` for adding modules end to end.
- `.github/skills/key-libraries/SKILL.md` for library stack context and integration choices.
- `.github/skills/testing-guidelines/SKILL.md` for validation commands and test expectations.
- `.github/skills/reference-map/SKILL.md` for the cross-file routing index.

To preserve this split strategy, avoid SHA-pinned links and run
`.github/scripts/audit-instruction-refs.sh` before merging documentation changes.

All dependency versions are centrally managed in `gradle/libs.versions.toml`. Use the generated
`libs.*` type-safe accessors (e.g., `libs.androidx.room.runtime`) instead of hardcoding artifact
coordinates. When adding or upgrading a library:

1. Add/update the version under `[versions]`.
2. Add the coordinate under `[libraries]` (or `[plugins]`) referencing that version.
3. Reference it via `libs.your.library.id` in build files.

## buildSrc: Custom Build Logic

Located at `buildSrc/src/main/java/co/anitrend/buildSrc/`. Key components:

- **`module/Modules.kt`** — central registry of all module paths (`Modules.Feature.Media.path`,
  etc.). Must be updated whenever a new module is added.
- **`plugins/CorePlugin.kt`** — convention plugin applied to every module; calls
  `configurePlugins`, `configureAndroid`, `configureOptions`, `configureDependencies`, etc.
- **`plugins/components/ProjectPlugins.kt`** — applies Android/Kotlin plugins per module type;
  conditionally applies Compose, Kapt, Parcelize, Spotless, and (for the `google` flavor)
  Google Services + Crashlytics.
- **`plugins/components/ProjectOptions.kt`** — sets `compileSdk`, `targetSdk`, Java compatibility;
  injects `versionName`/`versionCode` and secrets from `.config/*.properties` into `BuildConfig`;
  configures Room schema export for data modules; sets up release signing from
  `.config/keystore.properties` when present.
- **`plugins/components/ProjectDependencies.kt`** — automatically injects the correct library set
  based on module type (see table below). You typically do **not** need to declare common libraries
  in individual module build files.
- **`plugins/strategy/DependencyStrategy.kt`** — adds Timber, Koin, and test libraries (JUnit,
  MockK, Turbine) to every module automatically.
- **`plugins/components/ProjectSpotless.kt`** — configures Spotless/ktlint formatting rules.
- **`resolver/ConfigurationResolver.kt`** — forces dependency version alignment (e.g., all
  `kotlin-stdlib` artifacts use the catalog version) to prevent runtime conflicts.

## Automatic dependencies by module type

| Module prefix | Auto-included libraries |
|---|---|
| `:feature:*` | support-arch UI/domain/data, Compose, AndroidX core, Koin, Timber |
| `:data:*` | Room + KAPT, Retrofit, OkHttp, Kotlinx Serialization, Chucker (debug) |
| `:common:*` | support-arch UI, Compose (if name matches pattern), Koin |
| `:task:*` | WorkManager, support-arch, Koin |
| `:android:*` | AndroidX core, support-arch, Koin |

## Module Build Files

Individual `build.gradle.kts` files are minimal — they apply `CorePlugin` and declare only
module-specific dependencies not covered by the convention. Most dependencies are injected
automatically.

For a complete step-by-step guide to adding a new module, see
`.github/skills/new-module-checklist/SKILL.md`.

## Build Flavors and Variants

Two product flavors:

- `google` — includes Firebase Analytics and Crashlytics; requires `google-services.json`;
  targets the Play Store. Google Services + Crashlytics plugins are applied automatically when
  `google-services.json` is present.
- `oss` (or similar) — open-source variant without proprietary services; suitable for F-Droid.

When adding flavor-specific libraries, guard inclusion in `buildSrc` or the module's
`build.gradle.kts` using flavor conditions. Use `BuildConfig.FLAVOR` / `BuildConfig.GOOGLE` in
code to gate runtime behaviour.

## CI and Scripts

The project uses **GitHub Actions** (see `.github/workflows/`). CI runs `./gradlew build` and
`./gradlew spotlessCheck`. Run `./gradlew spotlessApply` locally before pushing.

## ProGuard / R8

`proguard-common.pro` in the root covers common keep rules. Update it when adding libraries that
use reflection or serialization.

## Quick Reference: Where to Find Things

| Need | Location |
|---|---|
| Dependency versions | `gradle/libs.versions.toml` |
| Module path registry | `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt` |
| All custom build logic | `buildSrc/src/main/java/co/anitrend/buildSrc/` |
| Global dependency classpath | `build.gradle.kts` (root) |
| Secrets / API keys | `.config/secrets.properties` (not in VCS) |
| App manifest + startup logic | `app/src/main/AndroidManifest.xml`, `app/core/src/.../initializer/` |

---
