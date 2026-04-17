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
package co.anitrend.episode.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asLocaleString
import co.anitrend.common.episode.ui.compose.EpisodeBrowseCard
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import co.anitrend.episode.R
import co.anitrend.episode.component.content.viewmodel.EpisodeContentViewModel

@Composable
fun EpisodeCompose(
    settings: ILocaleSettings,
    viewModel: EpisodeContentViewModel,
    onEpisodeClick: (Episode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale by settings.locale.flow.collectAsStateWithLifecycle(
        initialValue = settings.locale.value,
    )
    val episodes =
        remember(locale) {
            viewModel.episodes(EpisodeParam.Paged(locale.asLocaleString()))
        }.collectAsLazyPagingItems()
    val refreshState = episodes.loadState.refresh

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        when {
            episodes.itemCount > 0 ->
                EpisodeFeed(
                    episodes = episodes,
                    onEpisodeClick = onEpisodeClick,
                )

            refreshState is LoadState.Loading ->
                EpisodeState(
                    title = stringResource(R.string.label_episode_loading_title),
                    subtitle = stringResource(R.string.message_episode_loading),
                )

            refreshState is LoadState.Error ->
                EpisodeRetryState(
                    title = stringResource(R.string.label_episode_error_title),
                    subtitle = stringResource(R.string.message_episode_error),
                    actionLabel = stringResource(R.string.action_episode_retry),
                    onRetry = episodes::retry,
                )

            else ->
                EpisodeState(
                    title = stringResource(R.string.label_episode_empty_title),
                    subtitle = stringResource(R.string.message_episode_empty),
                )
        }
    }
}

@Composable
private fun EpisodeFeed(
    episodes: androidx.paging.compose.LazyPagingItems<Episode>,
    onEpisodeClick: (Episode) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(
            count = episodes.itemCount,
            key = episodes.itemKey { episode -> episode.id },
            contentType = episodes.itemContentType { "episode_card" },
        ) { index ->
            val episode = episodes[index] ?: return@items
            EpisodeBrowseCard(
                episode = episode,
                onClick = onEpisodeClick,
            )
        }

        when (episodes.loadState.append) {
            is LoadState.Loading -> {
                item(
                    key = "episode_append_loading",
                    contentType = "episode_append_loading",
                ) {
                    Text(
                        text = stringResource(R.string.message_episode_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item(
                    key = "episode_append_error",
                    contentType = "episode_append_error",
                ) {
                    EpisodeRetryState(
                        title = stringResource(R.string.label_episode_error_title),
                        subtitle = stringResource(R.string.message_episode_error),
                        actionLabel = stringResource(R.string.action_episode_retry),
                        onRetry = episodes::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun EpisodeState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EpisodeRetryState(
    title: String,
    subtitle: String,
    actionLabel: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onRetry) {
                Text(text = actionLabel)
            }
        }
    }
}
