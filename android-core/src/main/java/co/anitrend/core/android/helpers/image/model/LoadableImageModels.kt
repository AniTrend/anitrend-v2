/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.core.android.helpers.image.model

import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.domain.common.entity.contract.IMediaCover

sealed class RequestImage<T : ICoverImage>(
    val image: T?
)

class MediaRequestImage(
    mediaImage: IMediaCover?,
    val type: ImageType
) : RequestImage<IMediaCover>(mediaImage) {
    enum class ImageType {
        BANNER, POSTER
    }
}

class CoverRequestImage(
    coverImage: ICoverImage?
) : RequestImage<ICoverImage>(coverImage)

fun String?.toCoverImage(): ICoverImage {
    val resource = this
    return object : ICoverImage {
        override val large: CharSequence? = resource
        override val medium: CharSequence? = resource
    }
}

fun ICoverImage.toRequestImage(): CoverRequestImage {
    return CoverRequestImage(this)
}