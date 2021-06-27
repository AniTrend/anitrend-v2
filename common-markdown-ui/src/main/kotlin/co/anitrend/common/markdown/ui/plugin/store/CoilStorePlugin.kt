/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.common.markdown.ui.plugin.store

import co.anitrend.core.android.helpers.image.toCoverImage
import coil.request.Disposable
import coil.request.ImageRequest
import coil.size.OriginalSize
import coil.size.PixelSize
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.coil.CoilImagesPlugin

internal class CoilStorePlugin private constructor(
    private val requestBuilder: ImageRequest.Builder
) : CoilImagesPlugin.CoilStore {

    override fun load(drawable: AsyncDrawable): ImageRequest {
        // TODO: Applying a custom non-string data item does not trigger our mapper
        val coverImage = drawable.destination.toCoverImage()
        val builder = requestBuilder
            .data(drawable.destination)

        val width = drawable.imageSize?.width
        val height = drawable.imageSize?.height

        val size = if (width?.value != null && height?.value != null) {
            PixelSize(
                width = width.value.toInt(),
                height = height.value.toInt()
            )
        } else OriginalSize

         builder.size(size)

        return builder.build()
    }

    override fun cancel(disposable: Disposable) {
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    companion object {
        fun create(
            requestBuilder: ImageRequest.Builder
        ) = CoilStorePlugin(requestBuilder)
    }
}