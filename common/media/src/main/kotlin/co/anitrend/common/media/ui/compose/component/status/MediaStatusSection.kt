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
package co.anitrend.common.media.ui.compose.component.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.R
import co.anitrend.core.extensions.stackTrace
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import org.koin.compose.koinInject

private const val StatusDatePattern = "MMM dd, yyyy"

@Composable
fun MediaStatusSection(
    media: Media,
    onOpenEpisodeGuide: () -> Unit,
    showEpisodeGuideAction: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val episodeUiState = rememberMediaStatusSectionUiState(media)

    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatusSectionHeader(category = media.category)

            episodeUiState.spotlight?.let {
                MediaEpisodeSpotlightCard(
                    episode = it,
                    showOverview = false,
                )
            }

            if (media.status == MediaStatus.HIATUS) {
                HiatusInfoCard(category = media.category)
            }

            MediaDetailsRow(media = media)

            episodeUiState.progress?.let {
                MediaEpisodeProgressRow(progress = it)
            } ?: run {
                if (media.category is Media.Category.Manga) {
                    ProgressDetails(media = media)
                }
            }

            if (showEpisodeGuideAction && episodeUiState.showEpisodeGuideAction) {
                MediaEpisodeGuideButton(onClick = onOpenEpisodeGuide)
            }
        }
    }
}

@Composable
private fun StatusSectionHeader(category: Media.Category) {
    val titleRes =
        when (category) {
            is Media.Category.Anime -> R.string.label_media_status_airing_title
            is Media.Category.Manga -> R.string.label_media_status_publishing_title
        }

    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun HiatusInfoCard(category: Media.Category) {
    val title =
        when (category) {
            is Media.Category.Anime -> stringResource(R.string.label_media_status_anime_hiatus_title)
            else -> stringResource(R.string.label_media_status_manga_hiatus_title)
        }

    val subtitle =
        when (category) {
            is Media.Category.Anime -> stringResource(R.string.label_media_status_anime_hiatus_subtitle)
            else -> stringResource(R.string.label_media_status_manga_hiatus_subtitle)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = statusColorFor(MediaStatus.HIATUS).copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.ErrorOutline,
                contentDescription = null,
                tint = statusColorFor(MediaStatus.HIATUS),
                modifier = Modifier.size(32.dp),
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun MediaDetailsRow(
    media: Media,
    dateHelper: AniTrendDateHelper = koinInject(),
) {
    val items = buildStatusMetadata(media = media, dateHelper = dateHelper)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                row.forEach { item ->
                    StatusMetadataItem(
                        item = item,
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun formatMediaStartDate(
    media: Media,
    helper: AniTrendDateHelper,
): String {
    val date =
        media.startDate.takeIf { !it.isDateNotSet() }?.let {
            runCatching {
                helper.convertToTextDate(it).toString()
            }.stackTrace()
        }
    val season = media.season
    val fallbackDate =
        (media.category as? Media.Category.Anime)
            ?.scheduleDetails
            ?.firstAirDate
            ?.let {
                helper.convertFromUnixTimeStamp(
                    unixTimeStamp = it * 1000L,
                    outputDatePattern = StatusDatePattern,
                )
            }
    // This is a simplified formatter. You might have a more robust one in your domain/core layer.
    return when {
        season != null ->
            "${season.alias} ${media.startDate.year}"

        else -> date ?: fallbackDate ?: stringResource(R.string.label_media_status_unknown_value)
    }
}

@Composable
private fun StatusMetadataItem(
    item: StatusMetadata,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun buildStatusMetadata(
    media: Media,
    dateHelper: AniTrendDateHelper,
): List<StatusMetadata> =
    buildList {
        when (val category = media.category) {
            is Media.Category.Anime -> {
                add(
                    StatusMetadata(
                        label = stringResource(R.string.label_media_status_broadcast),
                        value = category.broadcast?.takeIf(String::isNotBlank) ?: stringResource(R.string.label_media_status_unknown_value),
                        icon = Icons.Filled.SettingsInputAntenna,
                    ),
                )
                add(
                    StatusMetadata(
                        label = stringResource(R.string.label_media_status_premiered),
                        value = formatMediaStartDate(media = media, helper = dateHelper),
                        icon = Icons.Filled.CalendarToday,
                    ),
                )
            }

            is Media.Category.Manga -> {
                add(
                    StatusMetadata(
                        label = stringResource(R.string.label_media_status_publication),
                        value = category.type.alias?.takeIf { it.isNotBlank() }?.toString() ?: stringResource(R.string.label_media_status_unknown_value),
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                    ),
                )
                add(
                    StatusMetadata(
                        label = stringResource(R.string.label_media_status_started),
                        value = formatMediaStartDate(media = media, helper = dateHelper),
                        icon = Icons.Filled.CalendarToday,
                    ),
                )
            }
        }
    }

@Composable
private fun ProgressDetails(media: Media) {
    val (progress, total, unitLabelSingular, unitLabelPlural) = computeProgressDetails(media)
    val totalDisplay = if (total > 0) total.toString() else "?"
    val unitLabel = if (total == 1) unitLabelSingular else unitLabelPlural

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = unitLabel.replaceFirstChar { it.titlecase() }, // "Episodes" or "Chapters"
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "$progress/$totalDisplay",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (total > 0 && progress <= total) {
            LinearProgressIndicator(
                progress = { (progress.toFloat() / total.toFloat()).coerceIn(0f, 1f) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else if (total == 0 && progress > 0) {
            LinearProgressIndicator(
                progress = { 1f },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else if (media.status == MediaStatus.FINISHED && total > 0) {
            LinearProgressIndicator(
                progress = { 1f },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
@ReadOnlyComposable
private fun computeProgressDetails(media: Media): Quadruple<Int, Int, String, String> =
    when (val category = media.category) {
        is Media.Category.Anime -> {
            val totalEpisodes = category.episodes
            val currentProgress = (media.mediaList?.progress as? MediaListProgress.Anime)?.episodeProgress ?: 0
            Quadruple(
                currentProgress,
                totalEpisodes,
                stringResource(id = R.string.label_episode_singular),
                stringResource(id = R.string.label_episode_plural),
            )
        }
        is Media.Category.Manga -> {
            val totalChapters = category.chapters
            val currentProgress = (media.mediaList?.progress as? MediaListProgress.Manga)?.chapterProgress ?: 0
            Quadruple(
                currentProgress,
                totalChapters,
                stringResource(id = R.string.label_chapter_singular),
                stringResource(id = R.string.label_chapter_plural),
            )
        }
    }

data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
)

private data class StatusMetadata(
    val label: String,
    val value: String,
    val icon: ImageVector,
)

@Composable
@ReadOnlyComposable
private fun statusColorFor(status: MediaStatus?): Color {
    val scheme = MaterialTheme.colorScheme
    return when (status) {
        MediaStatus.RELEASING -> colorResource(co.anitrend.android.core.R.color.blue_A700)
        MediaStatus.FINISHED -> colorResource(co.anitrend.android.core.R.color.green_A700)
        MediaStatus.CANCELLED -> colorResource(co.anitrend.android.core.R.color.red_A700)
        MediaStatus.NOT_YET_RELEASED -> colorResource(co.anitrend.android.core.R.color.orange_A700)
        else -> scheme.surfaceVariant
    }
}

@Composable
@ReadOnlyComposable
private fun contentColorFor(backgroundColor: Color): Color =
    when {
        backgroundColor.luminance() > 0.5f -> MaterialTheme.colorScheme.onSurface
        else -> Color.White
    }

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaStatusSectionPreview(
    @PreviewParameter(MediaStatusSectionPreviewProvider::class) media: Media,
) {
    PreviewTheme(wrapInSurface = true) {
        MediaStatusSection(
            media = media,
            onOpenEpisodeGuide = {},
        )
    }
}
