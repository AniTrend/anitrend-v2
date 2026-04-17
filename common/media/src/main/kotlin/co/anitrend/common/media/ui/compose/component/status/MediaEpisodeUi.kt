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
package co.anitrend.common.media.ui.compose.component.status

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.common.media.ui.R
import co.anitrend.domain.media.entity.Media
import coil.compose.AsyncImage
import org.koin.compose.koinInject

private const val EpisodeDatePattern = "MMM dd, yyyy"

enum class MediaEpisodeVisualState {
    AIRED,
    UPCOMING,
    UNKNOWN,
}

enum class MediaEpisodeEmphasis {
    LATEST,
    NEXT,
}

data class MediaEpisodeItemUiState(
    val stableKey: String,
    val episodeNumber: Int?,
    val seasonNumber: Int?,
    val title: String?,
    val overview: String?,
    val imageUrl: String?,
    val airDate: Long?,
    val runtimeMinutes: Int?,
    val state: MediaEpisodeVisualState,
    val emphasis: MediaEpisodeEmphasis? = null,
)

data class MediaEpisodeProgressUiState(
    val current: Int,
    val total: Int?,
)

data class MediaStatusSectionUiState(
    val spotlight: MediaEpisodeItemUiState?,
    val progress: MediaEpisodeProgressUiState?,
    val showEpisodeGuideAction: Boolean,
)

data class MediaEpisodeGuideUiState(
    val spotlight: MediaEpisodeItemUiState?,
    val progress: MediaEpisodeProgressUiState?,
    val items: List<MediaEpisodeItemUiState>,
)

@Composable
fun rememberMediaStatusSectionUiState(media: Media): MediaStatusSectionUiState =
    remember(media) {
        when (val category = media.category) {
            is Media.Category.Anime -> category.toStatusSectionUiState()
            is Media.Category.Manga ->
                MediaStatusSectionUiState(
                    spotlight = null,
                    progress = null,
                    showEpisodeGuideAction = false,
                )
        }
    }

@Composable
fun rememberMediaEpisodeGuideUiState(media: Media.Extended): MediaEpisodeGuideUiState? =
    remember(media) {
        (media.category as? Media.Category.Anime)?.toEpisodeGuideUiState()
    }

private fun Media.Category.Anime.toStatusSectionUiState(): MediaStatusSectionUiState {
    val items = episodeItems()
    val spotlight = spotlight(items)
    return MediaStatusSectionUiState(
        spotlight = spotlight,
        progress = progressUiState(items),
        showEpisodeGuideAction = spotlight != null || items.isNotEmpty(),
    )
}

private fun Media.Category.Anime.toEpisodeGuideUiState(): MediaEpisodeGuideUiState {
    val items = episodeItems()
    val spotlight = spotlight(items)
    return MediaEpisodeGuideUiState(
        spotlight = spotlight,
        progress = progressUiState(items),
        items = items.filterNot { it.stableKey == spotlight?.stableKey },
    )
}

private fun Media.Category.Anime.progressUiState(items: List<MediaEpisodeItemUiState>): MediaEpisodeProgressUiState? {
    val current =
        scheduleDetails
            ?.airedEpisodes
            ?.takeIf { it >= 0 }
            ?: items.count { it.state == MediaEpisodeVisualState.AIRED }.takeIf { it > 0 }

    val total = episodes.takeIf { it > 0 }
    return current?.let {
        MediaEpisodeProgressUiState(
            current = it,
            total = total,
        )
    }
}

private fun Media.Category.Anime.spotlight(items: List<MediaEpisodeItemUiState>): MediaEpisodeItemUiState? =
    items.firstOrNull { it.emphasis == MediaEpisodeEmphasis.LATEST }
        ?: items.firstOrNull { it.emphasis == MediaEpisodeEmphasis.NEXT }
        ?: items.firstOrNull()

private fun Media.Category.Anime.episodeItems(): List<MediaEpisodeItemUiState> {
    val details = scheduleDetails ?: return emptyList()
    val latestKey = details.lastEpisode?.stableKey()
    val nextKey = details.nextEpisode?.stableKey()
    val now = System.currentTimeMillis() / 1000

    return buildList {
        addAll(details.episodes)
        details.lastEpisode?.let(::add)
        details.nextEpisode?.let(::add)
    }.distinctBy { it.stableKey() }
        .sortedWith(
            compareBy<Media.Category.Anime.ScheduleDetails.Episode> {
                it.seasonNumber ?: Int.MAX_VALUE
            }.thenBy {
                it.episodeNumber ?: Int.MAX_VALUE
            }.thenBy {
                it.airDate ?: Long.MAX_VALUE
            },
        ).map { episode ->
            val key = episode.stableKey()
            val airDate = episode.airDate
            val state =
                when {
                    airDate == null -> MediaEpisodeVisualState.UNKNOWN
                    airDate <= now -> MediaEpisodeVisualState.AIRED
                    else -> MediaEpisodeVisualState.UPCOMING
                }

            MediaEpisodeItemUiState(
                stableKey = key,
                episodeNumber = episode.episodeNumber,
                seasonNumber = episode.seasonNumber,
                title = episode.name?.trim()?.takeIf(String::isNotBlank),
                overview = episode.overview?.trim()?.takeIf(String::isNotBlank),
                imageUrl = episode.image?.trim()?.takeIf(String::isNotBlank),
                airDate = airDate,
                runtimeMinutes = episode.runtime,
                state = state,
                emphasis =
                    when (key) {
                        latestKey -> MediaEpisodeEmphasis.LATEST
                        nextKey -> MediaEpisodeEmphasis.NEXT
                        else -> null
                    },
            )
        }
}

private fun Media.Category.Anime.ScheduleDetails.Episode.stableKey(): String =
    listOfNotNull(seasonNumber, episodeNumber)
        .takeIf { it.isNotEmpty() }
        ?.joinToString(":")
        ?: id?.toString()
        ?: airDate?.toString()
        ?: name.orEmpty()

@Composable
fun MediaEpisodeSpotlightCard(
    episode: MediaEpisodeItemUiState,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = episodeCardColor(episode.state)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            MediaEpisodeArtwork(
                imageUrl = episode.imageUrl,
                modifier = Modifier.weight(0.38f).height(96.dp),
            )
            Column(
                modifier = Modifier.weight(0.62f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MediaEpisodeBadgeRow(episode = episode)
                Text(
                    text = episodeHeadline(episode),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                MediaEpisodeSupportText(episode = episode)
                episode.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun MediaEpisodeListItemCard(
    episode: MediaEpisodeItemUiState,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            MediaEpisodeArtwork(
                imageUrl = episode.imageUrl,
                modifier = Modifier.width(108.dp).height(72.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                MediaEpisodeBadgeRow(episode = episode)
                Text(
                    text = episodeHeadline(episode),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                MediaEpisodeSupportText(episode = episode)
                episode.overview?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun MediaEpisodeProgressRow(
    progress: MediaEpisodeProgressUiState,
    modifier: Modifier = Modifier,
) {
    val progressText =
        progress.total
            ?.takeIf { it > 0 }
            ?.let {
                stringResource(
                    R.string.label_media_status_episode_progress_summary,
                    progress.current,
                    it,
                )
            }
            ?: stringResource(
                R.string.label_media_status_episode_progress_summary_unknown_total,
                progress.current,
            )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_media_status_aired_episodes),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = progressText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LinearProgressIndicator(
            progress = {
                val total = progress.total?.takeIf { it > 0 } ?: return@LinearProgressIndicator 1f
                (progress.current.toFloat() / total.toFloat()).coerceIn(0f, 1f)
            },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(6.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun MediaEpisodeGuideButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Icon(
            imageVector = Icons.Default.PlayCircleOutline,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(R.string.action_media_status_episode_guide),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun MediaEpisodeBadgeRow(episode: MediaEpisodeItemUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        episode.emphasis?.let {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f),
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                Text(
                    text =
                        stringResource(
                            when (it) {
                                MediaEpisodeEmphasis.LATEST -> R.string.label_media_episode_badge_latest
                                MediaEpisodeEmphasis.NEXT -> R.string.label_media_episode_badge_next
                            },
                        ),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = episodeStateContainerColor(episode.state),
            contentColor = episodeStateContentColor(episode.state),
        ) {
            Text(
                text =
                    stringResource(
                        when (episode.state) {
                            MediaEpisodeVisualState.AIRED -> R.string.label_media_episode_state_aired
                            MediaEpisodeVisualState.UPCOMING -> R.string.label_media_episode_state_upcoming
                            MediaEpisodeVisualState.UNKNOWN -> R.string.label_media_episode_state_unknown
                        },
                    ),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun MediaEpisodeSupportText(episode: MediaEpisodeItemUiState) {
    val helper: AniTrendDateHelper = koinInject()
    val supportingText =
        remember(episode) {
            buildList {
                episode.airDate?.let {
                    add(
                        helper.convertFromUnixTimeStamp(
                            unixTimeStamp = it * 1000L,
                            outputDatePattern = EpisodeDatePattern,
                        ),
                    )
                }
                episode.runtimeMinutes?.takeIf { it > 0 }?.let {
                    add("${it} min")
                }
            }.joinToString(" • ").ifBlank { null }
        }

    Text(
        text = supportingText ?: stringResource(R.string.label_media_episode_air_date_unknown),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun MediaEpisodeArtwork(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.6f),
            )
        } else {
            Icon(
                imageVector = Icons.Default.PlayCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Composable
private fun episodeHeadline(episode: MediaEpisodeItemUiState): String {
    val episodeLabel =
        episode.episodeNumber?.let {
            stringResource(R.string.label_media_status_episode_number, it)
        }
    val title = episode.title?.takeIf(String::isNotBlank)

    return when {
        episodeLabel != null && title != null -> "$episodeLabel • $title"
        episodeLabel != null -> episodeLabel
        title != null -> title
        else -> stringResource(R.string.label_media_status_unknown_value)
    }
}

@Composable
@ReadOnlyComposable
private fun episodeCardColor(state: MediaEpisodeVisualState): Color =
    when (state) {
        MediaEpisodeVisualState.AIRED -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
        MediaEpisodeVisualState.UPCOMING -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        MediaEpisodeVisualState.UNKNOWN -> MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)
    }

@Composable
@ReadOnlyComposable
private fun episodeStateContainerColor(state: MediaEpisodeVisualState): Color =
    when (state) {
        MediaEpisodeVisualState.AIRED -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.72f)
        MediaEpisodeVisualState.UPCOMING -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
        MediaEpisodeVisualState.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
    }

@Composable
@ReadOnlyComposable
private fun episodeStateContentColor(state: MediaEpisodeVisualState): Color =
    when (state) {
        MediaEpisodeVisualState.AIRED -> MaterialTheme.colorScheme.onTertiaryContainer
        MediaEpisodeVisualState.UPCOMING -> MaterialTheme.colorScheme.onPrimaryContainer
        MediaEpisodeVisualState.UNKNOWN -> MaterialTheme.colorScheme.onSurfaceVariant
    }
