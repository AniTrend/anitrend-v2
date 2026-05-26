package co.anitrend.media.component.compose.section.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ThemePlaybackControllerTest {

    @Test
    fun `playback controller promotes selected preview into active state and starts engine`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "Gurenge",
            ),
        )

        val state = controller.uiState.value
        assertEquals(listOf("https://cdn.example/audio.mp3"), player.sourceHistory)
        assertEquals(1, player.playCalls)
        assertEquals("theme-1:v1:https://cdn.example/audio.mp3", state.activePreviewKey)
        assertEquals("Gurenge", state.activeTitle)
        assertTrue(state.isPlaying)
        assertTrue(state.isBuffering)
        assertNull(state.errorMessage)
    }

    @Test
    fun `playback controller updates runtime playback flags from engine events`() {
        val controller = ThemePlaybackController(RecordingPlayer())

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "Gurenge",
            ),
        )

        controller.updatePlayback(
            positionMs = 12_000L,
            durationMs = 96_000L,
            isBuffering = false,
        )

        val state = controller.uiState.value
        assertTrue(state.isPlaying)
        assertFalse(state.isBuffering)
        assertEquals(12_000L, state.positionMs)
        assertEquals(96_000L, state.durationMs)
    }

    @Test
    fun `playback controller pauses active preview through intent api`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "Gurenge",
            ),
        )

        controller.pause()

        val state = controller.uiState.value
        assertEquals(1, player.pauseCalls)
        assertFalse(state.isPlaying)
        assertFalse(state.isBuffering)
    }

    @Test
    fun `playback controller switches sources only when a different preview is selected`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio-a.mp3",
                audioUrl = "https://cdn.example/audio-a.mp3",
                title = "A",
            ),
        )
        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio-a.mp3",
                audioUrl = "https://cdn.example/audio-a.mp3",
                title = "A",
            ),
        )
        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v2:https://cdn.example/audio-b.mp3",
                audioUrl = "https://cdn.example/audio-b.mp3",
                title = "B",
            ),
        )

        assertEquals(
            listOf("https://cdn.example/audio-a.mp3", "https://cdn.example/audio-b.mp3"),
            player.sourceHistory,
        )
        assertEquals("theme-1:v2:https://cdn.example/audio-b.mp3", controller.uiState.value.activePreviewKey)
    }

    @Test
    fun `seek and error transitions update playback state`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "Gurenge",
            ),
        )

        controller.seekTo(positionMs = 42_000L)
        controller.onError(message = "Unable to open preview")

        val state = controller.uiState.value
        assertEquals(42_000L, player.seekPositionMs)
        assertEquals(42_000L, state.positionMs)
        assertFalse(state.isPlaying)
        assertFalse(state.isBuffering)
        assertEquals("Unable to open preview", state.errorMessage)
    }

    @Test
    fun `release clears active playback state and releases engine`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.play(
            ThemePlaybackRequest(
                previewKey = "theme-1:v1:https://cdn.example/audio.mp3",
                audioUrl = "https://cdn.example/audio.mp3",
                title = "A",
            ),
        )
        controller.updatePlayback(
            positionMs = 12_000L,
            durationMs = 96_000L,
            isBuffering = true,
        )

        controller.release()

        val state = controller.uiState.value
        assertEquals(1, player.releaseCalls)
        assertNull(state.activePreviewKey)
        assertNull(state.activeTitle)
        assertFalse(state.isPlaying)
        assertFalse(state.isBuffering)
        assertEquals(0L, state.positionMs)
        assertEquals(0L, state.durationMs)
    }
}

private class RecordingPlayer : ThemePlaybackEngine {
    val sourceHistory = mutableListOf<String>()
    var playCalls = 0
    var pauseCalls = 0
    var seekPositionMs = 0L
    var releaseCalls = 0

    override fun setSource(url: String) {
        sourceHistory += url
    }

    override fun play() {
        playCalls += 1
    }

    override fun pause() {
        pauseCalls += 1
    }

    override fun seekTo(positionMs: Long) {
        seekPositionMs = positionMs
    }

    override fun release() {
        releaseCalls += 1
    }
}
