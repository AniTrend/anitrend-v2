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
package co.anitrend.android.core.compose.design.image

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.helpers.image.model.RequestImage.Media.ImageType.BANNER
import co.anitrend.android.core.helpers.image.toRequestBuilder
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.navigation.ImageViewerRouter
import coil.compose.AsyncImage
import coil.transform.Transformation

object AniTrendImageDefaults {
    val BANNER_SIZE = Modifier.height(200.dp)
}

/**
 * Custom [AsyncImage] for specific use cases that uses auto image quality selection based on device power requirements
 * see [co.anitrend.android.core.controller.power.contract.IPowerController]
 *
 * @param image Resource to load, if literal use [co.anitrend.android.core.helpers.image.toCoverImage]
 * @param modifier Default modifier for the image surface
 * @param imageType The type of image, this will make some behavioural changes
 * @param transformations Image transformations for Coil
 * @param contentScale [ContentScale] defaulted to [ContentScale.Crop]
 * @param contentDescription Accessibility label for the image
 * @param onClick Optional callback with a receiver of [ImageViewerRouter.ImageSourceParam]
 * @param onLongClick Optional long press callback
 * @param onDoubleClick Optional double tap callback
 *
 * @see [co.anitrend.android.core.helpers.image]
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AniTrendImage(
    image: ICoverImage,
    imageType: RequestImage.Media.ImageType,
    modifier: Modifier = Modifier,
    transformations: List<Transformation> = emptyList(),
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = "$imageType image",
    onClick: ((ImageViewerRouter.ImageSourceParam) -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val requestImageBuilder =
        rememberRequestImage(
            image = image,
            type = imageType,
        ) { toRequestBuilder(context, transformations) }
    val interactiveModifier =
        if (onClick != null || onLongClick != null || onDoubleClick != null) {
            modifier.combinedClickable(
                onClick = {
                    val source =
                        when (image) {
                            is IMediaCover -> {
                                if (imageType == BANNER) {
                                    image.banner
                                } else {
                                    image.extraLarge ?: image.large ?: image.medium
                                }
                            }
                            else -> image.large ?: image.medium
                        } ?: return@combinedClickable

                    onClick?.invoke(
                        ImageViewerRouter.ImageSourceParam(source),
                    )
                },
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
            )
        } else {
            modifier
        }

    AsyncImage(
        model = requestImageBuilder.build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = interactiveModifier,
    )
}

@AniTrendPreview.Default
@Composable
private fun AniTrendImagePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendImage(
            modifier = Modifier.padding(16.dp),
            image = MediaImage.empty().copy(color = "#e4a15d"),
            imageType = RequestImage.Media.ImageType.POSTER,
            onClick = {},
        )
    }
}
