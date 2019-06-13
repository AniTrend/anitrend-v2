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

package co.anitrend.data.repository.medialist.attributes

import androidx.annotation.StringDef

/**
 * MediaList Sort values
 */
@StringDef(
    MediaListSortContract.MEDIA_ID,
    MediaListSortContract.SCORE,
    MediaListSortContract.STATUS,
    MediaListSortContract.PROGRESS,
    MediaListSortContract.PROGRESS_VOLUMES,
    MediaListSortContract.REPEAT,
    MediaListSortContract.PRIORITY,
    MediaListSortContract.STARTED_ON,
    MediaListSortContract.FINISHED_ON,
    MediaListSortContract.ADDED_TIME,
    MediaListSortContract.UPDATED_TIME
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaListSortContract {
    companion object {
        const val MEDIA_ID = "MEDIA_ID"
        const val SCORE = "SCORE"
        const val STATUS = "STATUS"
        const val PROGRESS = "PROGRESS"
        const val PROGRESS_VOLUMES = "PROGRESS_VOLUMES"
        const val REPEAT = "REPEAT"
        const val PRIORITY = "PRIORITY"
        const val STARTED_ON = "STARTED_ON"
        const val FINISHED_ON = "FINISHED_ON"
        const val ADDED_TIME = "ADDED_TIME"
        const val UPDATED_TIME = "UPDATED_TIME"

        val ALL = listOf(
            MEDIA_ID,
            SCORE,
            STATUS,
            PROGRESS,
            PROGRESS_VOLUMES,
            REPEAT,
            PRIORITY,
            STARTED_ON,
            FINISHED_ON,
            ADDED_TIME,
            UPDATED_TIME
        )
    }
}

@MediaListSortContract
typealias MediaListSort = String