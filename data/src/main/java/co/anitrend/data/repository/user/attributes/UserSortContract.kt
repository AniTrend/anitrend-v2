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

package co.anitrend.data.repository.user.attributes

import androidx.annotation.StringDef

/**
 * User sort values
 */
@StringDef(
    UserSortContract.ID,
    UserSortContract.USERNAME,
    UserSortContract.WATCHED_TIME,
    UserSortContract.CHAPTERS_READ,
    UserSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class UserSortContract {
    companion object {
        const val ID = "ID"
        const val USERNAME = "USERNAME"
        const val WATCHED_TIME = "WATCHED_TIME"
        const val CHAPTERS_READ = "CHAPTERS_READ"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            USERNAME,
            WATCHED_TIME,
            CHAPTERS_READ,
            SEARCH_MATCH
        )
    }
}

@UserSortContract
typealias UserSort = String