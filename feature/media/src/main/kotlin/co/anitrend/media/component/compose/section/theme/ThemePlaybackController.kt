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
    val activeTitle: String? = null,
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
                activeTitle = request.title,
                isPlaying = true,
                isBuffering = true,
                errorMessage = null,
            )
    }

    fun updatePlaybackState(
        isPlaying: Boolean = mutableUiState.value.isPlaying,
        isBuffering: Boolean = mutableUiState.value.isBuffering,
    ) {
        mutableUiState.value =
            mutableUiState.value.copy(
                isPlaying = isPlaying,
                isBuffering = isBuffering,
            )
    }

    fun release() {
        engine.release()
        mutableUiState.value = ThemePlaybackUiState()
    }
}
