/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.season.model

import co.anitrend.data.edge.episode.model.EdgeEpisodeModel
import co.anitrend.data.edge.image.model.EdgeImageModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Season information for a serialised media item.
 *
 * Mirrors the original `SeriesSeasonType` formerly nested in `EdgeMediaModel`.
 * A media can contain multiple seasons; each includes summary metadata and image resources.
 *
 * @param name Season name or marketing title (e.g. "Season 2", "Final Season").
 * @param tmdbId Upstream provider id (TMDB) for the season resource.
 * @param overview Brief overview/summary text for the season.
 * @param image Rich image bundle (backdrops/posters/logos) associated with the season.
 * @param episodeCount Total number of episodes in the season.
 * @param cover Optional single cover image URL (may duplicate one entry from image.poster set).
 * @param airDate First air date (epoch seconds) of the season.
 * @param number Numeric order of the season starting at 1.
 */
@Serializable
data class EdgeSeasonModel(
    @SerialName("name") val name: String,
    @SerialName("tmdbId") val tmdbId: Long,
    @SerialName("overview") val overview: String,
    @SerialName("image") val image: EdgeImageModel,
    @SerialName("episodeCount") val episodeCount: Int,
    @SerialName("cover") val cover: String? = null,
    @SerialName("airDate") val airDate: Long,
    @SerialName("number") val number: Int,
    @SerialName("episodes") val episodes: List<EdgeEpisodeModel>,
)
