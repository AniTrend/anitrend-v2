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
package co.anitrend.core.android.extensions

import android.content.res.Resources
import java.util.Locale
import kotlin.math.ln
import kotlin.math.pow

fun Int.toHumanReadableQuantity(digits: Int = 1): String {
    val quantity = this
    val unit = 1000
    if (quantity < unit) return "$quantity"
    val exp = (ln(quantity.toDouble()) / ln(unit.toDouble())).toInt()
    val suffix = "KMGTPE"[exp - 1].toString()
    return String.format(
        Locale.getDefault(),
        "%.${digits}f %s",
        quantity / unit.toDouble().pow(exp.toDouble()),
        suffix,
    )
}

/**
 * Helper value formatter
 *
 * @param digits Number of decimal places
 *
 * @author [Andrey Breslav](https://stackoverflow.com/a/23088000/1725347)
 */
fun Double.format(digits: Int) = "%.${digits}f".format(this)

/**
 * Helper value formatter for [Float] types
 *
 * @param digits Number of decimal places
 */
fun Float.format(digits: Int) = "%.${digits}f".format(this)

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.px: Float
    get() = (this / Resources.getSystem().displayMetrics.density + 0.5f)

val Int.px: Int
    get() = (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()
