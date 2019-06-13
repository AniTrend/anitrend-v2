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

package co.anitrend.data.repository.studio.attributes

import androidx.annotation.StringDef

/**
 * Studio sort values
 */
@StringDef(
    StudioSortContract.ID,
    StudioSortContract.ID_DESC,
    StudioSortContract.NAME,
    StudioSortContract.NAME_DESC,
    StudioSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class StudioSortContract {
    companion object {
        const val ID = "ID"
        const val ID_DESC = "ID_DESC"
        const val NAME = "NAME"
        const val NAME_DESC = "NAME_DESC"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            ID_DESC,
            NAME,
            NAME_DESC,
            SEARCH_MATCH
        )
    }
}

@StudioSortContract
typealias StudioSort = String