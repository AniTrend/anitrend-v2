package co.anitrend.core.android.compose.design.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.toRequestImage
import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.domain.common.entity.contract.IMediaCover
import coil.request.ImageRequest

@Composable
fun rememberRequestImage(
    image: ICoverImage,
    type: RequestImage.Media.ImageType,
    builder: RequestImage<out ICoverImage>.() -> ImageRequest.Builder
): ImageRequest.Builder = remember(image) {
    when (image) {
        is IMediaCover -> image.toMediaRequestImage(type)
        else -> image.toRequestImage()
    }.run(builder)
}
