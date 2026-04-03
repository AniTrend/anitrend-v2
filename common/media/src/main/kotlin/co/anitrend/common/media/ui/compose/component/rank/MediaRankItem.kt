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
package co.anitrend.common.media.ui.compose.component.rank

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.common.media.ui.R
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

private data class MediaRankPresentation(
    val title: String,
    val subtitle: String,
)

private fun IMediaRank.toSortings(): List<Sorting<MediaSort>> =
    listOf(
        when (type) {
            MediaRankType.RATED ->
                Sorting(
                    sortable = MediaSort.SCORE,
                    order = SortOrder.DESC,
                )

            MediaRankType.POPULAR ->
                Sorting(
                    sortable = MediaSort.POPULARITY,
                    order = SortOrder.DESC,
                )
        },
    )

@Composable
private fun rememberMediaRankPresentation(rank: IMediaRank): MediaRankPresentation {
    val allTimeLabel = stringResource(R.string.label_media_rank_timeframe_all_time)

    return remember(rank, allTimeLabel) {
        val season = rank.season
        val year = rank.year
        val timeframe =
            when {
                rank.allTime == true -> allTimeLabel
                season != null && year != null -> "${season.alias} $year"
                season != null -> season.alias.toString()
                year != null -> year.toString()
                else -> null
            }

        MediaRankPresentation(
            title =
                listOf(
                    rank.context.toString().capitalizeWords(),
                    timeframe,
                ).filterNotNull().joinToString(" • "),
            subtitle =
                listOf(
                    rank.type.alias.toString(),
                    rank.format.alias.toString(),
                ).joinToString(" • "),
        )
    }
}

@Composable
private fun MediaRankBadge(
    rank: IMediaRank,
    modifier: Modifier = Modifier,
) {
    val (containerColor, contentColor) =
        when (rank.type) {
            MediaRankType.RATED ->
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.72f) to
                    MaterialTheme.colorScheme.onSecondaryContainer

            MediaRankType.POPULAR ->
                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.72f) to
                    MaterialTheme.colorScheme.onTertiaryContainer
        }

    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier,
    ) {
        Text(
            text = "#${rank.rank}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun MediaRankLeadingIcon(
    rank: IMediaRank,
    modifier: Modifier = Modifier,
) {
    val (icon, containerColor, tint) =
        when (rank.type) {
            MediaRankType.POPULAR ->
                Triple(
                    Icons.AutoMirrored.Rounded.TrendingUp,
                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                    MaterialTheme.colorScheme.onTertiaryContainer,
                )

            MediaRankType.RATED ->
                Triple(
                    Icons.Rounded.StarRate,
                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                    MaterialTheme.colorScheme.onSecondaryContainer,
                )
        }

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.size(40.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
internal fun MediaRankInsightRow(
    rank: IMediaRank,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.16f),
) {
    val presentation = rememberMediaRankPresentation(rank)

    Surface(
        color = containerColor,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.56f)),
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(rank, rank.toSortings()) },
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MediaRankLeadingIcon(rank = rank)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = presentation.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = presentation.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            MediaRankBadge(
                rank = rank,
                modifier = Modifier.widthIn(min = 64.dp),
            )
        }
    }
}

@Composable
fun MediaRankItem(
    rank: IMediaRank,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaRankInsightRow(
        rank = rank,
        onClick = onClick,
        modifier = modifier,
    )
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaRankItemPreview(
    @PreviewParameter(MediaRankPreviewProvider::class) rank: IMediaRank,
) {
    PreviewTheme(wrapInSurface = true) {
        MediaRankItem(
            rank = rank,
            onClick = { _, _ -> },
            modifier = Modifier.padding(8.dp),
        )
    }
}
