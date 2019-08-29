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

package co.anitrend.data.usecase.media.attributes

import androidx.annotation.StringDef

/**
 * Media Season values
 */
@StringDef(
    MediaSeasonContract.WINTER,
    MediaSeasonContract.SPRING,
    MediaSeasonContract.SUMMER,
    MediaSeasonContract.FALL
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaSeasonContract {
    companion object {
        /** Months December to February */
        const val WINTER = "WINTER"

        /** Months March to May */
        const val SPRING = "SPRING"

        /** Months June to August */
        const val SUMMER = "SUMMER"

        /** Months September to November */
        const val FALL = "FALL"

        val ALL = listOf(
            WINTER,
            SPRING,
            SUMMER,
            FALL
        )
    }
}

@MediaSeasonContract
typealias MediaSeason = String