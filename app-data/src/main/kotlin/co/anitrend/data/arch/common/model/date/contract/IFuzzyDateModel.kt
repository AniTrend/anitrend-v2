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

package co.anitrend.data.arch.common.model.date.contract

import co.anitrend.data.arch.common.model.date.FuzzyDateModel.Companion.UNKNOWN

/** [FuzzyDate](https://anilist.github.io/ApiV2-GraphQL-Docs/fuzzydate.doc.html)
 * Date object that allows for incomplete date values (fuzzy) Unknown dates represented by 0.
 *
 * @property year Year annotated to have a minimum of 0
 * @property month Month annotated to have a minimum of 0 and maximum of 12
 * @property day Day annotated to have a minimum of 0 and maximum of 31
 */
internal interface IFuzzyDateModel {
    val year: Int
    val month: Int
    val day: Int

    /**
     * Checks if all date fields are not set
     *
     * @return [Boolean] true if the date fields are set to [UNKNOWN] otherwise false
     */
    fun isDateNotSet(): Boolean
}