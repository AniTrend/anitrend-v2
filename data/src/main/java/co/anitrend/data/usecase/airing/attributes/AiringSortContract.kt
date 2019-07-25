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

package co.anitrend.data.usecase.airing.attributes

import androidx.annotation.StringDef

/**
 * Airing schedule sort values
 */
@StringDef(
    AiringSortContract.ID,
    AiringSortContract.MEDIA_ID,
    AiringSortContract.TIME,
    AiringSortContract.EPISODE
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class AiringSortContract {
    companion object {
        const val ID = "ID"
        const val MEDIA_ID = "MEDIA_ID"
        const val TIME = "TIME"
        const val EPISODE = "EPISODE"

        val ALL = listOf(
            ID,
            MEDIA_ID,
            TIME,
            EPISODE
        )
    }
}

@AiringSortContract
typealias AiringSort = String