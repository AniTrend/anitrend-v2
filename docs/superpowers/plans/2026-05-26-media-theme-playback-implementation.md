# Media Theme Playback Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Enable playback-first media theme previews in the detail bottom sheet with in-sheet Exo3 audio controls and external video intent handling, while preserving the current AniTrend visual language.

**Architecture:** Keep playback ownership local to `MediaThemeDetailSheet` via one sheet-scoped controller that manages a single Media3 player instance and exposes immutable UI state to hero and row composables. Use the already-enriched `MediaTheme.Variant` and `MediaTheme.Preview` model path so the UI remains data-driven, with hero and row state synchronized by a stable preview key.

**Tech Stack:** Jetpack Compose Material3, AndroidX Media3 ExoPlayer, Android `Intent.ACTION_VIEW`, Kotlin coroutines/state, existing `AniTrendPreview` preview matrix, JUnit support tests.

---

## File Structure

**Files to modify**
- `gradle/libs.versions.toml`
  Adds Media3 aliases so the feature module can depend on the player without hard-coded coordinates.
- `feature/media/build.gradle.kts`
  Wires the Media3 dependencies into `:feature:media` only.
- `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
  Integrates the sheet-level controller, hero card, identity block, active-row controls, chooser video action, and release lifecycle.
- `feature/media/src/main/kotlin/co/anitrend/media/component/compose/MediaComposePreviewProvider.kt`
  Expands preview fixtures so the playback-first sheet can be reviewed in Compose previews before runtime.
- `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt`
  Extends support coverage for preview key selection, duration formatting, and derived hero/row UI state.

**Files to create**
- `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackController.kt`
  Focused playback wrapper around a single Media3 player instance and immutable `ThemePlaybackUiState`.
- `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt`
  State-transition tests for play, pause, seek, switch, error, and release behavior.

**Files to review while implementing**
- `docs/superpowers/specs/2026-05-26-media-theme-playback-design.md`
- `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
- `domain/src/main/kotlin/co/anitrend/domain/media/entity/attribute/theme/MediaTheme.kt`

---

### Task 1: Add Playback Dependencies And Controller Test Scaffold

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `feature/media/build.gradle.kts`
- Create: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackController.kt`
- Create: `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt`

- [ ] **Step 1: Write the failing controller test**

```kotlin
package co.anitrend.media.component.compose.section.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ThemePlaybackControllerTest {

    @Test
    fun `playback controller promotes selected preview into active state`() {
        val controller = ThemePlaybackController(fakePlayer())

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "Gurenge",
            ),
        )

        val state = controller.uiState.value
        assertEquals("theme-1:v1:https://cdn.example/audio.mp3", state.activePreviewKey)
        assertFalse(state.errorMessage != null)
    }
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.theme.ThemePlaybackControllerTest" --no-daemon`

Expected: FAIL with unresolved references for `ThemePlaybackController`, `ThemePlaybackRequest`, and `fakePlayer`.

- [ ] **Step 3: Add minimal Media3 dependency aliases and module wiring**

```toml
# gradle/libs.versions.toml
[versions]
androidx-media3 = "1.8.0"

[libraries]
androidx-media3-exoplayer = { module = "androidx.media3:media3-exoplayer", version.ref = "androidx-media3" }
androidx-media3-ui = { module = "androidx.media3:media3-ui", version.ref = "androidx-media3" }
```

```kotlin
// feature/media/build.gradle.kts
dependencies {
    implementation(project(Libraries.AniTrend.CommonUi.character))
    implementation(project(Libraries.AniTrend.CommonUi.staff))
    implementation(project(Libraries.AniTrend.CommonUi.media))
    implementation(project(Libraries.AniTrend.CommonUi.review))
    implementation(project(Libraries.AniTrend.CommonUi.shared))
    implementation(project(Libraries.AniTrend.CommonUi.genre))
    implementation(project(Libraries.AniTrend.CommonUi.tag))
    implementation(project(Libraries.AniTrend.CommonUi.markdown))
    implementation(libs.androidx.browser)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.paging.compose)
}
```

- [ ] **Step 4: Add the minimal controller shape to satisfy the test**

```kotlin
package co.anitrend.media.component.compose.section.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ThemePlaybackRequest(
    val previewKey: String,
    val audioUrl: String,
    val title: String,
)

data class ThemePlaybackUiState(
    val activePreviewKey: String? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val errorMessage: String? = null,
)

interface ThemePlaybackEngine {
    fun setSource(url: String)
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun release()
}

class ThemePlaybackController(
    private val engine: ThemePlaybackEngine,
) {
    private val mutableUiState = MutableStateFlow(ThemePlaybackUiState())
    val uiState: StateFlow<ThemePlaybackUiState> = mutableUiState.asStateFlow()

    fun play(request: ThemePlaybackRequest) {
        engine.setSource(request.audioUrl)
        engine.play()
        mutableUiState.value =
            mutableUiState.value.copy(
                activePreviewKey = request.previewKey,
                isPlaying = true,
                isBuffering = true,
                errorMessage = null,
            )
    }
}
```

```kotlin
// test helper inside ThemePlaybackControllerTest.kt
private fun fakePlayer() =
    object : ThemePlaybackEngine {
        override fun setSource(url: String) = Unit
        override fun play() = Unit
        override fun pause() = Unit
        override fun seekTo(positionMs: Long) = Unit
        override fun release() = Unit
    }
```

- [ ] **Step 5: Run test to verify it passes**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.theme.ThemePlaybackControllerTest" --no-daemon`

Expected: PASS for `playback controller promotes selected preview into active state`.

- [ ] **Step 6: Commit**

```bash
git add gradle/libs.versions.toml feature/media/build.gradle.kts feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackController.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt
git commit -m "feat(media): scaffold theme playback controller"
```

### Task 2: Wire Sheet-Scoped Playback State And Playback-First Hero Layout

**Files:**
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/MediaComposePreviewProvider.kt`
- Test: `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt`

- [ ] **Step 1: Write the failing support test for preview key and hero selection helpers**

```kotlin
@Test
fun `preferredPreview selects first playable variant preview and builds stable key`() {
    val theme =
        theme(
            themeId = "theme-1",
            variants =
                listOf(
                    MediaTheme.Variant(
                        version = 1,
                        episodes = "1-15",
                        previews = listOf(MediaTheme.Preview(video = "", audio = "https://cdn.example/audio.mp3", resolution = 1080, source = "web")),
                    ),
                ),
        )

    val selection = theme.preferredPreviewSelection()

    assertEquals("theme-1:v1:", selection?.previewKey?.substringBefore("https://"))
    assertEquals("1-15", selection?.variant?.episodes)
}
```

- [ ] **Step 2: Run test to verify it fails**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.MediaThemeSectionSupportTest" --no-daemon`

Expected: FAIL with unresolved references for `preferredPreviewSelection` or mismatched helper API.

- [ ] **Step 3: Add minimal helper and layout code for playback-first sheet structure**

```kotlin
private data class ThemePreviewSelection(
    val variant: MediaTheme.Variant,
    val preview: MediaTheme.Preview,
    val previewKey: String,
)

internal fun MediaTheme.preferredPreviewSelection(): ThemePreviewSelection? =
    variants.firstNotNullOfOrNull { variant ->
        variant.previews.firstOrNull { !it.audio.isNullOrBlank() }?.let { preview ->
            ThemePreviewSelection(
                variant = variant,
                preview = preview,
                previewKey = "$themeId:v${variant.version}:${preview.video}",
            )
        }
    }

@Composable
private fun ThemeIdentityBlock(
    theme: MediaTheme,
    selection: ThemePreviewSelection?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = theme.name, style = MaterialTheme.typography.titleLarge)
        Text(
            text = buildString {
                append(theme.metaBadgeLabel().orEmpty())
                selection?.variant?.episodes?.takeIf(String::isNotBlank)?.let {
                    if (isNotEmpty()) append(" • ")
                    append("Episodes ")
                    append(it)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ThemeHeroPreviewCard(
    selection: ThemePreviewSelection?,
    state: ThemePlaybackUiState,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = selection?.variant?.variantLabel() ?: "No preview", style = MaterialTheme.typography.labelLarge)
            ThemeAudioPreviewControls(
                isPlaying = state.isPlaying,
                isBuffering = state.isBuffering,
                elapsedText = formatDuration(state.positionMs),
                durationText = formatDuration(state.durationMs),
                onPlayPauseClick = onPlayPauseClick,
            )
        }
    }
}
```

- [ ] **Step 4: Thread hero state into the bottom sheet with the current controller stub**

```kotlin
val preferredSelection = remember(theme) { theme.preferredPreviewSelection() }
val controller = remember { ThemePlaybackController(androidThemePlaybackEngine(LocalContext.current)) }
val playbackState by controller.uiState.collectAsStateWithLifecycle()

DisposableEffect(controller) {
    onDispose { controller.release() }
}

ThemeHeroPreviewCard(
    selection = preferredSelection,
    state = playbackState,
    onPlayPauseClick = {
        preferredSelection?.let { selection ->
            controller.play(
                ThemePlaybackRequest(
                    previewKey = selection.previewKey,
                    audioUrl = selection.preview.audio.orEmpty(),
                    title = theme.name,
                ),
            )
        }
    },
)

ThemeIdentityBlock(theme = theme, selection = preferredSelection)
```

- [ ] **Step 5: Run test to verify it passes**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.MediaThemeSectionSupportTest" --no-daemon`

Expected: PASS for the new preferred-preview helper test and all previously passing helper tests.

- [ ] **Step 6: Commit**

```bash
git add feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt feature/media/src/main/kotlin/co/anitrend/media/component/compose/MediaComposePreviewProvider.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt
git commit -m "feat(media): add playback-first theme sheet layout"
```

### Task 3: Implement Real Controller Transitions, Inline Row Controls, And Video Chooser

**Files:**
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackController.kt`
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
- Test: `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt`
- Test: `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt`

- [ ] **Step 1: Add the next failing controller tests for pause, switch, and release**

```kotlin
@Test
fun `playback controller switches sources when a different preview is selected`() {
    val engine = recordingPlayer()
    val controller = ThemePlaybackController(engine)

    controller.play(ThemePlaybackRequest("theme-1:v1:https://cdn.example/audio-a.mp3", "https://cdn.example/audio-a.mp3", "A"))
    controller.play(ThemePlaybackRequest("theme-1:v2:https://cdn.example/audio-b.mp3", "https://cdn.example/audio-b.mp3", "B"))

    assertEquals(listOf("https://cdn.example/audio-a.mp3", "https://cdn.example/audio-b.mp3"), engine.sources)
    assertEquals("theme-1:v2:https://cdn.example/audio-b.mp3", controller.uiState.value.activePreviewKey)
}

@Test
fun `release clears active playback state`() {
    val controller = ThemePlaybackController(fakePlayer())
    controller.play(ThemePlaybackRequest("theme-1:v1:https://cdn.example/audio.mp3", "https://cdn.example/audio.mp3", "A"))

    controller.release()

    assertEquals(null, controller.uiState.value.activePreviewKey)
    assertFalse(controller.uiState.value.isPlaying)
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.theme.ThemePlaybackControllerTest" --no-daemon`

Expected: FAIL because the controller does not yet model switching, pause, or release cleanup.

- [ ] **Step 3: Implement the real controller transitions and engine adapter**

```kotlin
class ThemePlaybackController(
    private val engine: ThemePlaybackEngine,
) {
    private val mutableUiState = MutableStateFlow(ThemePlaybackUiState())
    val uiState: StateFlow<ThemePlaybackUiState> = mutableUiState.asStateFlow()

    fun play(request: ThemePlaybackRequest) {
        if (mutableUiState.value.activePreviewKey != request.previewKey) {
            engine.setSource(request.audioUrl)
        }
        engine.play()
        mutableUiState.value =
            mutableUiState.value.copy(
                activePreviewKey = request.previewKey,
                isPlaying = true,
                isBuffering = true,
                errorMessage = null,
            )
    }

    fun pause() {
        engine.pause()
        mutableUiState.value = mutableUiState.value.copy(isPlaying = false, isBuffering = false)
    }

    fun seekTo(positionMs: Long) {
        engine.seekTo(positionMs)
        mutableUiState.value = mutableUiState.value.copy(positionMs = positionMs)
    }

    fun updatePlayback(positionMs: Long, durationMs: Long, isBuffering: Boolean) {
        mutableUiState.value =
            mutableUiState.value.copy(
                positionMs = positionMs,
                durationMs = durationMs,
                isBuffering = isBuffering,
            )
    }

    fun onError(message: String) {
        mutableUiState.value = mutableUiState.value.copy(isPlaying = false, isBuffering = false, errorMessage = message)
    }

    fun release() {
        engine.release()
        mutableUiState.value = ThemePlaybackUiState()
    }
}
```

```kotlin
private fun openVideoPreview(context: Context, url: String): Boolean =
    runCatching {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    }.getOrElse {
        false
    }
```

- [ ] **Step 4: Replace disabled buttons with real hero/row control wiring**

```kotlin
@Composable
private fun ThemeAudioPreviewControls(
    isPlaying: Boolean,
    isBuffering: Boolean,
    elapsedText: String,
    durationText: String,
    onPlayPauseClick: () -> Unit,
    onSeek: ((Float) -> Unit)? = null,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        FilledTonalButton(onClick = onPlayPauseClick, modifier = Modifier.fillMaxWidth(), enabled = !isBuffering) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(if (isPlaying) stringResource(R.string.action_media_theme_sheet_pause_audio) else stringResource(R.string.action_media_theme_sheet_play_audio))
        }
        Slider(
            value = elapsedTextToSliderValue(elapsedText, durationText),
            onValueChange = { value -> onSeek?.invoke(value) },
            enabled = onSeek != null,
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(elapsedText, style = MaterialTheme.typography.labelMedium)
            Text(durationText, style = MaterialTheme.typography.labelMedium)
        }
    }
}
```

```kotlin
ThemeVariantRow(
    variant = variant,
    isActive = playbackState.activePreviewKey == selection.previewKey,
    onPlayPreview = {
        controller.play(
            ThemePlaybackRequest(
                previewKey = selection.previewKey,
                audioUrl = selection.preview.audio.orEmpty(),
                title = theme.name,
            ),
        )
    },
    onOpenVideo = {
        if (!openVideoPreview(LocalContext.current, selection.preview.video)) {
            controller.onError("Unable to open video preview")
        }
    },
)
```

- [ ] **Step 5: Run tests to verify they pass**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.theme.ThemePlaybackControllerTest" --no-daemon`

Expected: PASS for controller transitions, including switch and release.

- [ ] **Step 6: Commit**

```bash
git add feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackController.kt feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt
git commit -m "feat(media): enable theme preview playback controls"
```

### Task 4: Preview Validation, Compose Cleanup, And Device Verification

**Files:**
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/MediaComposePreviewProvider.kt`
- Modify: `feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt`
- Test: `feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt`

- [ ] **Step 1: Add the failing support tests for duration formatting and row-state derivation**

```kotlin
@Test
fun `formatDuration renders m:ss and unknown duration`() {
    assertEquals("0:00", formatDuration(0L))
    assertEquals("1:05", formatDuration(65_000L))
    assertEquals("--:--", formatDuration(-1L))
}

@Test
fun `previewRowState marks only matching key active`() {
    val active = previewRowState(activePreviewKey = "theme-1:v1:https://cdn.example/audio.mp3", previewKey = "theme-1:v1:https://cdn.example/audio.mp3", isPlaying = true)
    val inactive = previewRowState(activePreviewKey = "theme-1:v1:https://cdn.example/audio.mp3", previewKey = "theme-1:v2:https://cdn.example/audio-b.mp3", isPlaying = true)

    assertEquals(true, active.isActive)
    assertEquals(false, inactive.isActive)
}
```

- [ ] **Step 2: Run tests to verify they fail**

Run: `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.MediaThemeSectionSupportTest" --no-daemon`

Expected: FAIL until `formatDuration` and row-state helper semantics match the test expectations.

- [ ] **Step 3: Finalize preview fixtures and helper polish**

```kotlin
internal fun formatDuration(ms: Long): String {
    if (ms < 0L) return "--:--"
    val totalSeconds = ms / 1000L
    val minutes = totalSeconds / 60L
    val seconds = totalSeconds % 60L
    return "%d:%02d".format(minutes, seconds)
}

private data class PreviewRowState(
    val isActive: Boolean,
    val canSeek: Boolean,
    val hasVideo: Boolean,
)

internal fun previewRowState(
    activePreviewKey: String?,
    previewKey: String,
    isPlaying: Boolean,
): PreviewRowState =
    PreviewRowState(
        isActive = activePreviewKey == previewKey,
        canSeek = activePreviewKey == previewKey && isPlaying,
        hasVideo = previewKey.isNotBlank(),
    )
```

```kotlin
// MediaComposePreviewProvider.kt
MediaTheme(
    mediaId = "1",
    themeId = "theme-1",
    name = "Gurenge",
    audio = "https://cdn.example/audio.mp3",
    video = "https://cdn.example/video.webm",
    meta = MediaTheme.Meta(number = 1, type = "OP", version = 1),
    variants =
        listOf(
            MediaTheme.Variant(
                version = 1,
                episodes = "1-15",
                previews = listOf(MediaTheme.Preview(video = "https://cdn.example/video.webm", audio = "https://cdn.example/audio.mp3", resolution = 1080, source = "web", tags = listOf("NC"))),
            ),
        ),
)
```

- [ ] **Step 4: Run the full targeted verification set**

Run:
- `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.MediaThemeSectionSupportTest" --no-daemon`
- `rtk gradlew :feature:media:testDebugUnitTest --tests "co.anitrend.media.component.compose.section.theme.ThemePlaybackControllerTest" --no-daemon`
- `rtk gradlew :data:testDebugUnitTest --tests "co.anitrend.data.core.api.converter.AniTrendConverterFactoryTest" --no-daemon`

Expected: PASS for all three targeted commands with no new warnings from `:feature:media` sources.

- [ ] **Step 5: Validate on device with Argent**

Run this exact sequence after installing the latest debug build:

```bash
rtk gradlew :app:installGoogleDebug --no-daemon
```

Then validate these states on `emulator-5554`:
- Open a theme detail sheet and capture the idle hero state.
- Start hero playback and verify play/pause + time updates.
- Switch to another variant and verify the old preview stops.
- Tap a row video action and verify chooser launch.
- Dismiss the sheet, reopen it, and verify playback state resets.

Expected: one active preview only, chooser opens without crash, and sheet dismiss releases playback state.

- [ ] **Step 6: Commit**

```bash
git add feature/media/src/main/kotlin/co/anitrend/media/component/compose/MediaComposePreviewProvider.kt feature/media/src/main/kotlin/co/anitrend/media/component/compose/section/MediaThemeSection.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/MediaThemeSectionSupportTest.kt feature/media/src/test/kotlin/co/anitrend/media/component/compose/section/theme/ThemePlaybackControllerTest.kt
git commit -m "test(media): validate theme playback states"
```
