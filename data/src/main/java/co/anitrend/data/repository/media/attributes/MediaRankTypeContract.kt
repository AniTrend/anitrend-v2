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
 * Media Rank Type values
 */
@StringDef(
    MediaRankTypeContract.RATED,
    MediaRankTypeContract.POPULAR
)
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPEALIAS)
annotation class MediaRankTypeContract {
    companion object {
        /** Ranking is based on the media's ratings/score **/
        const val RATED = "RATED"

        /** Ranking is based on the media's popularity **/
        const val POPULAR = "POPULAR"

        val ALL = listOf(
            RATED,
            POPULAR
        )
    }
}

@MediaRankTypeContract
typealias MediaRankType = String