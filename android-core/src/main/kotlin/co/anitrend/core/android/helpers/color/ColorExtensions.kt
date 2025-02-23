/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.core.android.helpers.color

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.domain.common.HexColor

/** Checks if a color has enough contrast */
fun Int.hasEnoughContrastFor(
    @ColorInt backgroundColor: Int,
) = ColorUtils.calculateContrast(
    this,
    backgroundColor,
) > 1.8f

/** Decreases the lightness of the color by the [factor] */
fun Int.increaseContrastBy(factor: Float): Int {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this, hsl)
    hsl[2] = hsl[2] * factor
    return ColorUtils.HSLToColor(hsl)
}

/**
 * Convert a hex color and return the corresponding color-int
 *
 * @param backgroundColor Current background color to allow auto adjusting contrast
 */
@ColorInt
fun HexColor.asColorInt(
    @ColorInt backgroundColor: Int,
): Int {
    val foregroundColor = Color.parseColor(toString())
    return if (foregroundColor.hasEnoughContrastFor(backgroundColor)) {
        foregroundColor
    } else {
        foregroundColor.increaseContrastBy(0.5f)
    }
}

/**
 * Convert a hex color and return the corresponding color-int
 *
 * @param context Used to retrieve current theme background color for contrast fixes
 */
@ColorInt
fun HexColor.asColorInt(context: Context): Int {
    val backgroundColor = context.getCompatColor(co.anitrend.arch.theme.R.color.colorBackground)
    return asColorInt(backgroundColor)
}

/**
 * Creates a new ColorDrawable with the specified color.
 *
 * @param context Used to retrieve current theme background color for contrast fixes
 */
fun HexColor.asDrawable(context: Context) = ColorDrawable(asColorInt(context))
