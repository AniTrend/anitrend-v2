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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.widget.airing.MediaAiringScheduleWidget
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.compose.series_aspect_ration
import co.anitrend.core.android.compose.series_image_lg
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.roundedCornersTransformation
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.toRequestBuilder
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.navigation.ImageViewerRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import coil.compose.rememberImagePainter



@Composable
private fun MediaCover(
    cover: IMediaCover,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val requestImage = remember(cover) {
        cover.toMediaRequestImage(
            RequestImage.Media.ImageType.POSTER
        ).toRequestBuilder(
            context,
            listOf(roundedCornersTransformation)
        ).build()
    }

    Image(
        painter = rememberImagePainter(requestImage),
        contentDescription = "Media cover image",
        contentScale = ContentScale.Fit,
        modifier = modifier.clickable {
            val coverXL = cover.extraLarge ?: return@clickable
            val param = ImageViewerRouter.Param(coverXL)
            ImageViewerRouter.startActivity(
                context = context,
                navPayload = param.asNavPayload()
            )
        }
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
            style = AniTrendTheme.typography.h6
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = extraInfo ?: title.native.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            style = AniTrendTheme.typography.caption
        )
    }
}

@Composable
private fun ReleaseStatusWidget(media: Media, modifier: Modifier = Modifier) {
    val category = media.category
    if (category is Media.Category.Anime && category.schedule != null) {
        Spacer(Modifier.height(8.dp))
        AndroidView(
            factory = ::MediaAiringScheduleWidget,
            modifier = modifier
        ) { widget ->
            widget.setUpAiringSchedule(media)
        }
    } else {
        Spacer(Modifier.height(8.dp))
        Text(
            text = media.status?.alias.toString(),
            fontWeight = FontWeight.Bold,
            color = media.rememberAccentColor(),
            style = AniTrendTheme.typography.caption,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
private fun InfoWidget(media: Media, modifier: Modifier = Modifier) {
    val ratedRank = remember(media.rankings) {
        media.rankings.firstOrNull {
            it.type == MediaRankType.RATED
        }
    }

    Row(modifier = modifier) {
        Text(
            text = "${media.averageScore}%",
            color = media.rememberAccentColor(),
            fontWeight = FontWeight.Bold,
            style = AniTrendTheme.typography.h6
        )
        Spacer(Modifier.width(8.dp))
        if (ratedRank != null) {
            Text(
                text = "# ${ratedRank.rank} ${ratedRank.context}".capitalizeWords(),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = AniTrendTheme.typography.caption,
                modifier = Modifier
                    .background(
                        color = media.rememberAccentColor(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(4.dp)
            )
        }
    }
}

@Composable
private fun StudioWidget(media: Media) {
    media.twitterTag?.let { twitterTag ->
        Text(
            text = twitterTag.toString(),
            fontWeight = FontWeight.Bold,
            color = media.rememberAccentColor(),
            style = AniTrendTheme.typography.caption,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun SummarySection(media: Media, modifier: Modifier = Modifier) {
    media as Media.Extended
    Row(modifier = modifier) {
        MediaCover(
            cover = media.image,
            modifier = Modifier.height(series_image_lg)
                .aspectRatio(series_aspect_ration)
        )
        Spacer(Modifier.width(8.dp))
        Column(Modifier.offset(y = 24.dp)) {
            TitleWidget(
                title = media.title,
                extraInfo = media.extraInfo
            )
            ReleaseStatusWidget(media)
            Spacer(Modifier.height(8.dp))
            InfoWidget(media)
            Spacer(Modifier.height(8.dp))
            StudioWidget(media)
        }
    }
}