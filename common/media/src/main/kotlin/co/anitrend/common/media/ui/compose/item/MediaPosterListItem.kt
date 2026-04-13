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
package co.anitrend.common.media.ui.compose.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.component.MediaRating
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.extensions.displayTitle
import co.anitrend.common.media.ui.compose.extensions.genreMetaLine
import co.anitrend.common.media.ui.compose.extensions.secondaryTitle
import co.anitrend.common.media.ui.compose.preview.mediaPreviewItems
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.model.common.IParam

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPosterListItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    supportingContent: @Composable ColumnScope.() -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onMediaItemClick(
                            MediaRouter.MediaParam(
                                id = media.id,
                                type = media.category.type,
                            ),
                        )
                    },
                    onLongClick = {
                        onMediaItemClick(
                            MediaListEditorRouter.MediaListEditorParam(
                                mediaId = media.id,
                                mediaType = media.category.type,
                                scoreFormat = mediaPreferenceData.scoreFormat,
                            ),
                        )
                    },
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier.width(112.dp).height(160.dp),
            ) {
                AniTrendImage(
                    image = media.image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
                    onClick = {},
                )
                MediaRating(
                    media = media,
                    scoreFormat = mediaPreferenceData.scoreFormat,
                    modifier = Modifier.padding(8.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = media.displayTitle().orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                media.secondaryTitle()?.also { secondaryTitle ->
                    Text(
                        text = secondaryTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                supportingContent()
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaPosterListItemPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    val media = mediaPreviewItems.first()

    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaPosterListItem(
            media = media,
            mediaPreferenceData = MediaPreferenceData(scoreFormat = ScoreFormat.POINT_10_DECIMAL),
            onMediaItemClick = {},
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "TV • ${media.status?.name.orEmpty().lowercase().replaceFirstChar(Char::uppercaseChar)} • ${media.startDate.year}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            media.genreMetaLine()?.also { genres ->
                Text(
                    text = genres,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
