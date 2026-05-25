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
2. Sheet renders variants from `MediaTheme.variants`.
3. User taps audio play on a preview row.
4. Sheet controller prepares Exo3 for selected URL and marks active key.
5. Active row receives playback ticks and state updates.
6. User seeks/pauses/resumes or switches to another preview.
7. Dismissing sheet releases player and clears active state.

## State Matrix

- Empty variants: metadata rows remain visible; no variant controls.
- Variant with no audio: row shows disabled audio action.
- Variant with audio loading: active row shows loading.
- Variant with audio playing: active row shows pause + live progress.
- Variant paused: active row keeps seek and time state.
- Playback error: active row shows retry.

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
2. Implement `ThemeAudioPreviewControls` with play/pause + seek + time.
3. Wire controls into variant rows with stable active key.
4. Enable video chooser intent and failure handling.
5. Add/extend unit tests.
6. Validate on device with Argent for collapsed, expanded, and open-sheet states.

## Phase 2 Placeholder (explicitly deferred)

- Migrate sheet-local controller to media-session-aware playback orchestration.
- Add background playback and transport notification.
- Preserve Phase 1 row interaction contract to minimize UI churn.
