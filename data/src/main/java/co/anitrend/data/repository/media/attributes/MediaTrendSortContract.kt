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

package co.anitrend.data.repository.media.attributes

import androidx.annotation.StringDef

/**
 * Media trend sort values
 */
@StringDef(
    MediaTrendSortContract.ID,
    MediaTrendSortContract.MEDIA_ID,
    MediaTrendSortContract.DATE,
    MediaTrendSortContract.SCORE,
    MediaTrendSortContract.POPULARITY,
    MediaTrendSortContract.TRENDING,
    MediaTrendSortContract.EPISODE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaTrendSortContract {
    companion object {
        const val ID = "ID"
        const val MEDIA_ID = "MEDIA_ID"
        const val DATE = "DATE"
        const val SCORE = "SCORE"
        const val POPULARITY = "POPULARITY"
        const val TRENDING = "TRENDING"
        const val EPISODE = "EPISODE"

        val ALL = listOf(
            ID,
            MEDIA_ID,
            DATE,
            SCORE,
            POPULARITY,
            TRENDING,
            EPISODE
        )
    }
}

@MediaTrendSortContract
typealias MediaTrendSort = String