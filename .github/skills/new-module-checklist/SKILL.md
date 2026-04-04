---
name: new-module-checklist
description: 'Step-by-step module creation checklist. Use when adding a new Gradle module and wiring build, DI, and architecture integration correctly.'
---

# Skill: Adding a New Module

## Overview

The build system is convention-driven: naming a module correctly causes `buildSrc/CorePlugin` to
automatically apply the right plugins, dependencies, and Room options. Follow this checklist when
adding any new Gradle module.

## Key files to read

- `buildSrc/src/main/java/co/anitrend/buildSrc/module/Modules.kt` — central registry of all
  module paths; **must** be updated for every new module
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/CorePlugin.kt` — the convention plugin
  applied to every module
- `buildSrc/src/main/java/co/anitrend/buildSrc/plugins/components/ProjectDependencies.kt` —
  determines which libraries are automatically injected based on module type
- `settings.gradle.kts` — where the `include(":your:new:module")` declaration lives

## Step-by-step checklist

### 1. File system
- [ ] Create the module directory following the existing layout (`feature/`, `data/`, `common/`, etc.).
- [ ] Add `build.gradle.kts` applying the `CorePlugin` (copy from a similar existing module).
- [ ] Add `src/main/kotlin/` and `src/main/AndroidManifest.xml` (for Android library modules).

### 2. Gradle wiring
- [ ] Add `include(":your:new:module")` to `settings.gradle.kts`.
- [ ] Register the path constant in `Modules.kt` under the correct category
  (`Feature`, `Data`, `Common`, `Android`, `Task`, etc.).

### 3. Dependency injection (Koin)
- [ ] Create `<module>/src/main/kotlin/.../koin/Modules.kt` (see `.github/skills/koin-module-wiring/SKILL.md`).
- [ ] Add the module loader to the nearest aggregator (or to
  `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt` for top-level modules).

### 4. Domain / Data wiring (if a data or feature module)
- [ ] Choose the nearest reference pattern first: `tag` for simple query-only, `media` for
  multi-contract reads, `medialist` or `review` for hybrid flows, `favourite` for mutation-only.
- [ ] Define the domain repository contract in `:domain`; for hybrid modules split it by operation
  (`Detail`, `Paged`, `Save`, `Delete`, etc.) instead of one broad interface.
- [ ] Define the abstract domain use case in `:domain`; do not keep repository contracts or
  concrete interactor implementations inside the module `Types.kt`.
- [ ] Implement the repository specialization in the new data module and export aliases from
  `Types.kt`.
- [ ] Create the data-layer interactor bridge in `usecase/`; simple modules may use
  `XxxUseCaseImpl`, while multi-operation modules may use nested `XxxInteractor` classes.
- [ ] If writes should be background-safe or follow existing mutation routing, add the matching
  `:task:*` worker and router plumbing instead of calling the mutation interactor directly from
  feature code.
- [ ] Follow the four-file entity pattern if adding persistence (see `.github/skills/room-entity-pattern/SKILL.md`).

### 5. Validation
- [ ] Run `./gradlew :your:new:module:assembleDebug` to verify plugin and dependency resolution.
- [ ] Run `./gradlew spotlessCheck` to validate formatting.
- [ ] If Room is involved, confirm schema JSON is exported under `data/schemas/`.

## Automatic dependency injection by module type

| Module prefix | Auto-included libraries |
|---|---|
| `:feature:*` | support-arch UI/domain/data, Compose, AndroidX core, Koin, Timber |
| `:data:*` | Room + KAPT, Retrofit, OkHttp, Kotlinx Serialization, Chucker (debug) |
| `:common:*` | support-arch UI, Compose (if name matches pattern), Koin |
| `:task:*` | WorkManager, support-arch, Koin |
| `:android:*` | AndroidX core, support-arch, Koin |
