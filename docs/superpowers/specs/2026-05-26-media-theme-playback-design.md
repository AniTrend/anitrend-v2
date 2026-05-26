# Media Theme Playback Design

## Goal

Enable in-sheet audio preview playback for media theme variants using Exo3 in Compose, while enabling video preview via external intent chooser, without broad architecture churn.

## Scope

### In scope (Phase 1)

- Audio preview controls inside `MediaThemeDetailSheet`.
- Single active playback model (starting one preview stops current preview).
- Standard v1 controls: play/pause, seek, elapsed time, total duration, buffering and error state.
- Video action via external chooser (`Intent.ACTION_VIEW`).
- Sheet-scoped playback lifecycle (release on dismiss).

### Out of scope (Phase 2)

- Background playback.
- Media session integration.
- Notification and lockscreen transport controls.
- App-wide/global player orchestration.

## Existing Context

- Theme detail UI entry point: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
- Current enriched model path:
  - `domain/src/main/kotlin/co/anitrend/domain/media/entity/attribute/theme/MediaTheme.kt`
  - `data/src/main/kotlin/co/anitrend/data/media/converter/MediaEntityViewConverter.kt`
- Theme variants are already modeled (`MediaTheme.Variant`, `MediaTheme.Preview`) and mapped from edge data.

## UX Decisions (locked)

- Playback UI lives in the theme detail bottom sheet.
- Single active playback behavior.
- Video launches with external chooser via `ACTION_VIEW`.
- Controls level for audio v1 is Standard (play/pause + seek + elapsed/duration).
- Enhanced playback behavior is deferred to Phase 2.
- Bottom sheet direction is playback-first rather than metadata-first.
- First-screen hierarchy is locked to: hero player -> theme identity -> quick metadata -> variant list.
- Hero player is prominent, preselected, and ready to play, but does not autoplay.
- Hero player includes a lightweight variant switcher before play.
- Theme identity is song-first, with rich supporting context such as `Opening 1 • Performed by LiSA`.

## Architecture

### Ownership and lifecycle

- Playback state is owned by `MediaThemeDetailSheet` and does not escape sheet scope.
- A single sheet-level playback controller owns one Exo3 player instance.
- Row composables are pure renderers deriving state from sheet-level playback state + preview identity.
- Player is always released when sheet is dismissed.

### Identity model

- Active preview identity uses a stable composite key:
  - `themeId + variantVersion + previewVideoUrl`
- This prevents incorrect active-row rendering after recomposition or reorder.

### Playback state model

- `activePreviewKey: String?`
- `isPlaying: Boolean`
- `isBuffering: Boolean`
- `positionMs: Long`
- `durationMs: Long`
- `errorMessage: String?`
- `canSeek: Boolean` (derived)

### Control contract

- Play on inactive row -> stop current, prepare selected, set active, start.
- Play on active paused row -> resume.
- Pause on active playing row -> pause.
- Seek available only for active row with valid duration.
- Selecting a different row during playback performs a hard switch.

### Bottom-sheet hierarchy and interaction

- Hero player is the dominant first-screen affordance and should be usable without scrolling.
- The hero variant switcher lets users pick the initial preview before play, without autoplaying on selection.
- Theme identity sits directly under the hero player:
  - line 1: song title
  - line 2: rich context (`Opening 1 • Performed by LiSA`)
- Quick metadata remains compact and supportive, not primary.
- Variant rows are the deeper browse layer for alternate versions and episode coverage.
- Video actions stay at the row level, where exact version context is clearest.

### Row interaction UX contract

- Hero controls always operate on the currently selected preview.
- Pressing play on a row promotes that row's preview into the hero and starts playback.
- Active row and hero state must remain visually synchronized.
- Inactive rows show dormant controls only.
- Starting another preview hard-switches playback and updates both hero and row state.

### Error and loading behavior

- Buffering/prepare renders loading affordance on active row.
- Invalid/missing audio URL renders disabled state.
- Runtime player errors render per-row error hint and retry path.

### Video behavior

- Video action uses `Intent.ACTION_VIEW` wrapped in chooser.
- Blank/invalid URLs keep button disabled.
- `ActivityNotFoundException` is handled without crash and surfaced as a lightweight failure state.

## Component Decomposition

Primary file target:

- `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`

Add or refactor composables:

- `ThemeHeroPreviewCard(...)` (new playback-first hero surface)
- `ThemeIdentityBlock(...)` (new song-first identity block)
- `ThemeVariantRow(...)` (existing, remains container)
- `ThemeAudioPreviewControls(...)` (new)
- Small formatter helpers:
  - `formatDuration(ms): String` (`m:ss`)
  - preview key helper

Add sheet-local playback controller wrapper:

- Either local private class in the same file or sibling file under `section/theme/` if file size grows too much.
- Wrapper responsibilities only:
  - prepare/play/pause/seek/stop/switch
  - expose immutable UI state
  - release on dispose

## Data Flow

1. User opens theme detail sheet.
2. Sheet preselects a recommended preview into hero state without autoplay.
3. Sheet renders identity, quick metadata, and variants from `MediaTheme.variants`.
4. User either taps play in hero or promotes a row preview into active hero state.
5. Sheet controller prepares Exo3 for selected URL and marks active key.
6. Active hero and active row receive synchronized playback ticks and state updates.
7. User seeks, pauses, resumes, or switches preview via the hero switcher or a row action.
8. Dismissing sheet releases player and clears active state.

## ASCII High-Fidelity Mock

```text
[ Hero Preview Card ]
  [v1 Episodes 1-15 ▼]                     [Play]
  [------------- seek -------------]       0:00 / 1:30

[ Theme Identity ]
  Gurenge
  Opening 1 • Performed by LiSA

[ Quick Metadata ]
  [Stream-ready] [Watch-ready] [1080P WEB NC]

[ Theme Variants ]
  v1  Episodes 1-15
      [Audio preview]        [Watch video]

  v2  Episodes 16-21
      [Audio preview]        [Watch video]

  v3  Episodes 22
      [Unavailable]          [Watch video]
```

## Key UX and Interaction Decisions

- Playback-first stacked sheet is preferred because the primary user goal here is immediate media interaction, not metadata inspection.
- A prominent hero player removes one click between opening the sheet and starting playback.
- The lightweight hero variant switcher exposes choice early without turning the first screen into a wall of rows.
- Song-first identity is a better recognition anchor for AniTrend users than type-first identity.
- Video belongs at the row level because users need variant-specific context before leaving the app.
- Shared sheet-level playback state is the safest Compose model because it prevents multiple active players and hero/row desynchronization.

## Design Quality Gates

### Good looks like

- The top of the sheet makes playback feel immediate and obvious.
- The hero player is clearly the first action to notice.
- Theme identity supports playback rather than competing with it.
- Variant rows feel like a deeper browse layer with clear version context.
- Dark-theme surface layering stays readable and separated.

### Avoid this

- Leading with a wall of equally weighted rows before the primary playback action.
- Overcrowding the hero with every piece of metadata and action.
- Duplicating equal-weight play controls everywhere without a clear primary state.
- Hiding which preview is currently active.
- Letting metadata push the hero below the fold on common phone sizes.

### Contrast and readability risks

- Accent-heavy hero styling can overpower supporting metadata.
- The supporting identity line must remain legible at larger font scales.
- Row-level video actions must not visually outrank the hero player.

### Accessibility handoff notes

- Hero controls need clear semantics for selected preview, play/pause state, seek position, and duration.
- Active preview state must be communicated beyond color alone.
- Play, pause, seek, and variant switching require accessible touch targets.
- Text scaling must preserve title-first hierarchy without collapsing the supporting identity line.
- Disabled audio states need explicit unavailable messaging.

## State Matrix

- Opening idle: hero preview is preselected, but no autoplay occurs.
- Opening populated: identity and quick metadata are visible without scrolling.
- Empty variants: metadata rows remain visible; no variant controls.
- Variant with no audio: row shows disabled audio action.
- Variant with audio loading: active row shows loading.
- Variant with audio playing: active row shows pause + live progress.
- Variant paused: active row keeps seek and time state.
- Playback error: active row shows retry.
- Hero variant switch while idle: updates selected preview and active-row highlight without autoplay.
- Hero variant switch while playing: hard-switches playback and updates synchronized hero/row state.

## Testing Strategy

### Unit tests (Phase 1)

- Extend `MediaThemeSectionSupportTest` for:
  - preview key stability
  - duration formatter edge cases (`0`, `<1m`, `>1h` folded to minutes)
  - active/inactive row derived state mapping
- Add focused tests for controller state transitions:
  - play -> pause -> seek -> resume
  - switch source while playing
  - dismiss/release behavior
  - invalid source error path

### Verification commands

- `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.MediaThemeSectionSupportTest" --no-daemon`
- Add targeted controller tests under `:feature:media` and run those specific tests.

## Risks and Mitigations

- Recomposition churn from playback ticks:
  - Limit high-frequency updates to active row rendering path.
- Player leaks:
  - Enforce deterministic release in `DisposableEffect` tied to sheet lifecycle.
- Wrong active row after data reorder:
  - Use stable key, never list index.
- Seek jitter during drag:
  - Temporarily decouple slider drag state from incoming position ticks.

## Incremental Delivery Plan

1. Add sheet-level controller wrapper and immutable state.
2. Introduce `ThemeHeroPreviewCard(...)` and `ThemeIdentityBlock(...)` with preselected hero preview.
3. Implement `ThemeAudioPreviewControls` with play/pause + seek + time.
4. Wire hero switching and row promotion into stable active-key handling.
5. Enable video chooser intent and failure handling.
6. Add and extend unit tests.
7. Validate on device with Argent for collapsed, expanded, and open-sheet states.

## Phase 2 Placeholder (explicitly deferred)

- Migrate sheet-local controller to media-session-aware playback orchestration.
- Add background playback and transport notification.
- Preserve Phase 1 row interaction contract to minimize UI churn.
