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

import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import co.anitrend.arch.extension.getCompatColor
import co.anitrend.core.R
import co.anitrend.core.settings.common.theme.IThemeSettings
import co.anitrend.core.ui.activity.AnitrendActivity
import timber.log.Timber

class ThemeUtil(private val settings: IThemeSettings) {

    @get:StyleRes
    val theme: Int
        get() {
            if (
                settings.theme == AniTrendTheme.SYSTEM ||
                settings.theme == AniTrendTheme.AMOLED ||
                settings.theme == AniTrendTheme.LIGHT
            ) return R.style.AppTheme

            return getPersonalizedTheme()
        }


    private fun AnitrendActivity<*, *>.applyWindowStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val systemUiOptions = window.decorView.systemUiVisibility
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_NO -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
                AppCompatDelegate.MODE_NIGHT_YES -> {
                    window.navigationBarColor = getCompatColor(R.color.colorPrimary)
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    window.decorView.systemUiVisibility = systemUiOptions and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
                else -> {
                    Timber.w("Follow system night mode might be deprecated in future")
                    // According to Google/IO other ui options like auto and follow system might be deprecated
                }
            }
        }
    }

    internal fun applyNightMode() {
        val theme = settings.theme
        if (theme == AniTrendTheme.SYSTEM)
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        else
            when (isThemeLight(settings.theme)) {
                true -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
                else -> AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
    }

    internal fun applyApplicationTheme(context: AnitrendActivity<*, *>) {
        context.setTheme(theme)
        context.applyWindowStyle()
    }

    private fun isThemeLight(theme: AniTrendTheme): Boolean {
        return when (theme) {
            AniTrendTheme.AMOLED,
            AniTrendTheme.DARK -> false
            else -> true
        }
    }

    @StyleRes
    private fun getPersonalizedTheme(): Int {
        if (settings.theme == AniTrendTheme.DARK)
            TODO("Set up other custom themes")

        return 0
    }
}