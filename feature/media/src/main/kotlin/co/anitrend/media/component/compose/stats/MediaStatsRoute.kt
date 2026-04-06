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
package co.anitrend.media.component.compose.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import co.anitrend.media.component.viewmodel.MediaStatsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MediaStatsRoute(
    mediaId: Long,
    mediaTitle: String?,
    averageScore: Int?,
    favourites: Int?,
    popularity: Int?,
    trendRank: Int?,
    onBackPress: () -> Unit,
    viewModel: MediaStatsViewModel = koinViewModel(),
) {
    val stats by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()
    val summarySnapshot =
        remember(averageScore, favourites, popularity, trendRank) {
            buildMediaStatsSummarySnapshot(
                averageScore = averageScore,
                favourites = favourites,
                popularity = popularity,
                trendRank = trendRank,
            )
        }

    LaunchedEffect(mediaId) {
        viewModel(mediaId)
    }

    MediaStatsScreenContent(
        summarySnapshot = summarySnapshot,
        stats = stats,
        loadState = loadState,
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
        onRetry = { viewModel(mediaId) },
    )
}
