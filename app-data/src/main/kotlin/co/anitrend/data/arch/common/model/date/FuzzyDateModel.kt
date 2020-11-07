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

import co.anitrend.data.arch.common.model.date.contract.IFuzzyDateModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class FuzzyDateModel(
    @SerialName("year") override val year: Int = UNKNOWN,
    @SerialName("month") override val month: Int = UNKNOWN,
    @SerialName("day") override val day: Int = UNKNOWN
) : IFuzzyDateModel {

    /**
     * Checks if all date fields are not set
     *
     * @return [Boolean] true if the date fields are set to [UNKNOWN] otherwise false
     */
    override fun isDateNotSet() =
        day == UNKNOWN && month == UNKNOWN && year == UNKNOWN

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

        internal fun IFuzzyDateModel?.orEmpty() = this ?: empty()
    }
}