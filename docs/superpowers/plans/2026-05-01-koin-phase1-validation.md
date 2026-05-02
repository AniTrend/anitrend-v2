# Phase 1 Koin Validation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add runtime-critical Koin resolution coverage for startup and authenticated flows so central `:data` and `:app:core` wiring regressions fail in normal `testDebugUnitTest` instead of at runtime.

**Architecture:** Use hybrid layered coverage: one focused `src/test` aggregator guard for `dataModules`, one for `coreModules`, and focused module-level resolution tests for the highest-risk authenticated-flow modules. Prioritize factories that depend on broad interfaces, typealiases, `EmbedMapper`, `PersistEmbedded`, and writer/mapper contracts that sit on startup or authenticated-navigation paths.

**Tech Stack:** Koin, MockK, JUnit/Kotlin test, `testDebugUnitTest`, code-review-graph, Gradle.

**Code Style Constraint:** Prefer normal Kotlin import statements for concrete types used in tests and module wiring changes. Avoid inline fully qualified package names in code examples and implementation unless there is a real naming collision that imports cannot resolve cleanly.

---

### Task 1: Add the Phase 1 aggregator guard for `dataModules`

**Files:**
- Create: `data/src/test/kotlin/co/anitrend/data/android/koin/DataModulesResolutionTest.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/android/koin/Modules.kt`
- Reference: `data/src/androidTest/kotlin/co/anitrend/data/android/koin/ModulesTest.kt`

- [ ] **Step 1: Write the failing test**

```kotlin
package co.anitrend.data.android.koin

import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class DataModulesResolutionTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun dataModulesResolveRuntimeCriticalRootContracts() {
        startKoin {
            modules(
                testDataRootModule,
                dataModules,
            )
        }

        assertNotNull(getKoin().get<IAniTrendStore>())
        assertNotNull(getKoin().get<IAuthenticationHelper>())
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.android.koin.DataModulesResolutionTest --no-daemon`
Expected: FAIL because the minimal helper module and/or current graph do not yet support clean resolution of all root contracts in a plain `src/test` Koin context.

- [ ] **Step 3: Add the smallest helper module needed for `src/test` graph bootstrapping**

```kotlin
private val testDataRootModule = module {
    factory { mockk<IAniTrendStore>(relaxed = true) }
    factory { mockk(relaxed = true) }
}
```

Keep this helper module narrow. It should provide only the Android/runtime collaborators required to boot the runtime-critical `dataModules` graph in `src/test`.

- [ ] **Step 4: Run test to verify it passes or reveals the first real binding gap**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.android.koin.DataModulesResolutionTest --no-daemon`
Expected: Either PASS or a concrete `NoDefinitionFoundException` / `InstanceCreationException` naming the next runtime-critical binding gap to fix.

- [ ] **Step 5: Commit**

```bash
git add data/src/test/kotlin/co/anitrend/data/android/koin/DataModulesResolutionTest.kt
git commit -m "test(data): add data modules resolution guard"
```

### Task 2: Add the Phase 1 aggregator guard for `coreModules`

**Files:**
- Create: `app/core/src/test/kotlin/co/anitrend/core/koin/CoreModulesResolutionTest.kt`
- Reference: `app/core/src/main/kotlin/co/anitrend/core/koin/Modules.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/android/koin/Modules.kt`

- [ ] **Step 1: Write the failing test**

```kotlin
package co.anitrend.core.koin

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class CoreModulesResolutionTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun coreModulesResolveStartupGraph() {
        startKoin {
            modules(
                testCoreRootModule,
                coreModules,
            )
        }

        assertNotNull(getKoin().get<co.anitrend.arch.core.model.IStateLayoutConfig>())
        assertNotNull(getKoin().get<co.anitrend.data.android.network.model.NetworkMessage>())
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `./gradlew :app:core:testDebugUnitTest --tests co.anitrend.core.koin.CoreModulesResolutionTest --no-daemon`
Expected: FAIL until the plain-unit bootstrap module supplies the Android/runtime context the startup graph expects.

- [ ] **Step 3: Add the smallest `src/test` bootstrap module for startup resolution**

```kotlin
private val testCoreRootModule = module {
    factory { mockk<android.content.Context>(relaxed = true) }
    factory { mockk<co.anitrend.android.core.storage.contract.IStorageController>(relaxed = true) }
}
```

Mirror the real startup dependency surface instead of mocking feature-level behavior. Keep the helper constrained to what `coreModules` needs to resolve startup bindings.

- [ ] **Step 4: Run test to verify it passes or reveals the next startup-scope gap**

Run: `./gradlew :app:core:testDebugUnitTest --tests co.anitrend.core.koin.CoreModulesResolutionTest --no-daemon`
Expected: Either PASS or a concrete Koin resolution error that identifies the next runtime-critical startup binding to cover.

- [ ] **Step 5: Commit**

```bash
git add app/core/src/test/kotlin/co/anitrend/core/koin/CoreModulesResolutionTest.kt
git commit -m "test(app-core): add core modules resolution guard"
```

### Task 3: Add `UserModulesTest` and catch the current `UserProfileOverviewMapper` regression red

**Files:**
- Create: `data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt`
- Reference: `data/src/test/kotlin/co/anitrend/data/auth/koin/AuthModulesTest.kt`

- [ ] **Step 1: Write the failing test**

```kotlin
package co.anitrend.data.user.koin

import co.anitrend.data.user.mapper.UserProfileOverviewMapper
import io.mockk.mockk
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent.getKoin

class UserModulesTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun userProfileOverviewMapperResolvesFromUserModules() {
        startKoin {
            modules(
                testUserModule,
                userModules,
            )
        }

        assertNotNull(getKoin().get<UserProfileOverviewMapper>())
    }
}
```

- [ ] **Step 2: Run test to verify it fails with the current root-cause error**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.user.koin.UserModulesTest --no-daemon`
Expected: FAIL with `InstanceCreationException` / `NoDefinitionFoundException` involving broad `EmbedMapper` lookup from `UserProfileOverviewMapper` or a closely-related dependency.

- [ ] **Step 3: Add the narrow helper module that matches the user-module dependency surface**

```kotlin
private val testUserModule = module {
    factory { mockk<co.anitrend.data.android.database.common.IAniTrendStore>(relaxed = true) }
    factory { mockk<co.anitrend.domain.common.ITransactionRunner>(relaxed = true) }
    factory { mockk<co.anitrend.data.user.cache.UserCache.Overview>(relaxed = true) }
}
```

Extend this helper only when the test output names an actual collaborator needed to boot `userModules` in `src/test`.

- [ ] **Step 4: Implement the smallest production fix for broad `EmbedMapper` lookups**

Reference the high-risk block in `data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt`:

```kotlin
factory {
    UserProfileOverviewMapper(
        favouriteEmbedMapper = get(),
        statusEmbedMapper = get(),
        mediaEmbedMapper = get(),
        writer = get(),
        transactionRunner = transaction(),
    )
}
```

Replace broad bare `get()` calls with explicit concrete lookups when the concrete binding is already owned by `userModules`, and prefer imports over inline package qualifiers, for example:

```kotlin
factory {
    UserProfileOverviewMapper(
        favouriteEmbedMapper = get<UserProfileConnectionMapper.FavouriteEmbed>(),
        statusEmbedMapper = get<StatusMapper.Activity.Embed>(),
        mediaEmbedMapper = get<MediaMapper.Embed>(),
        writer = get(),
        transactionRunner = transaction(),
    )
}
```

Use the exact concrete mapper types that the file already exports; do not introduce new generic root-scope bindings if explicit concrete lookup is enough. Add the matching import statements rather than leaving long inline fully qualified references in production or test code.

- [ ] **Step 5: Run test to verify it passes**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.user.koin.UserModulesTest --no-daemon`
Expected: PASS.

- [ ] **Step 6: Commit**

```bash
git add data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt
git commit -m "fix(data-user): bind overview mappers explicitly"
```

### Task 4: Extend `UserModulesTest` to cover the feed path from the same risk family

**Files:**
- Modify: `data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt`

- [ ] **Step 1: Write the second failing test**

```kotlin
@Test
fun userProfileFeedMapperResolvesFromUserModules() {
    startKoin {
        modules(
            testUserModule,
            userModules,
        )
    }

    assertNotNull(getKoin().get<co.anitrend.data.user.mapper.UserProfileFeedMapper>())
}
```

- [ ] **Step 2: Run the targeted test to verify it fails if the feed path still uses broad lookups**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.user.koin.UserModulesTest.userProfileFeedMapperResolvesFromUserModules --no-daemon`
Expected: FAIL if `UserProfileFeedMapper` still depends on broad root-scope `EmbedMapper` resolution.

- [ ] **Step 3: Apply the matching explicit-concrete lookup fix in production code**

Reference the production block:

```kotlin
factory {
    UserProfileFeedMapper(
        reviewConnectionMapper = get(),
        reviewPreviewMapper = get(),
        statusEmbedMapper = get(),
        mediaEmbedMapper = get(),
        writer = get(),
        transactionRunner = transaction(),
    )
}
```

Make the same style of explicit lookup change used for `UserProfileOverviewMapper`, requesting the concrete embed bindings exported by `userModules` and their owning modules. Add imports for those concrete types instead of using inline fully qualified package qualifiers.

- [ ] **Step 4: Run the full `UserModulesTest` class to verify both user-path tests pass**

Run: `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.user.koin.UserModulesTest --no-daemon`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt
git commit -m "test(data-user): cover feed and overview koin resolution"
```

### Task 5: Add focused module guards for the remaining Phase 1 authenticated-flow modules

**Files:**
- Create: `data/src/test/kotlin/co/anitrend/data/media/koin/MediaModulesTest.kt`
- Create: `data/src/test/kotlin/co/anitrend/data/review/koin/ReviewModulesTest.kt`
- Create: `data/src/test/kotlin/co/anitrend/data/medialist/koin/MediaListModulesTest.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/review/koin/Modules.kt`
- Reference: `data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt`

- [ ] **Step 1: Write one failing resolution test per runtime-critical module**

```kotlin
@Test
fun mediaModulesResolveDetailMapperPath() {
    startKoin { modules(testMediaModule, mediaModules) }
    assertNotNull(getKoin().get<co.anitrend.data.media.mapper.MediaMapper.Detail>())
}

@Test
fun reviewModulesResolveEntryWriterPath() {
    startKoin { modules(testReviewModule, reviewModules) }
    assertNotNull(getKoin().get<co.anitrend.data.review.mapper.ReviewWriterContract>())
}

@Test
fun mediaListModulesResolveEntryWriterPath() {
    startKoin { modules(testMediaListModule, mediaListModules) }
    assertNotNull(getKoin().get<co.anitrend.data.medialist.mapper.MediaListWriterContract>())
}
```

- [ ] **Step 2: Run each test class individually to watch the first real gap fail red**

Run:
- `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.media.koin.MediaModulesTest --no-daemon`
- `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.review.koin.ReviewModulesTest --no-daemon`
- `./gradlew :data:testDebugUnitTest --tests co.anitrend.data.medialist.koin.MediaListModulesTest --no-daemon`

Expected: each class either passes immediately or reveals a concrete missing binding in a runtime-critical path.

- [ ] **Step 3: Fix only the first concrete missing binding each test reveals**

Use the same policy as the auth and user fixes:
- prefer explicit concrete `get<ConcreteMapper>()` / `get<ConcreteWriter>()`
- do not add broad root-scope generic bindings if concrete module-owned bindings already exist
- keep each production fix limited to the path named by the failing test

- [ ] **Step 4: Rerun each module test class until green**

Run the same three commands from Step 2.
Expected: PASS for all three classes.

- [ ] **Step 5: Commit**

```bash
git add data/src/test/kotlin/co/anitrend/data/media/koin/MediaModulesTest.kt data/src/test/kotlin/co/anitrend/data/review/koin/ReviewModulesTest.kt data/src/test/kotlin/co/anitrend/data/medialist/koin/MediaListModulesTest.kt data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt data/src/main/kotlin/co/anitrend/data/review/koin/Modules.kt data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt
git commit -m "test(data): add phase 1 koin resolution coverage"
```

### Task 6: Run the Phase 1 verification gate

**Files:**
- Test: `data/src/test/kotlin/co/anitrend/data/android/koin/DataModulesResolutionTest.kt`
- Test: `app/core/src/test/kotlin/co/anitrend/core/koin/CoreModulesResolutionTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/auth/koin/AuthModulesTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/media/koin/MediaModulesTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/review/koin/ReviewModulesTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/medialist/koin/MediaListModulesTest.kt`

- [ ] **Step 1: Run the `:data` Phase 1 gate**

Run: `./gradlew :data:testDebugUnitTest --no-daemon`
Expected: PASS, including the focused Koin resolution tests for auth, user, media, review, medialist, and the `dataModules` aggregator guard.

- [ ] **Step 2: Run the `:app:core` startup gate**

Run: `./gradlew :app:core:testDebugUnitTest --no-daemon`
Expected: PASS, including `CoreModulesResolutionTest`.

- [ ] **Step 3: Run docs audit if any skill or instruction text changed while implementing**

Run: `.github/scripts/audit-instruction-refs.sh`
Expected: `No issues found.`

- [ ] **Step 4: Review final diff and create the Phase 1 implementation commit**

```bash
git status --short --branch
git diff --stat HEAD~1..HEAD
```

Then commit any final unstaged implementation changes with:

```bash
git add data/src/test/kotlin/co/anitrend/data/android/koin/DataModulesResolutionTest.kt app/core/src/test/kotlin/co/anitrend/core/koin/CoreModulesResolutionTest.kt data/src/test/kotlin/co/anitrend/data/user/koin/UserModulesTest.kt data/src/test/kotlin/co/anitrend/data/media/koin/MediaModulesTest.kt data/src/test/kotlin/co/anitrend/data/review/koin/ReviewModulesTest.kt data/src/test/kotlin/co/anitrend/data/medialist/koin/MediaListModulesTest.kt data/src/main/kotlin/co/anitrend/data/user/koin/Modules.kt data/src/main/kotlin/co/anitrend/data/media/koin/Modules.kt data/src/main/kotlin/co/anitrend/data/review/koin/Modules.kt data/src/main/kotlin/co/anitrend/data/medialist/koin/Modules.kt
git commit -m "test(data): add phase 1 koin resolution guards"
```

## Scope Notes

- Phase 1 does **not** attempt full repo-wide per-module Koin coverage for every `Modules.kt` file.
- Phase 1 focuses only on startup and authenticated flows where a broken factory blocks app startup, signed-in navigation, or central user/data rendering.
- The existing `data/src/androidTest/.../ModulesTest.kt` broad graph check stays as a secondary safety net; it is not sufficient on its own.
- If a module test reveals a repeated pattern of broad-interface lookup, prefer fixing the production module with explicit concrete binding requests rather than introducing generic catch-all bindings.

## Spec Coverage Check

- `app/core` startup graph coverage: handled by `CoreModulesResolutionTest`.
- `data/android` aggregator graph coverage: handled by `DataModulesResolutionTest`.
- Runtime-critical signed-in modules (`auth`, `user`, `media`, `review`, `medialist`): handled by focused `src/test` resolution tests.
- High-risk binding audit for broad interfaces/typealiases/`EmbedMapper`/`PersistEmbedded`: handled explicitly in Tasks 3-5 as each test fails red.
- Verification in normal `testDebugUnitTest`: handled in Task 6.
