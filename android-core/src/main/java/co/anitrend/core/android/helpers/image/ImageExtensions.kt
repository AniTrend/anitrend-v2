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

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import co.anitrend.arch.ui.view.image.SupportImageView
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.model.MediaRequestImage
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.request.LoadRequest
import coil.request.RequestDisposable
import coil.transition.CrossfadeTransition


@OptIn(ExperimentalCoilApi::class)
fun SupportImageView.using(requestImage: RequestImage<*>): RequestDisposable? {
    val requestBuilder = LoadRequest.Builder(context)

    if (requestImage is MediaRequestImage) {
        val color = requestImage.image?.color
        if (color != null) {
            val colorInt = Color.parseColor(color)
            val colorDrawable = ColorDrawable(colorInt)
            requestBuilder.placeholder(colorDrawable)
        }
    }

    val request = requestBuilder
        .transition(CrossfadeTransition(350))
        .data(requestImage).target(this).build()

    return Coil.imageLoader(context).execute(request)
}