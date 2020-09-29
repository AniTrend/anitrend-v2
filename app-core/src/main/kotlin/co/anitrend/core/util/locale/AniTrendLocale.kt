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

import co.anitrend.arch.extension.ext.empty
import java.util.*

/**
 * Represents application locales, we might have the same locale for different countries so we
 * define each locale variant explicitly that we support.
 *
 * _**N.B** [country] is optional, this means if you do not wish use a specific
 * locale you will only specify the [language] despite the country_
 *
 * @param language 2-letter language code as per **ISO 639**
 * @param country Uppercase 2-letter country code as per **ISO 3166**
 *
 * @see Locale
 */
enum class AniTrendLocale(val language: String, val country: String? = null) {
    AUTOMATIC(String.empty()),
    GERMAN_GERMANY(
        language = Locale.GERMAN.language,
        country = Locale.GERMAN.country
    ),
    ITALIAN_ITALY(
        language = Locale.ITALY.language,
        country = Locale.ITALY.country
    ),
    FRENCH_FRANCE(
        language = Locale.FRANCE.language,
        country = Locale.FRANCE.country
    )
}