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

package co.anitrend.core.android.settings.helper.theme.contract

import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity

interface IThemeHelper {

    /**
     * Sets the default night mode based on the theme set in settings
     */
    fun applyDynamicNightModeFromTheme()

    /**
     * Applies settings theme resource or provided [themeOverride] which overrides settings
     */
    fun applyApplicationTheme(
        context: FragmentActivity,
        @StyleRes themeOverride: Int? = null
    )
}