# Search Blended Drill-Down Design

## Goal

Deliver a shippable Search experience for AniTrend using a blended home with drill-down screens, starting with the only fully wired entity (`media`) and expanding incrementally as other entity data spines become production-ready.

## Product Decision

Approved direction: **Blended + drill-down**.

- The main search screen shows capped cross-source sections.
- Each section has a `See all` path into a typed results surface.
- v1 ships `media`-first, with future phases for `user`, then `studio`, then `staff`/`character` after data readiness.

## Current State

- `feature/search` is currently a shell (`SearchPresenter`, placeholder compose, no ViewModel wiring).
- `SearchScreenContent` is manual scaffold + placeholder text.
- Only `media` has end-to-end search/paging wiring in data/domain layers.
- `user` is partially prepared.
- `studio`, `staff`, `character` are not ready for paged search in current wiring.

## Architecture

### Ownership

`feature/search` owns:

- query text and submit/debounce behavior
- screen-level filters/scope
- section ordering and blend policy
- section UI model mapping
- composing loading/empty/error/populated states

`feature/search` does **not** own:

- repositories
- remote/local data-source logic
- GraphQL contracts and query builders

### Data flow

`SearchScreen` -> `SearchViewModel` -> typed data interactors -> domain use cases/repositories -> data sources -> UI section models.

The feature composes typed streams; it does not implement a fake single cross-entity pager.

## UX and Compose System

### Screen shape

- Search input at top.
- Scope chips for media (`All`, `Anime`, `Manga`) in v1.
- Capped section previews with `See all` action.
- Drill-down route for typed media results.

### Reuse-first component plan

- Screen shell: `DefaultScaffold` from shared compose.
- Section shell: `MediaHubSection`.
- Blended media preview: `MediaCompactItem`.
- Media drill-down: `MediaPosterListItem` or `MediaPagedBrowseContent`.
- Filter/chip style: existing patterns from `MediaFilterCompose` and `TagComponent`.

### Design quality gates

- Avoid chip overload and equal visual weight across controls.
- Avoid over-layered translucent cards that flatten hierarchy on dark theme.
- Preserve metadata scanability in dense rows.
- Prefer capped preview + drill-down over many horizontal rails.

## State Matrix

The feature must model and render:

- idle (with optional route query)
- initial loading
- populated results
- fully empty results
- section-level error with retry
- partial success where one section fails and others succeed
- drill-down append loading/retry

## Delivery Plan

### Phase 1 (implementation target)

Media-first blended search:

- add `SearchViewModel` in `feature/search`
- wire Koin `viewModelModule`
- map `SearchRouter.SearchParam` to media query params
- render media section previews with `See all`
- add typed media drill-down path and state handling

### Phase 2

Add `user` section after missing data/user search spine wiring is implemented.

### Phase 3

Add `studio` section after paged search wiring exists.

### Phase 4

Add `staff` and `character` after full search spines exist; fix the character query fragment mismatch before enabling character search.

## Risks and Constraints

- `feature/search` currently has no ViewModel and no search state model.
- `user` is not fully wired in data/DI yet.
- `studio` is detail-only today.
- `staff` and `character` are not end-to-end search-ready.
- Existing media fragment payload may be heavy for blended search and may need later optimization.

## Acceptance Criteria for Phase 1

- Search screen is no longer placeholder-only.
- Query input updates visible media search sections.
- `All/Anime/Manga` media scopes are supported.
- Each media section supports drill-down and retry states.
- UI uses repo-standard scaffold/section patterns and supports dark/light preview coverage.
