/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.common.media.ui.compose.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.AniTrendDimensions
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.widget.releasing.MediaReleaseStatus
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.navigation.ImageViewerRouter

@Composable
private fun MediaCover(
    cover: IMediaCover,
    onCoverClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    AniTrendImage(
        image = cover,
        imageType = RequestImage.Media.ImageType.POSTER,
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title.userPreferred.toString(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = extraInfo ?: title.native.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun MediaHeaderInfoSection(
    media: Media,
    onCoverClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        MediaCover(
            cover = media.image,
            modifier =
                Modifier
                    .height(AniTrendDimensions.series_image_lg)
                    .aspectRatio(AniTrendDimensions.series_image_aspect_ratio),
            onCoverClick = onCoverClick,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.offset(y = 24.dp),
        ) {
            MediaTitle(
                title = media.title,
                extraInfo = (media as Media.Extended).extraInfo,
            )
            // TODO: We need to replace this in a different section
            MediaReleaseStatus(media)
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaSummarySectionPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaHeaderInfoSection(
            media =
                Media.Extended.empty().copy(
                    title =
                        MediaTitle(
                            userPreferred = "Boku no Hero Academia 3",
                            english = "My Hero Academia Season 3",
                            romaji = "Boku no Hero Academia 3",
                            native = "僕のヒーローアカデミア 3",
                        ),
                    // extraInfo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
                    status = MediaStatus.RELEASING,
                    image = MediaImage.empty().copy(color = "#e4a15d"),
                    startDate = FuzzyDate.empty().copy(2018),
                    format = MediaFormat.TV,
                    category =
                        Media.Category.Anime(
                            episodes = 26,
                            broadcast = "",
                            duration = 24,
                            premiered = "",
                            schedule =
                                AiringSchedule(
                                    airingAt = 1750862811,
                                    episode = 8,
                                    timeUntilAiring = 62811,
                                    mediaId = 1,
                                    id = 1,
                                ),
                        ),
                ),
            onCoverClick = {},
        )
    }
}
