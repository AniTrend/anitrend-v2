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

package co.anitrend.data.repository.staff.attributes

import androidx.annotation.StringDef

/**
 * Staff sort values
 */
@StringDef(
    StaffSortContract.ID,
    StaffSortContract.ROLE,
    StaffSortContract.LANGUAGE,
    StaffSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StaffSortContract {
    companion object {
        const val ID = "ID"
        const val ROLE = "ROLE"
        const val LANGUAGE = "LANGUAGE"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ROLE,
            LANGUAGE,
            SEARCH_MATCH
        )
    }
}

@StaffSortContract
typealias StaffSort = String