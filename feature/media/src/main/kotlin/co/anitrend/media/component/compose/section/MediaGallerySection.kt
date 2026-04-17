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
package co.anitrend.media.component.compose.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.domain.media.entity.attribute.image.MediaGalleryImage
import co.anitrend.media.R
import co.anitrend.navigation.ImageViewerRouter
import coil.compose.AsyncImage

private val GalleryCardWidth = 220.dp

@Composable
internal fun MediaGallerySection(
    images: List<MediaGalleryImage>,
    onImageClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val galleryImages = remember(images) { images.distinctBy(MediaGalleryImage::url) }
    if (galleryImages.isEmpty()) {
        return
    }

    MediaHubSection(
        title = stringResource(R.string.title_media_gallery_section),
        subtitle = stringResource(R.string.subtitle_media_gallery_section),
        modifier = modifier,
    ) {
        MediaSectionRail(contentPadding = PaddingValues(vertical = 2.dp)) {
            items(galleryImages.size, key = { index -> galleryImages[index].url }) { index ->
                val image = galleryImages[index]
                GalleryCard(
                    image = image,
                    onClick = {
                        onImageClick(
                            ImageViewerRouter.ImageSourceParam(
                                imageSrc = image.url,
                                imageSources = galleryImages.map(MediaGalleryImage::url),
                                initialIndex = index,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun GalleryCard(
    image: MediaGalleryImage,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.width(GalleryCardWidth),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f)),
    ) {
        Column(
            modifier = Modifier.clickable(onClick = onClick).padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            ) {
                AsyncImage(
                    model = image.url,
                    contentDescription = galleryImageLabel(image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 10f),
                )
            }

            Text(
                text = galleryImageLabel(image),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            image.locale
                ?.takeIf(String::isNotBlank)
                ?.let { locale ->
                    Text(
                        text = locale,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
        }
    }
}

@Composable
private fun galleryImageLabel(image: MediaGalleryImage): String =
    when (image.type) {
        MediaGalleryImage.Type.BACKDROP -> stringResource(R.string.label_media_gallery_type_backdrop)
        MediaGalleryImage.Type.LOGO -> stringResource(R.string.label_media_gallery_type_logo)
        MediaGalleryImage.Type.POSTER -> stringResource(R.string.label_media_gallery_type_poster)
    }
