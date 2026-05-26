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

private fun fakePlayer() =
    object : ThemePlaybackEngine {
        override fun setSource(url: String) = Unit
        override fun play() = Unit
        override fun pause() = Unit
        override fun seekTo(positionMs: Long) = Unit
        override fun release() = Unit
    }
