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

import co.anitrend.arch.extension.empty
import java.util.*

/**
 * Represents application locales, we might have the same locale for different countries
 */
enum class AniTrendLocale(val language: String, val country: String? = null) {
    AUTOMATIC(String.empty()),
    ENGLISH_UK(
        language = Locale.UK.language,
        country = Locale.UK.country
    ),
    FRENCH_FRANCE(
        language = Locale.FRANCE.language,
        country = Locale.FRANCE.country
    )
}