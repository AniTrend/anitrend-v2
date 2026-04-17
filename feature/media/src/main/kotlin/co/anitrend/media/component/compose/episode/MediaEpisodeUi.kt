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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeGuideUiState
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeItemUiState
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeListItemCard
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeProgressRow
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeSpotlightCard
import co.anitrend.common.media.ui.compose.component.status.MediaEpisodeVisualState
import co.anitrend.common.media.ui.compose.component.status.rememberMediaEpisodeGuideUiState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.Media
import co.anitrend.media.R
import co.anitrend.media.component.compose.MediaComposePreviewProvider
import co.anitrend.common.media.ui.R as MediaUiR

private data class EpisodeContentGroup(
    val title: String,
    val items: List<MediaEpisodeItemUiState>,
)

private const val EpisodeContextCollapsedMaxLines = 8
private const val EpisodeContextCharacterThreshold = 420
private const val EpisodeContextLineBreakThreshold = 4

@Composable
internal fun MediaEpisodeScreenContent(
    guideUiState: MediaEpisodeGuideUiState?,
    contextNote: String?,
    loadState: LoadState?,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    onRetry: () -> Unit,
) {
    val episodeContentGroups =
        buildEpisodeContentGroups(
            guideUiState = guideUiState,
            upcomingTitle = stringResource(R.string.label_media_episode_upcoming_heading),
            recentTitle = stringResource(R.string.label_media_episode_recent_heading),
            moreTitle = stringResource(R.string.label_media_episode_more_heading),
        )

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
                        MediaEpisodeSpotlightCard(
                            episode = it,
                            overviewMaxLines = 3,
                        )
                    }
                    guideUiState.progress?.let {
                        MediaEpisodeProgressRow(progress = it)
                    }
                    EpisodeTimelineContextSection(contextNote = contextNote)
                    episodeContentGroups.forEach { group ->
                        EpisodeGroupSection(group = group)
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

private fun String.shouldCollapseEpisodeContext(): Boolean =
    length > EpisodeContextCharacterThreshold ||
        count { it == '\n' } >= EpisodeContextLineBreakThreshold

@Composable
private fun EpisodeTimelineContextSection(
    contextNote: String?,
    modifier: Modifier = Modifier,
) {
    val content = contextNote?.trim().orEmpty()

    if (content.isBlank()) {
        return
    }

    val canExpand = content.shouldCollapseEpisodeContext()
    var isExpanded by rememberSaveable(content) {
        mutableStateOf(!canExpand)
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = CardDefaults.outlinedShape,
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f),
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.title_media_episode_context_notes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.subtitle_media_episode_context_notes),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = if (isExpanded) Int.MAX_VALUE else EpisodeContextCollapsedMaxLines,
                overflow = TextOverflow.Ellipsis,
            )

            if (canExpand) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                    ) {
                        Text(
                            text =
                                stringResource(
                                    if (isExpanded) {
                                        MediaUiR.string.action_media_synopsis_section_show_less
                                    } else {
                                        MediaUiR.string.action_media_synopsis_section_show_more
                                    },
                                ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeGroupSection(group: EpisodeContentGroup) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = group.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        group.items.forEach { item ->
            MediaEpisodeListItemCard(episode = item)
        }
    }
}

private fun buildEpisodeContentGroups(
    guideUiState: MediaEpisodeGuideUiState?,
    upcomingTitle: String,
    recentTitle: String,
    moreTitle: String,
): List<EpisodeContentGroup> {
    if (guideUiState == null) {
        return emptyList()
    }

    val upcomingItems =
        guideUiState.items
            .filter { it.state == MediaEpisodeVisualState.UPCOMING }
            .sortedWith(
                compareBy<MediaEpisodeItemUiState>({ it.airDate ?: Long.MAX_VALUE })
                    .thenBy { it.episodeNumber ?: Int.MAX_VALUE },
            )
    val recentItems =
        guideUiState.items
            .filter { it.state == MediaEpisodeVisualState.AIRED }
            .sortedWith(
                compareByDescending<MediaEpisodeItemUiState> { it.airDate ?: Long.MIN_VALUE }
                    .thenByDescending { it.episodeNumber ?: Int.MIN_VALUE },
            )
    val moreItems =
        guideUiState.items
            .filter { it.state == MediaEpisodeVisualState.UNKNOWN }
            .sortedWith(
                compareByDescending<MediaEpisodeItemUiState> { it.episodeNumber ?: Int.MIN_VALUE }
                    .thenByDescending { it.seasonNumber ?: Int.MIN_VALUE },
            )

    return buildList {
        if (upcomingItems.isNotEmpty()) {
            add(EpisodeContentGroup(title = upcomingTitle, items = upcomingItems))
        }
        if (recentItems.isNotEmpty()) {
            add(EpisodeContentGroup(title = recentTitle, items = recentItems))
        }
        if (moreItems.isNotEmpty()) {
            add(EpisodeContentGroup(title = moreTitle, items = moreItems))
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

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun MediaEpisodeScreenPreview(
    @PreviewParameter(MediaComposePreviewProvider::class) media: Media.Extended,
) {
    val guideUiState = rememberMediaEpisodeGuideUiState(media)

    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = guideUiState,
            contextNote = media.extraInfo,
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
            contextNote = null,
            loadState = LoadState.Loading(),
            mediaTitle = "Seasonal Test",
            onBackPress = {},
            onRetry = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaEpisodeScreenErrorPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = null,
            contextNote = null,
            loadState = LoadState.Error(details = IllegalStateException("Episode timeline preview failed")),
            mediaTitle = "Seasonal Test",
            onBackPress = {},
            onRetry = {},
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaEpisodeScreenEmptyPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = MediaEpisodeGuideUiState(spotlight = null, progress = null, items = emptyList()),
            contextNote = null,
            loadState = LoadState.Idle(),
            mediaTitle = "Seasonal Test",
            onBackPress = {},
            onRetry = {},
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun MediaEpisodeScreenLongContextPreview(
    @PreviewParameter(MediaComposePreviewProvider::class) media: Media.Extended,
) {
    val guideUiState = rememberMediaEpisodeGuideUiState(media)

    PreviewTheme(wrapInSurface = true) {
        MediaEpisodeScreenContent(
            guideUiState = guideUiState,
            contextNote =
                "Episode 492 is the second part of a two part crossover block, with the first half airing in Toriko's slot and the second in One Piece's. " +
                    "Later specials repeated the same scheduling pattern for multi-franchise events.\n\n" +
                    "Source: AniDB. The weekly broadcast moved from Wednesday evenings to Sunday evenings in the early 2000s, " +
                    "and special-event scheduling occasionally overrode the normal cadence for major crossover weeks.",
            loadState = null,
            mediaTitle = media.title.userPreferred?.toString(),
            onBackPress = {},
            onRetry = {},
        )
    }
}
