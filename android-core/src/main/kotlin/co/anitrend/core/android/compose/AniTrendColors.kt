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
package co.anitrend.core.android.compose

import android.content.Context
import androidx.compose.material.Colors
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import co.anitrend.arch.extension.ext.getCompatColor

fun Context.primaryColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.primaryColor))

fun Context.secondaryColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.secondaryColor))

fun Context.backgroundColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.colorBackground))

fun Context.onBackgroundColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.colorOnBackground))

fun Context.primaryTextColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.primaryTextColor))

fun Context.secondaryTextColor() = Color(getCompatColor(co.anitrend.arch.theme.R.color.secondaryTextColor))

/**
 * Return the fully opaque color that results from compositing [onSurface] atop [surface] with the
 * given [alpha]. Useful for situations where semi-transparent colors are undesirable.
 */
@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

/**
 * Calculates the color of an elevated `surface` in dark mode. Returns `surface` in light mode.
 */
@Composable
fun Colors.elevatedSurface(elevation: Dp): Color {
    return LocalElevationOverlay.current?.apply(
        color = this.surface,
        elevation = elevation,
    ) ?: this.surface
}
