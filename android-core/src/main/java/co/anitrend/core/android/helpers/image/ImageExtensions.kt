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

package co.anitrend.core.android.helpers.image

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.core.android.R
import co.anitrend.core.android.helpers.color.asDrawable
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.domain.common.entity.contract.ICoverImage
import co.anitrend.domain.common.entity.contract.IMediaCover
import coil.Coil
import coil.request.Disposable
import coil.request.ImageRequest
import coil.target.Target
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import coil.transition.CrossfadeTransition

/**
 * String to cover image converter
 */
fun String?.toCoverImage(): ICoverImage {
    val resource = this
    return object : ICoverImage {
        override val large: CharSequence? = resource
        override val medium: CharSequence? = resource
    }
}

/**
 * Cover image to request image converter
 */
fun ICoverImage.toRequestImage() = RequestImage.Cover(this)

/**
 * Media cover to request image converter
 */
fun IMediaCover.toMediaRequestImage(
    type: RequestImage.Media.ImageType
) = RequestImage.Media(this, type)

/**
 * Draws an image onto the image view
 *
 * @param requestImage Request image model
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun AppCompatImageView.using(
    requestImage: RequestImage<*>,
    transformations: List<Transformation> = emptyList()
): Disposable {
    val requestBuilder = ImageRequest.Builder(context)

    if (requestImage is RequestImage.Media) {
        val color = requestImage.image?.color
        if (color != null) {
            // TODO: Apply corner radius to placeholder as well
            requestBuilder.placeholder(
                color.asDrawable(context)
            )
        }
    }

    if (transformations.isNotEmpty())
        requestBuilder.transformations(transformations)
    else {
        val radius = resources.getDimensionPixelSize(R.dimen.sm_margin).toFloat()
        RoundedCornersTransformation(
            bottomLeft = radius,
            bottomRight = radius
        )
    }

    val request = requestBuilder
        .transition(
            CrossfadeTransition(
                resources.getInteger(
                    R.integer.motion_duration_large
                )
            )
        )
        .data(requestImage)
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}

/**
 * Draws an image onto the image view
 *
 * @param resource resource to load of type [Drawable]
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun AppCompatImageView.using(
    resource: Drawable?,
    transformations: List<Transformation> = emptyList()
): Disposable {
    val requestBuilder = ImageRequest.Builder(context)

    if (transformations.isNotEmpty())
        requestBuilder.transformations(transformations)

    val request = requestBuilder
        .transition(
            CrossfadeTransition(
                resources.getInteger(R.integer.motion_duration_large)
            )
        )
        .data(resource)
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}

/**
 * Draws an image onto the image view
 *
 * @param resource resource of type [DrawableRes]
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun AppCompatImageView.using(
    @DrawableRes resource: Int,
    transformations: List<Transformation> = emptyList()
): Disposable {
    val drawable = context.getCompatDrawable(resource)
    return using(resource = drawable, transformations)
}

/**
 * Draws an image onto the image view
 *
 * @param resource resource to load of any sub type of [ICoverImage]
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun <T: ICoverImage> AppCompatImageView.using(
    resource: T?,
    transformations: List<Transformation> = emptyList()
): Disposable {
    val requestBuilder = ImageRequest.Builder(context)

    if (transformations.isNotEmpty())
        requestBuilder.transformations(transformations)

    val request = requestBuilder
        .transition(
            CrossfadeTransition(
                resources.getInteger(R.integer.motion_duration_large)
            )
        )
        .data(resource?.toRequestImage())
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}

/**
 * Draws an image onto the image view
 *
 * @param resource resource to load of any sub type of [ICoverImage]
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun <T: ICoverImage> Target.using(
    resource: T?,
    context: Context,
    transformations: List<Transformation> = emptyList()
): Disposable {
    val requestBuilder = ImageRequest.Builder(context)

    if (transformations.isNotEmpty())
        requestBuilder.transformations(transformations)

    val request = requestBuilder
        .transition(
            CrossfadeTransition(
                context.resources.getInteger(R.integer.motion_duration_large)
            )
        )
        .data(resource?.toRequestImage())
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}