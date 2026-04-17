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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeGuideUiState
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeListItemCard
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeProgressRow
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeSpotlightCard
import co.anitrend.common.media.ui.compose.component.status.rememberMediaEpisodeGuideUiState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.Media
import co.anitrend.media.R
import co.anitrend.media.component.compose.MediaComposePreviewProvider

@Composable
internal fun MediaEpisodeScreenContent(
    guideUiState: MediaEpisodeGuideUiState?,
    loadState: LoadState?,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    onRetry: () -> Unit,
) {
    EpisodeScreenScaffold(
        title = stringResource(R.string.title_media_episode_screen),
        subtitle = stringResource(R.string.subtitle_media_episode_screen),
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
    ) {
        when {
            guideUiState != null && (guideUiState.spotlight != null || guideUiState.items.isNotEmpty() || guideUiState.progress != null) -> {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    guideUiState.spotlight?.let {
                        MediaEpisodeSpotlightCard(episode = it)
                    }
                    guideUiState.progress?.let {
                        MediaEpisodeProgressRow(progress = it)
                    }
                    if (guideUiState.items.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = stringResource(R.string.label_media_episode_list_heading),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                            guideUiState.items.forEach { item ->
                                MediaEpisodeListItemCard(episode = item)
                            }
                        }
                    }
                }
            }

            loadState is LoadState.Error -> {
                EpisodeCenteredState(
                    title = stringResource(R.string.label_media_episode_error_title),
                    subtitle = stringResource(R.string.message_media_episode_error),
                    actionLabel = stringResource(co.anitrend.core.R.string.label_text_action_retry),
                    onAction = onRetry,
                )
            }

            loadState == null || loadState is LoadState.Loading -> {
                EpisodeLoadingState(
                    title = stringResource(R.string.label_media_episode_loading),
                    subtitle = stringResource(R.string.message_media_episode_loading),
                )
            }

            else -> {
                EpisodeCenteredState(
                    title = stringResource(R.string.label_media_episode_empty_title),
                    subtitle = stringResource(R.string.message_media_episode_empty),
                )
            }
        }
    }
}

@Composable
private fun EpisodeScreenScaffold(
    title: String,
    subtitle: String,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    content: @Composable () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                mediaTitle
                    ?.takeIf(String::isNotBlank)
                    ?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Composable
private fun EpisodeLoadingState(
    title: String,
    subtitle: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressIndicator()
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
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
private fun EpisodeCenteredState(
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!actionLabel.isNullOrBlank() && onAction != null) {
                OutlinedButton(onClick = onAction) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaEpisodeScreenPreview(
    @PreviewParameter(MediaComposePreviewProvider::class) media: Media.Extended,
) {
    val guideUiState = remember(media) { media }.let { rememberMediaEpisodeGuideUiState(it) }

    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = guideUiState,
            loadState = null,
            mediaTitle = media.title.userPreferred?.toString(),
            onBackPress = {},
            onRetry = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaEpisodeScreenLoadingPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = null,
            loadState = LoadState.Loading(),
            mediaTitle = "Seasonal Test",
            onBackPress = {},
            onRetry = {},
        )
    }
}
