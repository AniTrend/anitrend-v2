# Search UX + Deeplink Remediation Implementation Plan

> Superseded by `docs/superpowers/plans/2026-06-08-search-consistency-remediation.md`.

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make Search states deterministic (idle vs loading vs empty), remove stale partial-result behavior, and verify item/deeplink navigation for all entities.

**Architecture:** Keep orchestration in `feature/search` ViewModel and screen composables, keep navigation via existing router contracts, and fix dynamic-feature router availability through feature module initialization + Koin bindings. Use per-state UI branches to avoid mixed stale/empty sections.

**Tech Stack:** Kotlin, Jetpack Compose, Paging 3, Koin, AndroidX Startup, Gradle

---

### Task 1: Stabilize router provider loading for character/staff

**Files:**
- Modify: `feature/character/src/main/AndroidManifest.xml`
- Modify: `feature/staff/src/main/AndroidManifest.xml`
- Create: `feature/staff/src/main/kotlin/co/anitrend/staff/provider/FeatureProvider.kt`
- Create: `feature/staff/src/main/kotlin/co/anitrend/staff/koin/Modules.kt`
- Create: `feature/staff/src/main/kotlin/co/anitrend/staff/initializer/FeatureInitializer.kt`
- Test: `feature/staff/src/test/kotlin/co/anitrend/staff/koin/StaffFeatureModulesTest.kt`

- [ ] Ensure `androidx.startup.InitializationProvider` metadata exists for character + staff feature initializers.
- [ ] Ensure `StaffRouter.Provider` factory binding exists and resolves from Koin.
- [ ] Run: `./gradlew :feature:staff:testDebugUnitTest --no-daemon`
- [ ] Run: `./gradlew :app:installGoogleDebug --no-daemon`

### Task 2: Correct Search state model (idle vs loading vs empty)

**Files:**
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/viewmodel/SearchViewModel.kt`
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`
- Modify: `feature/search/src/main/res/values/strings.xml`
- Test: `feature/search/src/test/kotlin/co/anitrend/search/component/viewmodel/SearchViewModelTest.kt`

- [ ] Introduce explicit **idle** state for blank/unsubmitted query (no loading text shown).
- [ ] Keep **loading** state only for submitted non-blank query.
- [ ] Keep **empty** state only when submitted non-blank query resolves empty.
- [ ] Prevent stale section cards from previous runs from rendering when query/scope changes.
- [ ] Add tests for transitions: idle -> loading -> empty, and stale suppression.

### Task 3: HOME blended partial-results behavior

**Files:**
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`

- [ ] Collapse empty sections in HOME blended view (hide section block when non-critical and empty).
- [ ] Keep populated sections visible while sibling sections are loading/error/empty.
- [ ] Ensure section errors expose retry action scoped to that section.

### Task 4: Deeplink verification matrix (runtime)

**Files:**
- Modify (if needed): `feature/search/src/main/kotlin/co/anitrend/search/component/screen/SearchScreen.kt`

- [ ] Verify item tap navigation for Media/User/Studio/Staff/Character from visible results.
- [ ] Verify "See all" for each section maps to the correct drill-down scope.
- [ ] Verify back behavior: drill-down -> HOME with query preserved.
- [ ] Capture adb log evidence that no `NoDefinitionFoundException` is emitted for `CharacterRouter.Provider` or `StaffRouter.Provider`.

### Task 5: Regression checks + handoff

**Files:**
- Modify: `docs/superpowers/specs/` (only if behavior contract needs codifying)

- [ ] Run: `./gradlew :feature:search:compileDebugKotlin :feature:staff:compileDebugKotlin :feature:character:compileDebugKotlin --no-daemon`
- [ ] Run: `./gradlew :feature:search:testDebugUnitTest :feature:staff:testDebugUnitTest --no-daemon`
- [ ] Optional full gate: `./gradlew lint spotlessCheck --no-daemon`
- [ ] Re-run Argent audit for: idle screen, partial results, and deeplink taps.
- [ ] Record final pass/fail matrix in PR comment.
