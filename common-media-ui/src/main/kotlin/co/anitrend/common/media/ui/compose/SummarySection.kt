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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.releasing.MediaReleaseStatus
import co.anitrend.common.media.ui.compose.title.MediaSubTitle
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.compose.series_aspect_ration
import co.anitrend.core.android.compose.series_image_lg
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.roundedCornersTransformation
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.navigation.ImageViewerRouter


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
private fun MediaTitle(
    title: IMediaTitle,
    modifier: Modifier = Modifier,
    extraInfo: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title.userPreferred.toString(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = AniTrendTheme.typography.h6,
        )
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
private fun MediaScore(
    accentColor: Color,
    meanScore: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${meanScore}%",
        color = accentColor,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier,
    )
}

@Composable
fun SummarySection(
    media: Media,
    accentColor: Color,
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
            MediaTitle(
                title = media.title,
                extraInfo = (media as Media.Extended).extraInfo
            )
            MediaReleaseStatus(media)
            MediaSubTitle(media = media)
            MediaScore(
                accentColor = accentColor,
                meanScore = media.meanScore,
            )
        }
    }
}
