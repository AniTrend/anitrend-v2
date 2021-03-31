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

package co.anitrend.media.discover.filter.extensions

import android.content.Context
import co.anitrend.media.discover.filter.R
import com.airbnb.paris.extensions.style
import com.google.android.material.chip.Chip


internal inline fun Context.withChip(
    action: Chip.() -> Unit
): Chip {
    val chip = Chip(this)
    chip.style(R.style.AppTheme_Material_Chip_Choice)
    chip.setChipBackgroundColorResource(
        R.color.selector_chip_background
    )
    chip.isCheckedIconVisible = false
    chip.isCheckable = true
    action(chip)
    return chip
}

internal inline fun Chip.withChip(
    action: Chip.() -> Unit
): Chip {
    style(R.style.AppTheme_Material_Chip_Choice)
    setChipBackgroundColorResource(
        R.color.selector_chip_background
    )
    isCheckedIconVisible = false
    isCheckable = true
    action(this)
    return this
}
