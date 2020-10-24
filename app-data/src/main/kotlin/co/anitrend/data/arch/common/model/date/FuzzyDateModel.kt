/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.arch.common.model.date

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [FuzzyDate](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydate.doc.html)
 * Date object that allows for incomplete date values (fuzzy) Unknown dates represented by 0.
 *
 * @param year Year annotated to have a minimum of 0
 * @param month Month annotated to have a minimum of 0 and maximum of 12
 * @param day Day annotated to have a minimum of 0 and maximum of 31
 */
@Serializable
internal data class FuzzyDateModel(
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("day") val day: Int
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
        internal const val UNKNOWN = 0

        internal fun empty() = FuzzyDateModel(
            UNKNOWN,
            UNKNOWN,
            UNKNOWN
        )

        internal fun FuzzyDateModel?.orEmpty() = this ?: empty()
    }
}