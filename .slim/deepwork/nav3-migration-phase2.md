# Nav3 Migration Phase 2 — Deepwork Progress

Branch: `spike/nav3-about-runtime-only`

## Goal
Productionize the Navigation 3 spike: fix missing key entries, migrate real screens, wire deep links.

## Current State
- Working tree: clean (reverted fixer damage)
- `DrawerPlaceholder.kt` already exists in `app/src/main/kotlin/.../nav3/` (simple Box+Text, no Scaffold)
- `AniTrendNav3Host.kt`: Has entries for 9 of 17 NavKey types. 8 are missing from entryProvider.
- `MainComposeShellActivity.kt`: 7 drawer items

## Missing Entries in AniTrendNav3Host.kt entryProvider
These 8 NavKey types have NO entry registration:
- EpisodesNavKey, ReviewsNavKey, SuggestionsNavKey, ForumsNavKey
- AnimeListNavKey(userId), MangaListNavKey(userId), MediaNavKey(mediaId), ProfileNavKey(userId)

## Oracle Review (APPROVED with 4 revisions, all addressed)
R1: Defer parameterized keys from drawer ✓
R2: Replace Forum with Home (start key) ✓
R3: Use koinInject pattern ✓
R4: Per-provider tests ✓

## Revised Plan

### Phase 2a: Fix Missing Entries (DONE)
- [x] Enhance DrawerPlaceholder.kt: Scaffold + TopAppBar (ArrowBack) + title + "Coming soon"
- [x] Add entries for 8 missing NavKey types in AniTrendNav3Host (16/16 covered)
- [x] Add 5 drawer items: ImageViewer, Episodes, Reviews, Suggestions, Forums
- [x] Defer parameterized drawer items (AnimeList/MangaList need userId)
- [x] Verify: BUILD SUCCESSFUL, 3 files changed, no build files touched
- Commit: e0e88d9

### Phase 2b: Migrate Home + News + Episode (NEXT)
Follow established PHASE_2B_PATTERN.

## Architecture Constraints
- `feature/*` remain `runtimeOnly` from `app`
- `app:navigation` is pure contracts, no Compose compiler
- Koin bind: `org.koin.dsl.bind`
