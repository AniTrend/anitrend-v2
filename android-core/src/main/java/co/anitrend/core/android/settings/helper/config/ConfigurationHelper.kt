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

package co.anitrend.core.android.settings.helper.config

import android.content.Context
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.settings.common.IConfigurationSettings
import co.anitrend.core.android.settings.helper.config.contract.IConfigurationHelper
import co.anitrend.core.android.settings.helper.locale.AniTrendLocale
import co.anitrend.core.android.settings.helper.locale.contract.ILocaleHelper
import co.anitrend.core.android.settings.helper.theme.AniTrendTheme
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper

/**
 * Configuration helper for the application
 */
internal class ConfigurationHelper(
    private val settings: IConfigurationSettings,
    private val localeHelper: ILocaleHelper,
    private val themeHelper: IThemeHelper
) : IConfigurationHelper {

    override val moduleTag: String = ConfigurationHelper::class.java.simpleName

    @StyleRes
    override var themeOverride: Int? = null

    // we might change this in future when we have more themes
    private var applicationTheme = AniTrendTheme.SYSTEM
    private var applicationLocale = AniTrendLocale.AUTOMATIC

    /**
     * Creates a new context with configuration
     */
    override fun attachContext(context: Context?) = localeHelper.applyLocale(context)

    /**
     * Applies configuration upon the create state of the current activity
     *
     * @param activity
     */
    override fun onCreate(activity: FragmentActivity) {
        applicationTheme = settings.theme.value
        applicationLocale = settings.locale.value
        themeHelper.applyApplicationTheme(activity, themeOverride)
    }

    /**
     * Applies configuration upon the resume state of the current activity
     *
     * @param activity
     */
    override fun onResume(activity: FragmentActivity) {
        if (applicationTheme != settings.theme.value) {
            //activity.recreate()
            activity.resetActivity()
            themeHelper.applyDynamicNightModeFromTheme()
        }
    }

    private fun FragmentActivity.resetActivity() {
        val currentIntent = intent
        finish()
        invoke()
        startActivity(currentIntent)
        invoke()
    }

    companion object {
        private operator fun FragmentActivity.invoke() {
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}