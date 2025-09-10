/*
 * Copyright (C) 2020 AniTrend
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

data class MediaSourceId(
    override val aniDb: Long?,
    override val aniList: Long,
    override val animePlanet: String?,
    override val aniSearch: Long?,
    override val imdb: String?,
    override val kitsu: Long?,
    override val liveChart: Long?,
    override val myAnimeList: Long?,
    override val notify: String?,
    override val shoboi: Long?,
    override val slug: String?,
    override val tmdb: Long?,
    override val trakt: Long?,
    override val tvDb: Long?,
    override val tvMaze: Long?,
    override val tvRage: String?,
) : IMediaSourceId {
    companion object {
        fun empty() =
            MediaSourceId(
                aniDb = null,
                aniList = 0L,
                animePlanet = null,
                aniSearch = null,
                imdb = null,
                kitsu = null,
                liveChart = null,
                myAnimeList = null,
                notify = null,
                shoboi = null,
                slug = null,
                tmdb = null,
                trakt = null,
                tvDb = null,
                tvMaze = null,
                tvRage = null,
            )
    }
}
