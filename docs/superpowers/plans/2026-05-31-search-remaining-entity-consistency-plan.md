# Search Remaining Entity Consistency Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Fix non-deterministic search behavior for Characters/Studios/Staff and align Anime/Manga data + UI rendering with shared `common/media` patterns.

**Architecture:** Keep query orchestration in `feature/search` but normalize per-entity query filters, paging-source behavior, and scope-specific media params so each scope reflects the submitted query and selected media type. Replace custom media card rendering in search with shared media UI components from `common/media` to eliminate drift.

**Tech Stack:** Kotlin, Room, Paging 3, Compose, Koin, AniTrend common media UI

---

### Task 1: Lock down entity query determinism (Characters/Studios/Staff)

**Files:**
- Modify: `data/src/main/kotlin/co/anitrend/data/character/source/CharacterPagingSource.kt`
- Modify: `data/src/main/kotlin/co/anitrend/data/staff/entity/filter/StaffQueryFilter.kt`
- Modify: `data/src/main/kotlin/co/anitrend/data/studio/source/StudioPagingSource.kt`
- Modify: `data/src/main/kotlin/co/anitrend/data/staff/source/StaffPagingSource.kt` (if needed)
- Modify: `data/src/main/kotlin/co/anitrend/data/character/entity/filter/*` (if created/needed)
- Test: `data/src/test/kotlin/co/anitrend/data/character/**`
- Test: `data/src/test/kotlin/co/anitrend/data/staff/**`
- Test: `data/src/test/kotlin/co/anitrend/data/studio/**`

- [ ] Add failing tests proving repeated results are returned regardless of query (for each entity).
- [ ] Ensure each paging source uses query-aware SQL/filter objects (not `SELECT *` only paths).
- [ ] Ensure scope query changes invalidate/rebuild paging source.
- [ ] Re-run entity tests and verify distinct results for distinct queries.

### Task 2: Fix Anime/Manga scope contamination

**Files:**
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/viewmodel/SearchViewModel.kt`
- Modify: `data/src/main/kotlin/co/anitrend/data/media/entity/filter/MediaQueryFilter.kt`
- Modify: `data/src/main/kotlin/co/anitrend/data/media/source/MediaPagingSource.kt` (if applicable)
- Test: `feature/search/src/test/kotlin/co/anitrend/search/component/viewmodel/SearchViewModelTest.kt`
- Test: `data/src/test/kotlin/co/anitrend/data/media/entity/filter/MediaQueryFilterTest.kt`

- [ ] Add failing test: Anime scope must include only ANIME media type.
- [ ] Add failing test: Manga scope must include only MANGA media type.
- [ ] Verify `MediaParam.Find(type=...)` is preserved through filter + SQL.
- [ ] Verify SQL includes explicit media type predicate for anime/manga scope.

### Task 3: Unify search media UI with `common/media`

**Files:**
- Modify: `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt`
- Reuse from: `common/media/src/main/kotlin/co/anitrend/common/media/ui/compose/item/MediaCompactItem.kt`
- Reuse from: `common/media/src/main/kotlin/co/anitrend/common/media/ui/compose/item/MediaPosterListItem.kt`
- Test: `feature/search/src/test/kotlin/co/anitrend/search/component/compose/*` (if compose tests exist)

- [ ] Replace any custom media card variants in search with shared `MediaCompactItem`/`MediaPosterListItem` usage.
- [ ] Ensure same spacing, typography, score metadata, and image fallback behavior as `common/media`.
- [ ] Confirm no duplicated media card composables remain in search module.

### Task 4: End-to-end runtime verification matrix

**Files:**
- Verify runtime with Argent on `feature/search` screen

- [ ] Query `ani`: verify unique results per Users/Studios/Staff/Characters scopes.
- [ ] Query `bleach`: verify result set changes in each entity scope.
- [ ] Verify Anime and Manga tabs no longer show mixed types.
- [ ] Verify tap-through still works for media/user/studio/staff/character rows.

### Task 5: Quality gates + submission

**Files:**
- Modify tests as needed based on failures

- [ ] Run: `./gradlew :data:compileDebugKotlin :feature:search:compileDebugKotlin --no-daemon`
- [ ] Run: `./gradlew :feature:search:testDebugUnitTest --no-daemon`
- [ ] Run: targeted data tests for newly added query/filter cases
- [ ] Install and verify on emulator with Argent screenshots for all affected scopes
