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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleTextFormat
import co.anitrend.core.extensions.stackTrace
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import org.koin.compose.koinInject

@Composable
fun MediaStatusSection(
    media: Media,
    onShowSchedule: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            // UI looks cleaner without the section title
            // SectionTitleRow(mediaType = media.category.type)
            StatusInfoRow(media = media)

            when (media.status) {
                MediaStatus.RELEASING -> NextAiringPublicationCard(media = media)
                MediaStatus.HIATUS -> HiatusInfoCard(category = media.category)
                MediaStatus.FINISHED, MediaStatus.CANCELLED, MediaStatus.NOT_YET_RELEASED -> {
                    // No special card for these states, status chip handles it
                }
                null -> {
                    // Handled by status chip
                }
            }

            MediaDetailsRow(media = media)
            ProgressDetails(media = media)
            ScheduleButton(mediaType = media.category.type, onClick = onShowSchedule)
        }
    }
}

@Composable
private fun SectionTitleRow(mediaType: MediaType) {
    val titleIcon =
        when (mediaType) {
            MediaType.ANIME -> Icons.Filled.Tv
            else -> Icons.AutoMirrored.Filled.MenuBook
        }
    val titleText =
        stringResource(
            id =
                when (mediaType) {
                    MediaType.ANIME -> R.string.label_media_status_airing
                    else -> R.string.label_media_status_publishing
                },
        )
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(imageVector = titleIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = titleText, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun StatusInfoRow(media: Media) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatusChipRedesigned(media = media)
        if (media.status == MediaStatus.RELEASING) {
            LiveActiveIndicator(mediaType = media.category.type)
        }
    }
}

@Composable
private fun StatusChipRedesigned(media: Media) {
    val status = media.status
    val statusText =
        when (status) {
            MediaStatus.RELEASING ->
                when (media.category.type) {
                    MediaType.ANIME -> stringResource(R.string.label_media_status_chip_airing)
                    else -> stringResource(R.string.label_media_status_chip_publishing)
                }
            MediaStatus.FINISHED -> stringResource(R.string.label_media_status_chip_completed)
            MediaStatus.NOT_YET_RELEASED -> stringResource(R.string.label_media_status_chip_not_yet_released)
            MediaStatus.CANCELLED -> stringResource(R.string.label_media_status_chip_cancelled)
            MediaStatus.HIATUS -> stringResource(R.string.label_media_status_chip_hiatus)
            null -> stringResource(R.string.label_media_status_unknown_value)
        }
    val icon =
        when (status) {
            MediaStatus.RELEASING -> Icons.Filled.SettingsInputAntenna
            MediaStatus.FINISHED -> Icons.Filled.CheckCircleOutline
            MediaStatus.HIATUS -> Icons.Filled.PauseCircleOutline
            else -> Icons.AutoMirrored.Filled.HelpOutline
        }

    SuggestionChip(
        onClick = {},
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = "Filter by status",
                modifier = Modifier.size(18.dp),
            )
        },
        border = BorderStroke(width = 1.dp, color = statusColorFor(status)),
        label = {
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        },
        colors = SuggestionChipDefaults.suggestionChipColors(),
    )
}

@Composable
private fun LiveActiveIndicator(mediaType: MediaType) {
    val text =
        when (mediaType) {
            MediaType.ANIME -> stringResource(R.string.label_media_status_airing_indicator_live)
            else -> stringResource(R.string.publication_status_active)
        }
    val color = MaterialTheme.colorScheme.primary

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(8.dp)
                    .background(color, CircleShape),
        )
        Text(text = text, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun NextAiringPublicationCard(
    media: Media,
    dateHelper: AniTrendDateHelper = koinInject(),
) {
    val category = media.category
    if (category !is Media.Category.Anime) {
        return
    }

    val schedule = category.schedule

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.label_media_status_next_episode),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (schedule?.episode != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ) {
                        Text(
                            text = stringResource(R.string.label_media_status_episode_number, schedule.episode),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            if (schedule != null) {
                AiringScheduleText(
                    media = media,
                    style =
                        MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    format = AiringScheduleTextFormat.WITHOUT_PREFIX,
                )
                Text(
                    text =
                        dateHelper.convertFromUnixTimeStamp(
                            unixTimeStamp = schedule.airingAt * 1000L,
                            outputDatePattern = AniTrendDateHelper.DATE_FORMAT_WITH_TIME_ZONE,
                        ),
                    style = MaterialTheme.typography.bodySmall,
                )
            } else if (media.category is Media.Category.Manga) {
                // Placeholder for Manga countdown if data becomes available
                Text(
                    text = stringResource(R.string.label_media_status_manga_next_chapter_placeholder), // e.g. "6d 22h 54m"
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                )
                Text(
                    text = stringResource(R.string.label_media_status_manga_next_chapter_date_placeholder), // e.g. "Tue, Sep 2, 02:50 AM GMT+9"
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
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
private fun MediaDetailsRow(media: Media) {
    val broadcastOrPublicationStatus =
        when (val category = media.category) {
            is Media.Category.Anime -> {
                category.broadcast?.let { it.ifBlank { stringResource(R.string.label_media_status_unknown_value) } }
            }
            is Media.Category.Manga -> {
                // Assuming publishingInfo field
                category.type.alias.let { it.ifBlank { stringResource(R.string.label_media_status_unknown_value) } }
            }
        }?.toString()

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text =
                    if (media.category.type == MediaType.ANIME) {
                        stringResource(R.string.label_media_status_broadcast)
                    } else {
                        stringResource(R.string.label_media_status_publication)
                    },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                val icon =
                    when (media.category.type) {
                        MediaType.ANIME -> Icons.Filled.SettingsInputAntenna
                        else -> Icons.AutoMirrored.Filled.MenuBook
                    }
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(
                    text = broadcastOrPublicationStatus ?: stringResource(R.string.label_media_status_unknown_value),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text =
                    when (media.category.type) {
                        MediaType.ANIME -> stringResource(R.string.label_media_status_premiered)
                        else -> stringResource(R.string.label_media_status_started)
                    },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Filled.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                Text(
                    text = formatMediaStartDate(media),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun formatMediaStartDate(media: Media): String {
    val helper = AniTrendDateHelper()
    val date =
        media.startDate.takeIf { !it.isDateNotSet() }?.let {
            runCatching {
                helper.convertToTextDate(it).toString()
            }.stackTrace()
        }
    val season = media.season
    // This is a simplified formatter. You might have a more robust one in your domain/core layer.
    return when {
        season != null ->
            "${season.alias} ${media.startDate.year}"

        else -> date ?: stringResource(R.string.label_media_status_unknown_value)
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
        } else if (total == 0 && progress > 0) { // Progress known, total unknown
            LinearProgressIndicator(
                progress = { 1f }, // Show as indeterminate or full if only progress known
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else if (media.status == MediaStatus.FINISHED && total > 0) { // Completed
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
private fun ScheduleButton(
    mediaType: MediaType,
    onClick: () -> Unit,
) {
    // TODO: When we have episode data we should only show this episode schedule button when we have episodes to show
    if (mediaType == MediaType.ANIME) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text =
                    stringResource(
                        id = R.string.label_media_status_episode_schedule_button,
                    ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
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
            onShowSchedule = {},
        )
    }
}
