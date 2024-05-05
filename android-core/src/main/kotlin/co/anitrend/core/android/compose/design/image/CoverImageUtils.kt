/*
 * Copyright (C) 2024 AniTrend
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
    builder: RequestImage<out ICoverImage>.() -> ImageRequest.Builder,
): ImageRequest.Builder =
    remember(image) {
        when (image) {
            is IMediaCover -> image.toMediaRequestImage(type)
            else -> image.toRequestImage()
        }.run(builder)
    }
