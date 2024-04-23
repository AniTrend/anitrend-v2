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

package co.anitrend.core.android.settings.helper.theme

import android.annotation.TargetApi
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.theme.extensions.isEnvironmentNightMode
import co.anitrend.core.android.settings.common.theme.IThemeSettings
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import co.anitrend.core.android.settings.helper.theme.model.AniTrendTheme

/**
 * Theme utility for applying decorations at runtime
 *
 * @param settings instance of theme settings
 */
internal class ThemeHelper(private val settings: IThemeSettings) : IThemeHelper {

    /** Indicates weather the current theme is dynamic or static */
    override val dynamicColor: Boolean
        get() = settings.theme.value == AniTrendTheme.DYNAMIC

    @TargetApi(Build.VERSION_CODES.O)
    private fun FragmentActivity.applyNightModeDecorations(systemUiOptions: Int) {
        val primaryColor = getCompatColor(co.anitrend.arch.theme.R.color.colorPrimary)
        window.navigationBarColor = primaryColor
        window.statusBarColor = primaryColor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS and WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS and WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = systemUiOptions and
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR  and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun FragmentActivity.applyDayModeDecorations(systemUiOptions: Int) {
        val primaryColor = getCompatColor(co.anitrend.arch.theme.R.color.colorPrimary)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = WindowCompat.getInsetsController(window, window.decorView)
            controller.isAppearanceLightNavigationBars = true
            controller.isAppearanceLightStatusBars = true
            window.statusBarColor = primaryColor
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = systemUiOptions or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.navigationBarColor = primaryColor
            window.statusBarColor = primaryColor
        }
    }


    private fun FragmentActivity.applyWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @Suppress("DEPRECATION") //Until I figure out a way to apply decorations using window controller
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
    override fun applyDynamicNightModeFromTheme() {
        val theme = settings.theme.value
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

    /**
     * Applies settings theme resource or provided [themeOverride] which overrides settings
     */
    override fun applyApplicationTheme(
        context: FragmentActivity,
        themeOverride: Int?
    ) {
        if (themeOverride != null)
            context.setTheme(themeOverride)
        else
            context.setTheme(settings.theme.value.styleAttribute)
        context.applyWindowStyle()
    }
}
