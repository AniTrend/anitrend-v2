package co.anitrend.core.android.compose.design.image

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.model.RequestImage.Media.ImageType.*
import co.anitrend.core.android.helpers.image.toRequestBuilder
import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.navigation.ImageViewerRouter
import coil.compose.AsyncImage
import coil.transform.Transformation

object AniTrendImageDefaults {
    val BANNER_SIZE = Modifier.height(200.dp)
}

/**
 * Custom [AsyncImage] for specific use cases that uses auto image quality selection based on device power requirements
 * see [co.anitrend.core.android.controller.power.contract.IPowerController]
 *
 * @param image Resource to load, if literal use [co.anitrend.core.android.helpers.image.toCoverImage]
 * @param modifier Default modifier that will have a default clickable modifier that invokes [onClick]
 * @param imageType The type of image, this will make some behavioural changes
 * @param transformations Image transformations for Coil
 * @param contentScale [ContentScale] defaulted to [ContentScale.Crop]
 * @param onClick Callback with a receiver of [ImageViewerRouter.ImageSourceParam]
 *
 * @see [co.anitrend.core.android.helpers.image]
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AniTrendImage(
    image: ICoverImage,
    imageType: RequestImage.Media.ImageType,
    modifier: Modifier = Modifier,
    transformations: List<Transformation> = emptyList(),
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (ImageViewerRouter.ImageSourceParam) -> Unit,
    onLongClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val requestImageBuilder = rememberRequestImage(
        image = image,
        type = imageType
    ) { toRequestBuilder(context, transformations) }

    AsyncImage(
        model = requestImageBuilder.build(),
        contentDescription = "$imageType image",
        contentScale = contentScale,
        modifier = modifier.combinedClickable(
            onClick = {
                val source = when (image) {
                    is IMediaCover -> {
                        if (imageType == BANNER) image.banner
                        else image.extraLarge ?: image.large ?: image.medium
                    }
                    else -> image.large ?: image.medium
                } ?: return@combinedClickable

                onClick(
                    ImageViewerRouter.ImageSourceParam(source)
                )
            },
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
        ),
    )
}
