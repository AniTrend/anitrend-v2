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

package co.anitrend.domain.common.entity.shared

/** FuzzyDate
 *
 * @param year Year annotated to have a minimum of 0
 * @param month Month annotated to have a minimum of 0 and maximum of 12
 * @param day Day annotated to have a minimum of 0 and maximum of 31
 */
data class FuzzyDate(
    val year: Int,
    val month: Int,
    val day: Int
) {

    /**
     * Checks if all date fields are not set
     *
     * @return [Boolean] true if the date fields are set to [UNKNOWN] otherwise false
     */
    fun isDateNotSet() = day == UNKNOWN && month == UNKNOWN && year == UNKNOWN

    companion object {
        /**
         * Default representation of null or not set
         */
        const val UNKNOWN = 0

        fun empty() = FuzzyDate(
            UNKNOWN,
            UNKNOWN,
            UNKNOWN
        )

        fun FuzzyDate?.orEmpty() = this ?: empty()
    }
}