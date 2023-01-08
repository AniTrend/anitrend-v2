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

package co.anitrend.data.relation.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MoeModel(
    @SerialName("anidb") val aniDb: Long? = null,
    @SerialName("anilist") val anilist: Long? = null,
    @SerialName("anime-planet") val animePlanet: String? = null,
    @SerialName("anisearch") val aniSearch: Long? = null,
    @SerialName("imdb") val imdb: String? = null,
    @SerialName("kitsu") val kitsu: Long? = null,
    @SerialName("livechart") val liveChart: Long? = null,
    @SerialName("notify-moe") val notifyMoe: String? = null,
    @SerialName("themoviedb") val theMovieDb: Long? = null,
    @SerialName("thetvdb") val theTvDb: Long? = null,
    @SerialName("myanimelist") val mal: Long? = null,
)