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

package co.anitrend.core.util.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import co.anitrend.core.settings.common.locale.ILocaleSettings
import java.util.*

class LocaleHelper(private val settings: ILocaleSettings) {

    val locale: Locale
        get() {
            if (settings.locale == AniTrendLocale.AUTOMATIC)
                return Locale.getDefault()
            return getPersonalizedLocale()
        }

    internal fun applyLocale(context: Context?): Context? {
        Locale.setDefault(locale)
        val resources = context?.resources
        val configuration = Configuration(resources?.configuration)
        configuration.setLocale(locale)
        return context?.createConfigurationContext(configuration)
    }

    @Deprecated(
        "Use applyLocale instead",
        ReplaceWith("applyLocale"),
        DeprecationLevel.WARNING
    )
    internal fun applyApplicationLocale(context: Context) {
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration = Configuration(resources.configuration)
        @Suppress("DEPRECATION")
        configuration.locale = locale
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun getPersonalizedLocale(): Locale {
        val locale = settings.locale
        if (locale.country == null)
            return Locale(locale.language)
        return Locale(locale.language, locale.language)
    }
}