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
 * Media status values
 */
@StringDef(
    MediaStatusContract.FINISHED,
    MediaStatusContract.RELEASING,
    MediaStatusContract.NOT_YET_RELEASED,
    MediaStatusContract.CANCELLED
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaStatusContract {
    companion object {
        /** Has completed and is no longer being released */
        const val FINISHED = "FINISHED"

        /** Currently releasing **/
        const val RELEASING = "RELEASING"

        /** To be released at a later date */
        const val NOT_YET_RELEASED = "NOT_YET_RELEASED"

        /** Ended before the work could be finished */
        const val CANCELLED = "CANCELLED"

        val ALL = listOf(
            FINISHED,
            RELEASING,
            NOT_YET_RELEASED,
            CANCELLED
        )
    }
}

@MediaStatusContract
typealias MediaStatus = String