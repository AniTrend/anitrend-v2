# Search Compose Refactor Design

## Goal

Break up `feature/search` Compose UI into focused files so future work has clear ownership boundaries without changing behavior or introducing a broader architecture rewrite.

## Approved Direction

Approved direction: **ownership-first split within the existing `component.compose` package**.

- Keep the package as `co.anitrend.search.component.compose`.
- Split the current oversized file into focused files by UI concern.
- Preserve existing navigation, state flow, previews, and behavior.
- Optimize for “where should this change go?” clarity, not just smaller file sizes.

## Current State

- `feature/search/src/main/kotlin/co/anitrend/search/component/compose/SearchCompose.kt` is a single large Compose file.
- The file currently mixes:
  - top-level screen orchestration
  - query and scope controls
  - generic loading/error/empty states
  - media section and drill-down UI
  - user section and drill-down UI
  - studio section and drill-down UI
  - staff section and drill-down UI
  - character section and drill-down UI
  - preview-only content
- `SearchScreen.kt`, `SearchViewModel.kt`, DI, and routing are already working and are not the target of this refactor.
- Existing unit coverage is centered on `SearchViewModelTest`, which should remain unchanged by this UI-only split.

## Target File Shape

Keep all files in `co.anitrend.search.component.compose`.

- `SearchScreenContent.kt`
  - top-level coordinator
  - scope switching
  - screen composition and callback threading
- `SearchChrome.kt`
  - search bar
  - scope chips
  - query-entry controls only
- `SearchState.kt`
  - generic loading, empty, and error UI
- `SearchMediaSections.kt`
  - media preview section(s)
  - media drill-down/list UI
- `SearchUserSections.kt`
  - user preview section
  - user drill-down/list UI
- `SearchStudioSections.kt`
  - studio preview section
  - studio drill-down/list UI
- `SearchStaffSections.kt`
  - staff preview section
  - staff drill-down/list UI
- `SearchCharacterSections.kt`
  - character preview section
  - character drill-down/list UI
- `SearchPreview.kt`
  - preview-only setup and preview entry points when that keeps production files leaner

## Responsibility Boundaries

### `SearchScreenContent.kt`

This file should stay thin and orchestration-focused.

It owns:

- collecting `SearchViewModel` state
- converting flows into `LazyPagingItems`
- deciding which section or drill-down composable to render for the active `SearchScope`
- passing navigation callbacks and shared dependencies downward

It does **not** own:

- domain-specific row/card rendering
- loading/error/empty surface implementation details
- per-domain UI helper internals

### Domain section files

Each domain file owns both:

- the home-screen preview section
- the full drill-down/list rendering for that same domain

This keeps ownership intuitive: if a change affects how a domain is rendered anywhere in search, it goes in that domain file.

### Shared files

Shared files stay intentionally narrow.

- `SearchChrome.kt` is only for search input and scope controls.
- `SearchState.kt` is only for reusable loading/error/empty surfaces.
- Helpers should move into shared files only when they are genuinely reused across two or more domain files.

## Design Rules

- Keep the same package to reduce visibility churn and import churn.
- Move code first; rename only where names are actively unclear.
- Keep callback shapes unchanged unless the split reveals a concrete readability issue.
- Do not use this refactor to redesign search behavior, navigation, or ViewModel contracts.
- Do not extract new cross-feature shared components as part of this change.
- Keep previews, but isolate preview-only wiring so production files stay focused.

## Migration Plan

1. Extract the top-level coordinator into `SearchScreenContent.kt`.
2. Move search bar and scope chip UI into `SearchChrome.kt`.
3. Move generic state surfaces into `SearchState.kt`.
4. Move media UI into `SearchMediaSections.kt`.
5. Move user, studio, staff, and character UI into their respective section files.
6. Move preview-only content into `SearchPreview.kt` if it materially reduces production-file clutter.
7. Remove the original monolithic file once all declarations are relocated cleanly.

The refactor should be behavior-preserving at every step.

## Verification

Primary verification targets:

- `feature/search` still compiles.
- Existing `SearchViewModelTest` passes unchanged.
- No user-facing behavior changes in:
  - scope switching
  - search submission
  - result rendering
  - drill-down rendering
  - retry/loading/empty states

Recommended verification commands:

- `./gradlew :feature:search:compileDebugKotlin`
- `./gradlew :feature:search:testDebugUnitTest --tests co.anitrend.search.component.viewmodel.SearchViewModelTest`

## Risks

- Shared helpers may be prematurely generalized during the split.
- A mechanical move could accidentally blur coordinator vs domain ownership if declarations are grouped by convenience instead of concern.
- Preview code can quietly drag production dependencies back into the wrong files if not isolated deliberately.

## Non-Goals

- No search UX redesign.
- No ViewModel contract rewrite.
- No new shared cross-feature search component library.
- No package restructuring into subpackages for this pass.
- No DI or routing rewrite.

## Success Criteria

- Future changes to search UI have an obvious destination file.
- `component.compose` remains the package, but no single file owns unrelated concerns.
- The coordinator file is visibly thinner and limited to orchestration.
- Each search domain owns its own preview + drill-down rendering.
- Existing behavior and tests remain intact.
