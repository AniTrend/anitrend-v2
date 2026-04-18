/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.updater.component.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.data.core.app.IAppInfo

enum class UpdateChannel {
    STABLE,
    BETA,
    EXPERIMENTAL,
}

enum class UpdateCheckState {
    IDLE,
    MANUAL_ONLY,
}

data class UpdateUiState(
    val appLabel: String,
    val version: String,
    val build: String,
    val source: String,
    val code: String,
    val locale: String,
    val buildType: String,
    val selectedChannel: UpdateChannel = UpdateChannel.STABLE,
    val autoCheckEnabled: Boolean = true,
    val includePreviewBuilds: Boolean = false,
    val checkState: UpdateCheckState = UpdateCheckState.IDLE,
)

class UpdateViewModel(
    override val savedStateHandle: SavedStateHandle,
    private val appInfo: IAppInfo,
) : AniTrendViewModel() {
    var uiState by mutableStateOf(appInfo.asUiState())
        private set

    fun selectChannel(channel: UpdateChannel) {
        uiState =
            uiState.copy(
                selectedChannel = channel,
                includePreviewBuilds = uiState.includePreviewBuilds || channel != UpdateChannel.STABLE,
            )
    }

    fun setAutoCheckEnabled(enabled: Boolean) {
        uiState = uiState.copy(autoCheckEnabled = enabled)
    }

    fun setIncludePreviewBuilds(enabled: Boolean) {
        uiState = uiState.copy(includePreviewBuilds = enabled)
    }

    fun checkForUpdates() {
        uiState = uiState.copy(checkState = UpdateCheckState.MANUAL_ONLY)
    }
}

private fun IAppInfo.asUiState(): UpdateUiState =
    UpdateUiState(
        appLabel = label,
        version = version,
        build = build,
        source = source,
        code = code,
        locale = locale,
        buildType = buildType,
    )
