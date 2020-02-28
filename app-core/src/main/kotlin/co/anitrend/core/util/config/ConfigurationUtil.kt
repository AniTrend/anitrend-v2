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

import co.anitrend.core.settings.common.IConfigurationSettings
import co.anitrend.core.ui.activity.AnitrendActivity
import co.anitrend.core.util.config.contract.IConfigurationUtil
import co.anitrend.core.util.locale.AniTrendLocale
import co.anitrend.core.util.locale.LocaleUtil
import co.anitrend.core.util.theme.AniTrendTheme
import co.anitrend.core.util.theme.ThemeUtil

/**
 * Configuration helper for the application
 */
class ConfigurationUtil(
    private val settings: IConfigurationSettings,
    private val localeUtil: LocaleUtil,
    private val themeUtil: ThemeUtil
) : IConfigurationUtil {

    override val moduleTag: String = this::class.java.simpleName

    // we might change this in future when we have more themes
    private var applicationTheme = AniTrendTheme.SYSTEM
    private var applicationLocale = AniTrendLocale.AUTOMATIC

    /**
     * Applies configuration upon the create state of the current activity
     *
     * @param activity
     */
    override fun onCreate(activity: AnitrendActivity<*, *>) {
        applicationTheme = settings.theme
        applicationLocale = settings.locale
        localeUtil.applyApplicationLocale(activity)
        themeUtil.applyApplicationTheme(activity)
    }

    /**
     * Applies configuration upon the resume state of the current activity
     *
     * @param activity
     */
    override fun onResume(activity: AnitrendActivity<*, *>) {
        if (applicationTheme != settings.theme) {
            activity.resetActivity()
            themeUtil.applyNightMode()
        }
    }


    private fun AnitrendActivity<*, *>.resetActivity() {
        val currentIntent = intent
        finish()
        invoke()
        startActivity(currentIntent)
        invoke()
    }

    companion object {
        private operator fun AnitrendActivity<*, *>.invoke() {
            overridePendingTransition(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }
    }
}