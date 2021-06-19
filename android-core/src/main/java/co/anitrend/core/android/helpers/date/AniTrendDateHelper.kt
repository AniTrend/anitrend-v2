/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.android.helpers.date

import androidx.annotation.IntRange
import co.anitrend.arch.extension.util.attribute.SeasonType
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import java.util.*

class AniTrendDateHelper : AbstractSupportDateHelper() {

    /**
     * Returns the current month in the form of an [Int].
     *
     * @return [IntRange] between 0 - 11
     */
    val month: Int
        @IntRange(from = 0, to = 11) get() =
            Calendar.getInstance().get(Calendar.MONTH)

    /**
     * Returns the current day in the form of an [Int].
     *
     * @return [IntRange] between 0 - 30
     */
    val day: Int
        @IntRange(from = 0, to = 30) get() =
            Calendar.getInstance().get(Calendar.DATE)

    /**
     * Returns the current year
     */
    val year: Int
        get() = Calendar.getInstance().get(Calendar.YEAR)

    /**
     * @return [FuzzyDate]
     */
    fun fuzzyDateNow() = FuzzyDate(
        day = day.plus(1),
        month = month.plus(1),
        year = year.plus(1)
    )

    /**
     * @return current seasons name
     */
    override val currentSeason: SeasonType
        get() {
            return when (month) {
                in 2..4 -> SeasonType.SPRING
                in 5..7 -> SeasonType.SUMMER
                in 8..10 -> SeasonType.FALL
                else -> SeasonType.WINTER
            }
        }

    /**
     * Gets the current year + delta, if the season for the year is winter later in the year
     * then the result would be the current year plus the delta
     *
     * @return current year with a given delta
     */
    override fun getCurrentYear(delta: Int): Int {
        return if (month >= 11 && currentSeason == SeasonType.WINTER)
            year + delta
        else year
    }
}