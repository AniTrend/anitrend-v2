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

package co.anitrend.data.usecase.medialist.attributes

import androidx.annotation.StringDef

/**
 * Media list watching/reading status.
 */
@StringDef(
    MediaListStatusContract.CURRENT,
    MediaListStatusContract.PLANNING,
    MediaListStatusContract.COMPLETED,
    MediaListStatusContract.DROPPED,
    MediaListStatusContract.PAUSED,
    MediaListStatusContract.REPEATING
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaListStatusContract {
    companion object {
        /** Currently watching/reading */
        const val CURRENT = "CURRENT"

        /** Planning to watch/read */
        const val PLANNING = "PLANNING"

        /** Finished watching/reading */
        const val COMPLETED = "COMPLETED"

        /** Stopped watching/reading before completing */
        const val DROPPED = "DROPPED"

        /** Paused watching/reading */
        const val PAUSED = "PAUSED"

        /** Paused watching/reading */
        const val REPEATING = "REPEATING"

        val ALL = listOf(
            CURRENT,
            PLANNING,
            COMPLETED,
            DROPPED,
            PAUSED,
            REPEATING
        )
    }
}

@MediaListStatusContract
typealias MediaListStatus = String