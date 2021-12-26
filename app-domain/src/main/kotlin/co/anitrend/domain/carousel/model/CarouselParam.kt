/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.carousel.model

import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat

sealed class CarouselParam {
    data class Find(
        val season: MediaSeason,
        val seasonYear: Int,
        val nextSeason: MediaSeason,
        val nextSeasonYear: Int,
        val isAdult: Boolean? = false,
        val currentTime: Long,
        val scoreFormat: ScoreFormat? = null,
        val type: MediaType? = null,
        val pageSize: Int
    ) : CarouselParam()
}