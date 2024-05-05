/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.core.android.ui.theme.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import co.anitrend.core.android.ui.theme.AniTrendTheme3

private val PreviewThemeHelper =
    object : IThemeHelper {
        /**
         * Sets the default night mode based on the theme set in settings
         */
        override fun applyDynamicNightModeFromTheme() {}

        /**
         * Applies settings theme resource or provided [themeOverride] which overrides settings
         */
        override fun applyApplicationTheme(
            context: FragmentActivity,
            themeOverride: Int?,
        ) {}
    }

@Composable
fun PreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    AniTrendTheme3(
        darkTheme = darkTheme,
        themeHelper = PreviewThemeHelper,
        dynamicColor = dynamicColor,
        content = content,
    )
}
