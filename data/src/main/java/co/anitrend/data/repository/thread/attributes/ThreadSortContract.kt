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

package co.anitrend.data.repository.thread.attributes

import androidx.annotation.StringDef

/**
 * Thread sort values
 */
@StringDef(
    ThreadSortContract.ID,
    ThreadSortContract.TITLE,
    ThreadSortContract.CREATED_AT,
    ThreadSortContract.UPDATED_AT,
    ThreadSortContract.REPLIED_AT,
    ThreadSortContract.REPLY_COUNT,
    ThreadSortContract.VIEW_COUNT,
    ThreadSortContract.IS_STICKY,
    ThreadSortContract.SEARCH_MATCH
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ThreadSortContract {
    companion object {
        const val ID = "ID"
        const val TITLE = "TITLE"
        const val CREATED_AT = "CREATED_AT"
        const val UPDATED_AT = "UPDATED_AT"
        const val REPLIED_AT = "REPLIED_AT"
        const val REPLY_COUNT = "REPLY_COUNT"
        const val VIEW_COUNT = "VIEW_COUNT"
        const val IS_STICKY = "IS_STICKY"
        const val SEARCH_MATCH = "SEARCH_MATCH"

        val ALL = listOf(
            ID,
            TITLE,
            CREATED_AT,
            UPDATED_AT,
            REPLIED_AT,
            REPLY_COUNT,
            VIEW_COUNT,
            IS_STICKY,
            SEARCH_MATCH
        )
    }
}

@ThreadSortContract
typealias ThreadSort = String