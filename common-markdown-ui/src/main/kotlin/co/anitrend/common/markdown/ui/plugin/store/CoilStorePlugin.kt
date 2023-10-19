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

import android.content.Context
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.image.toCoverImage
import coil.request.Disposable
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import coil.transition.CrossfadeTransition
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.coil.CoilImagesPlugin

internal class CoilStorePlugin private constructor(
    private val context: Context
) : CoilImagesPlugin.CoilStore {

    override fun load(drawable: AsyncDrawable): ImageRequest {
        // TODO: Applying a custom non-string data item does not trigger our mapper
        val coverImage = drawable.destination.toCoverImage()
        val builder = ImageRequest.Builder(context)
            .transformations(RoundedCornersTransformation(4f.dp))
            .transitionFactory { target, result ->
                CrossfadeTransition(target, result, 300, true)
            }
            .crossfade(true)
            .data(drawable.destination)

        val width = drawable.imageSize?.width
        val height = drawable.imageSize?.height

        val size = if (width?.value != null && height?.value != null) {
            Size(
                width = width.value.toInt(),
                height = height.value.toInt()
            )
        } else Size.ORIGINAL

         builder.size(size)

        return builder.build()
    }

    override fun cancel(disposable: Disposable) {
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    companion object {
        fun create(context: Context) = CoilStorePlugin(context)
    }
}