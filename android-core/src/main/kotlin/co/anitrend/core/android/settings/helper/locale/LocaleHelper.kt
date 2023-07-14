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

package co.anitrend.core.android.settings.helper.locale

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import co.anitrend.core.android.settings.common.locale.ILocaleSettings
import co.anitrend.core.android.settings.helper.locale.contract.ILocaleHelper
import co.anitrend.core.android.settings.helper.locale.model.AniTrendLocale
import java.util.Locale

internal class LocaleHelper(private val settings: ILocaleSettings) : ILocaleHelper {

    /**
     * Current locale
     */
    override val locale: Locale
        get() {
            if (settings.locale.value == AniTrendLocale.AUTOMATIC)
                return Locale.getDefault()
            return getPersonalizedLocale()
        }

    /**
     * Applies locale to [AppCompatDelegate]
     *
     * @see AppCompatDelegate.setApplicationLocales
     */
    override fun applyLocale() {
        val localeList = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    /**
     * Applies locale to context configuration
     */
    override fun applyLocale(context: Context?): Context? {
        Locale.setDefault(locale)
        val resources = context?.resources
        val configuration = Configuration(resources?.configuration)
        configuration.setLocale(locale)
        return context?.createConfigurationContext(configuration)
    }

    private fun getPersonalizedLocale(): Locale {
        val locale = settings.locale.value
        if (locale.country == null)
            return Locale(locale.language)
        return Locale(locale.language, locale.country)
    }
}
