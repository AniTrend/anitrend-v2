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
package co.anitrend.common.media.ui.compose.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.component.MediaRating
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.title.MediaSubTitleText
import co.anitrend.core.android.R
import co.anitrend.core.android.compose.design.image.AniTrendImage
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.IMediaTitle
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.model.common.IParam

@Composable
private fun MediaTitleItem(
    mediaTitle: IMediaTitle,
    mediaStatus: MediaStatus?,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        mediaStatus?.let {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            when (mediaStatus) {
                                MediaStatus.NOT_YET_RELEASED -> colorResource(R.color.orange_A700)
                                MediaStatus.RELEASING -> colorResource(R.color.blue_A700)
                                MediaStatus.CANCELLED -> colorResource(R.color.red_A700)
                                MediaStatus.FINISHED -> colorResource(R.color.green_A700)
                                MediaStatus.HIATUS -> colorResource(R.color.purple_A700)
                            },
                        ),
            )
            Text(
                text = mediaTitle.userPreferred?.toString().orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
fun MediaCompactItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    mediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f, fill = true),
        ) {
            AniTrendImage(
                image = media.image,
                imageType = RequestImage.Media.ImageType.POSTER,
                onClick = {
                    mediaItemClick(
                        MediaRouter.MediaParam(
                            id = media.id,
                            type = media.category.type,
                        ),
                    )
                },
                onLongClick = {
                    mediaItemClick(
                        MediaListEditorRouter.MediaListEditorParam(
                            mediaId = media.id,
                            mediaType = media.category.type,
                            scoreFormat = mediaPreferenceData.scoreFormat,
                        ),
                    )
                },
                modifier =
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
            )

            MediaRating(
                media = media,
                scoreFormat = mediaPreferenceData.scoreFormat,
                modifier = Modifier.padding(4.dp),
            )
        }
        MediaTitleItem(
            mediaTitle = media.title,
            mediaStatus = media.status,
        )
        MediaSubTitleText(
            media = media,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        AiringScheduleText(
            media = media,
            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaCompactItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaCompactItem(
            media =
                Media.Core.empty().copy(
                    title =
                        MediaTitle(
                            userPreferred = "Boku no Hero Academia 3",
                            english = "My Hero Academia Season 3",
                            romaji = "Boku no Hero Academia 3",
                            native = "僕のヒーローアカデミア 3",
                        ),
                    status = MediaStatus.FINISHED,
                    image = MediaImage.empty().copy(color = "#e4a15d"),
                    startDate = FuzzyDate.empty().copy(2018),
                    format = MediaFormat.TV,
                    category =
                        Media.Category.Anime
                            .empty()
                            .copy(25),
                ),
            mediaPreferenceData =
                MediaPreferenceData(
                    ScoreFormat.POINT_10_DECIMAL,
                ),
            mediaItemClick = {},
            modifier =
                Modifier
                    .padding(8.dp)
                    .size(
                        height = 265.dp,
                        width = 150.dp,
                    ),
        )
    }
}
