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

        controller.updatePlaybackState(
            isPlaying = false,
            isBuffering = false,
        )

        val state = controller.uiState.value
        assertFalse(state.isPlaying)
        assertFalse(state.isBuffering)
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
    fun `playback controller can select another preview without starting playback`() {
        val player = RecordingPlayer()
        val controller = ThemePlaybackController(player)

        controller.select(
            ThemePlaybackRequest(
                previewKey = "theme-1:v2:https://cdn.example/audio-b.mp3",
                audioUrl = "https://cdn.example/audio-b.mp3",
                title = "Gurenge",
            ),
            playWhenSelected = false,
        )

        val state = controller.uiState.value
        assertEquals("theme-1:v2:https://cdn.example/audio-b.mp3", state.activePreviewKey)
        assertEquals("Gurenge", state.activeTitle)
        assertTrue(player.sourceHistory.isEmpty())
        assertFalse(state.isPlaying)
    }
}

private class RecordingPlayer : ThemePlaybackEngine {
    val sourceHistory = mutableListOf<String>()
    var playCalls = 0
    var pauseCalls = 0

    override fun setSource(url: String) {
        sourceHistory += url
    }

    override fun play() {
        playCalls += 1
    }

    override fun pause() {
        pauseCalls += 1
    }

    override fun seekTo(positionMs: Long) = Unit

    override fun release() = Unit
}
