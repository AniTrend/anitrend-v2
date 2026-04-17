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

private fun IMediaTitle.defaultSupportingText(): String? =
    listOf(native, english, romaji)
        .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
        .firstOrNull()

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
    supportingText: String? = null,
    compact: Boolean = false,
) {
    val secondaryText = supportingText?.takeIf(String::isNotBlank) ?: title.defaultSupportingText()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(if (compact) 2.dp else 8.dp),
    ) {
        Text(
            text = title.userPreferred?.toString().orEmpty(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = if (compact) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall,
        )
        secondaryText?.let {
            Text(
                text = it,
                maxLines = if (compact) 1 else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = if (compact) 4.dp else 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun MediaHeaderInfoSection(
    media: Media,
    onCoverClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
    preferExtendedExtraInfo: Boolean = true,
    compact: Boolean = false,
) {
    val supportingText =
        if (preferExtendedExtraInfo) {
            (media as? Media.Extended)?.extraInfo?.takeIf(String::isNotBlank) ?: media.title.defaultSupportingText()
        } else {
            media.title.defaultSupportingText()
        }

    Row(
        horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp),
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
            verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
            modifier = Modifier.offset(y = if (compact) 10.dp else 24.dp),
        ) {
            MediaTitle(
                title = media.title,
                supportingText = supportingText,
                compact = compact,
            )
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
                            userPreferred = "Seishun Buta Yarou wa Santa Claus no Yume wo Minai",
                            english = "Rascal Does Not Dream of Santa Claus",
                            romaji = "Seishun Buta Yarou wa Santa Claus no Yume wo Minai",
                            native = "青春ブタ野郎はサンタクロースの夢を見ない",
                        ),
                    // extraInfo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et",
                    status = MediaStatus.RELEASING,
                    image = MediaImage.empty().copy(color = "#e4c928"),
                    startDate = FuzzyDate(2025, 7, 5),
                    endDate = FuzzyDate(2025, 9, 27),
                    format = MediaFormat.TV,
                    category =
                        Media.Category.Anime(
                            episodes = 13,
                            broadcast = "",
                            duration = 24,
                            premiered = "",
                            schedule =
                                AiringSchedule(
                                    airingAt = 1756564200,
                                    episode = 9,
                                    timeUntilAiring = 323880,
                                    mediaId = 171046,
                                    id = 399334,
                                ),
                        ),
                ),
            onCoverClick = {},
        )
    }
}
