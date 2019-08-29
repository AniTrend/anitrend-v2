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

package co.anitrend.data.usecase.user.attributes

import androidx.annotation.StringDef

/**
 * User statistics sort values
 */
@StringDef(
    UserStatisticsSortContract.COUNT,
    UserStatisticsSortContract.ID,
    UserStatisticsSortContract.MEAN_SCORE,
    UserStatisticsSortContract.PROGRESS
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class UserStatisticsSortContract {
    companion object {
        const val COUNT = "COUNT"
        const val ID = "ID"
        const val MEAN_SCORE = "MEAN_SCORE"
        const val PROGRESS = "PROGRESS"

        val ALL = listOf(
            COUNT,
            ID,
            MEAN_SCORE,
            PROGRESS
        )
    }
}

@UserSortContract
typealias UserStatisticsSort = String