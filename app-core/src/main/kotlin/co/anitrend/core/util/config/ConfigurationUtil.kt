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

package co.anitrend.core.util.config

import android.content.Context
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.util.config.contract.IConfigurationUtil
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.locale.LocaleHelper
import co.anitrend.core.util.theme.AniTrendTheme
import co.anitrend.core.util.theme.ThemeHelper

/**
 * Configuration helper for the application
 */
class ConfigurationUtil(
    private val settings: IConfigurationSettings,
    private val localeHelper: LocaleHelper,
    private val themeHelper: ThemeHelper
) : IConfigurationUtil {

    override val moduleTag: String = ConfigurationUtil::class.java.simpleName

    // we might change this in future when we have more themes
    private var applicationTheme = AniTrendTheme.SYSTEM
    private var applicationLocale = AniTrendLocale.AUTOMATIC

    fun attachContext(context: Context?) =
        localeHelper.applyLocale(context)

    /**
     * Applies configuration upon the create state of the current activity
     *
     * @param activity
     */
    override fun onCreate(activity: FragmentActivity) {
        applicationTheme = settings.theme
        applicationLocale = settings.locale
        //localeHelper.applyApplicationLocale(activity)
        themeHelper.applyApplicationTheme(activity)
    }

    /**
     * Applies configuration upon the resume state of the current activity
     *
     * @param activity
     */
    override fun onResume(activity: FragmentActivity) {
        if (applicationTheme != settings.theme) {
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