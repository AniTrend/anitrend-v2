/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.domain.media.entity.attribute.origin

/**
 * Ids for media from different sources
 */
interface IMediaSourceId {
    val aniDb: Long?
    val anilist: Long?
    val animePlanet: String?
    val aniSearch: Long?
    val imdb: String?
    val kitsu: Long?
    val liveChart: Long?
    val notifyMoe: String?
    val theMovieDb: Long?
    val mal: Long?
}