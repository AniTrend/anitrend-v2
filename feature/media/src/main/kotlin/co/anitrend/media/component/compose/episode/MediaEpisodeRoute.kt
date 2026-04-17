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
package co.anitrend.media.component.compose.episode

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import co.anitrend.common.media.ui.compose.component.status.rememberMediaEpisodeGuideUiState
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.navigation.MediaRouter
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaEpisodeRoute(
    mediaId: Long,
    mediaType: MediaType,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    viewModel: MediaViewModel = koinViewModel(),
) {
    val media by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()
    val guideUiState = (media as? Media.Extended)?.let { rememberMediaEpisodeGuideUiState(it) }

    LaunchedEffect(mediaId, mediaType) {
        viewModel(
            MediaRouter.MediaParam(
                id = mediaId,
                type = mediaType,
            ),
        )
    }

    MediaEpisodeScreenContent(
        guideUiState = guideUiState,
        loadState = loadState,
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
        onRetry = {
            viewModel(
                MediaRouter.MediaParam(
                    id = mediaId,
                    type = mediaType,
                ),
            )
        },
    )
}
