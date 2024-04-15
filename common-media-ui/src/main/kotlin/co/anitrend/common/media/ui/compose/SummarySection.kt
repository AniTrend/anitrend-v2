/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.common.media.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.widget.airing.MediaAiringScheduleWidget
import co.anitrend.common.media.ui.widget.title.MediaSubTitleWidget
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.compose.series_aspect_ration
import co.anitrend.core.android.compose.series_image_lg
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.roundedCornersTransformation
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.sorting.Sorting


@Composable
private fun MediaCover(
    cover: IMediaCover,
    onCoverClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier
) {
    AniTrendImage(
        image = cover,
        imageType = RequestImage.Media.ImageType.POSTER,
        modifier = modifier,
        transformations = listOf(roundedCornersTransformation),
        onClick = onCoverClick,
    )
}

@Composable
private fun TitleWidget(
    title: IMediaTitle,
    modifier: Modifier = Modifier,
    extraInfo: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = title.userPreferred.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AniTrendTheme.typography.h6,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = extraInfo ?: title.native.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            style = AniTrendTheme.typography.caption,
        )
    }
}

@Composable
private fun ReleaseStatusWidget(
    media: Media,
    modifier: Modifier = Modifier,
) {
    Spacer(Modifier.height(8.dp))
    when (val category = media.category) {
        is Media.Category.Anime -> {
            Spacer(Modifier.height(8.dp))
            AndroidView(
                factory = ::MediaAiringScheduleWidget,
                modifier = modifier
            ) { widget ->
                widget.setUpAiringSchedule(media)
            }
        }
        is Media.Category.Manga -> {
            val chapters = pluralStringResource(
                R.plurals.label_number_of_chapters,
                category.chapters
            ).format(category.chapters)
            val volumes = pluralStringResource(
                R.plurals.label_number_of_volumes,
                category.volumes
            ).format(category.volumes)

            val textTemplate = if (category.volumes > 0)
                "$volumes $CHARACTER_SEPARATOR $chapters"
            else
                "${media.status?.alias.toString()} $CHARACTER_SEPARATOR $chapters"

            Text(
                text = textTemplate,
                fontWeight = FontWeight.Bold,
                color = media.image.rememberAccentColor(),
                style = AniTrendTheme.typography.caption,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun RankingItems(
    accentColor: Color,
    modifier: Modifier = Modifier,
    rankings: List<IMediaRank> = emptyList(),
    onClick: (IMediaRank) -> Unit,
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(all = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(
            count = rankings.size,
            key = { rankings[it].id },
            contentType = { rankings[it].type }
        ) { index ->
            val item = rankings[index]
            Chip(
                onClick = { onClick(item) },
                colors = ChipDefaults.chipColors(
                    backgroundColor = accentColor,
                    contentColor = Color.White,
                ),
                modifier = Modifier
            ) {
                Text(
                    text = "# ${item.rank} ${item.context}".capitalizeWords(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = AniTrendTheme.typography.caption,
                )
            }
        }
    }
}

@Composable
private fun InfoWidget(
    accentColor: Color,
    meanScore: Int,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = "${meanScore}%",
            color = accentColor,
            fontWeight = FontWeight.Bold,
            style = AniTrendTheme.typography.h6
        )
        Spacer(Modifier.width(8.dp))
    }
}

@Composable
private fun MediaSubTitle(
    media: Media,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = ::MediaSubTitleWidget,
        modifier = modifier
    ) {
        it.setUpSubTitle(media)
    }
}

@Composable
fun SummarySection(
    media: Media,
    accentColor: Color,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onCoverClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        MediaCover(
            cover = media.image,
            modifier = Modifier.height(series_image_lg)
                .aspectRatio(series_aspect_ration),
            onCoverClick = onCoverClick,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(y = 24.dp)
        ) {
            TitleWidget(
                title = media.title,
                extraInfo = (media as Media.Extended).extraInfo
            )
            ReleaseStatusWidget(media)
            MediaSubTitle(
                media = media
            )
            InfoWidget(
                accentColor = accentColor,
                meanScore = media.meanScore,
            )
            RankingItems(
                accentColor = accentColor,
                rankings = media.rankings.toList(),
                onClick = { rank ->
                    val sorting = when (rank.type) {
                        MediaRankType.RATED -> Sorting(
                            sortable = MediaSort.SCORE,
                            order = SortOrder.DESC
                        )
                        else -> Sorting(
                            sortable = MediaSort.POPULARITY,
                            order = SortOrder.DESC
                        )
                    }

                    onMediaDiscoverableItemClick(
                        MediaDiscoverRouter.MediaDiscoverParam(
                            type = media.category.type,
                            format = media.format,
                            season = media.season,
                            seasonYear = if (rank.allTime != true && media.category.type == MediaType.ANIME) rank.year else null,
                            startDate_like = if (rank.allTime != true && media.category.type == MediaType.MANGA) "${rank.year}%" else null,
                            sort = listOf(sorting)
                        )
                    )
                }
            )
        }
    }
}
