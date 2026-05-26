/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
        if (mutableUiState.value.activePreviewKey != request.previewKey) {
            engine.setSource(request.audioUrl)
        }
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

    fun pause() {
        engine.pause()
        mutableUiState.value =
            mutableUiState.value.copy(
                isPlaying = false,
                isBuffering = false,
            )
    }

    fun select(
        request: ThemePlaybackRequest,
        playWhenSelected: Boolean = mutableUiState.value.isPlaying,
    ) {
        if (playWhenSelected) {
            play(request)
        } else {
            mutableUiState.value =
                mutableUiState.value.copy(
                    activePreviewKey = request.previewKey,
                    activeTitle = request.title,
                    isPlaying = false,
                    isBuffering = false,
                    errorMessage = null,
                )
        }
    }

    fun toggle(request: ThemePlaybackRequest) {
        val activePreviewKey = mutableUiState.value.activePreviewKey
        if (mutableUiState.value.isPlaying && activePreviewKey == request.previewKey) {
            pause()
        } else {
            play(request)
        }
    }

    fun seekTo(positionMs: Long) {
        engine.seekTo(positionMs)
        mutableUiState.value = mutableUiState.value.copy(positionMs = positionMs)
    }

    fun updatePlayback(
        positionMs: Long = mutableUiState.value.positionMs,
        durationMs: Long = mutableUiState.value.durationMs,
        isBuffering: Boolean = mutableUiState.value.isBuffering,
    ) {
        mutableUiState.value =
            mutableUiState.value.copy(
                positionMs = positionMs,
                durationMs = durationMs,
                isBuffering = isBuffering,
            )
    }

    fun onError(message: String) {
        mutableUiState.value =
            mutableUiState.value.copy(
                isPlaying = false,
                isBuffering = false,
                errorMessage = message,
            )
    }

    fun release() {
        engine.release()
        mutableUiState.value = ThemePlaybackUiState()
    }
}
