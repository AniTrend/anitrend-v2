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
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import co.anitrend.core.android.R
import co.anitrend.core.android.helpers.image.model.MediaRequestImage
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.domain.common.HexColor
import coil.Coil
import coil.request.Disposable
import coil.request.ImageRequest
import coil.target.Target
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import coil.transition.CrossfadeTransition

/**
 * Convert a hex color and return the corresponding color-int
 */
@ColorInt
fun HexColor.toColorInt(): Int {
    // TODO: increase colour contrast if the shade is below 500 e.g shows like Dr. Stone have poor contrast especially in light themes
    return Color.parseColor(toString())
}

/**
 * Creates a new ColorDrawable with the specified color.
 */
fun HexColor.toDrawable() = ColorDrawable(toColorInt())

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

    if (requestImage is MediaRequestImage) {
        val color = requestImage.image?.color
        if (color != null)
            requestBuilder.placeholder(color.toDrawable())
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
 * @param resource resource to load
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun <T: Any> AppCompatImageView.using(
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
        .data(resource)
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}

/**
 * Draws an image onto the image view
 *
 * @param resource resource to load
 * @param transformations Optional image transformations, providing this with an empty list will
 * bypass the default [RoundedCornersTransformation] on bottom corners.
 *
 * @return A [Disposable] contract
 */
fun <T: Any> Target.using(
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
        .data(resource)
        .target(this)
        .build()

    return Coil.imageLoader(context).enqueue(request)
}