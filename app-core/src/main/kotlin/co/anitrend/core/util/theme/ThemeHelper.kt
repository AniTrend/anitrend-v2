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

package co.anitrend.core.util.theme

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.core.R
import co.anitrend.core.extensions.isEnvironmentNightMode
import co.anitrend.core.settings.common.theme.IThemeSettings

/**
 * Theme utility for applying decorations at runtime
 *
 * @param settings instance of theme settings
 */
@Suppress("DEPRECATION")
class ThemeHelper(private val settings: IThemeSettings) {

    @TargetApi(Build.VERSION_CODES.O)
    private fun FragmentActivity.applyNightModeDecorations(systemUiOptions: Int) {
        val primaryColor = getCompatColor(R.color.colorPrimary)
        window.navigationBarColor = primaryColor
        window.statusBarColor = primaryColor
        window.decorView.systemUiVisibility = systemUiOptions and
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR  and
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun FragmentActivity.applyDayModeDecorations(systemUiOptions: Int) {
        val primaryColor = getCompatColor(R.color.colorPrimary)
        window.navigationBarColor = primaryColor
        window.statusBarColor = primaryColor
        window.decorView.systemUiVisibility = systemUiOptions or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


    private fun FragmentActivity.applyWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val systemUiOptions = window.decorView.systemUiVisibility
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> applyDayModeDecorations(systemUiOptions)
                AppCompatDelegate.MODE_NIGHT_YES -> applyNightModeDecorations(systemUiOptions)
                else -> {
                    // According to Google/IO other ui options like auto and follow system
                    // will be deprecated in the future
                    if (isEnvironmentNightMode())
                        applyNightModeDecorations(systemUiOptions)
                    else applyDayModeDecorations(systemUiOptions)
                }
            }
        }
    }

    /**
     * Sets the default night mode based on the theme set in settings
     */
    internal fun applyDynamicNightModeFromTheme() {
        val theme = settings.theme
        if (theme == AniTrendTheme.SYSTEM)
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        else
            when (theme.useNightMode) {
                true -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
                else -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
    }

    internal fun applyApplicationTheme(context: FragmentActivity) {
        context.setTheme(settings.theme.styleAttribute)
        context.applyWindowStyle()
    }
}