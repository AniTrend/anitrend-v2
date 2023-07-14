/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core.android.settings.helper.theme.model

import androidx.annotation.StyleRes
import co.anitrend.core.android.R

/**
 * Represents application themes
 *
 * @param styleAttribute The style resource attribute that will be applied
 * @param useNightMode If the current theme should also change the system to use night mode
 */
enum class AniTrendTheme(@StyleRes val styleAttribute: Int, val useNightMode: Boolean = false) {
    SYSTEM(
        styleAttribute = 0
    ),
    AMOLED(
        styleAttribute = R.style.AppTheme3,
        useNightMode = true
    ),
    LIGHT(
        styleAttribute = R.style.AppTheme3,
        useNightMode = false
    ),
    DARK(
        styleAttribute = R.style.AppTheme3_Dark,
        useNightMode = true
    )
}
