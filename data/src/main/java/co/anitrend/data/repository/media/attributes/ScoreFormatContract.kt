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
 * Media list scoring type
 */
@StringDef(
    ScoreFormatContract.POINT_100,
    ScoreFormatContract.POINT_10_DECIMAL,
    ScoreFormatContract.POINT_10,
    ScoreFormatContract.POINT_5,
    ScoreFormatContract.POINT_3
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class ScoreFormatContract {
    companion object {
        /** An integer from 0-100 */
        const val POINT_100 = "POINT_100"

        /** A float from 0-10 with 1 decimal place */
        const val POINT_10_DECIMAL = "POINT_10_DECIMAL"

        /** An integer from 0-10 */
        const val POINT_10 = "POINT_10"

        /** An integer from 0-5. Should be represented in Stars */
        const val POINT_5 = "POINT_5"

        /** An integer from 0-3. Should be represented in Smileys.
         * 0 => No Score
         * 1 => :(
         * 2 => :|
         * 3 => :)
         */
        const val POINT_3 = "POINT_3"

        val ALL = listOf(
            POINT_100,
            POINT_10_DECIMAL,
            POINT_10,
            POINT_5,
            POINT_3
        )
    }
}

@ScoreFormatContract
typealias ScoreFormat = String