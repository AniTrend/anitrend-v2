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
}

private class RecordingPlayer : ThemePlaybackEngine {
    val sourceHistory = mutableListOf<String>()
    var playCalls = 0

    override fun setSource(url: String) {
        sourceHistory += url
    }

    override fun play() {
        playCalls += 1
    }

    override fun pause() = Unit

    override fun seekTo(positionMs: Long) = Unit

    override fun release() = Unit
}
