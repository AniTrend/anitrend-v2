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
package co.anitrend.data.edge.episode.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Episode information for a specific season of a media item.
 *
 * Mirrors the original `SeriesEpisodeType` (and nested `SeriesEpisodeCrewType`) previously
 * embedded in `EdgeMediaModel`. Episodes may include crew and guest star listings as well as
 * cross-season absolute numbering when available.
 *
 * @param absoluteEpisodeNumber Absolute episode number across all seasons (if supplied).
 * @param airDate Air date/time of the episode (epoch seconds).
 * @param airedAfterEpisodeNumber Episode number this aired after (irregular ordering support).
 * @param airedAfterSeasonNumber Season number this aired after (irregular ordering support).
 * @param airedBeforeEpisodeNumber Episode number this aired before (irregular ordering support).
 * @param airedBeforeSeasonNumber Season number this aired before (irregular ordering support).
 * @param crew Crew members associated with the episode (direction, writing, etc.).
 * @param episodeNumber The season-relative episode number.
 * @param guests Guest cast entries for the episode.
 * @param id Unique upstream episode identifier (TMDB/TVDB id).
 * @param image Primary episode still image.
 * @param name Optional short name/title for the episode.
 * @param overview Summary / synopsis of the episode.
 * @param poster Poster style image where available.
 * @param runtime Runtime length in minutes.
 * @param seasonNumber Season number this episode belongs to.
 * @param title Optional extended or alt title.
 * @param tvdbId TVDB episode id if available.
 * @param tvdbShowId TVDB show id reference if available.
 */
@Serializable
data class EdgeEpisodeModel(
    @SerialName("absoluteEpisodeNumber") val absoluteEpisodeNumber: Int? = null,
    @SerialName("airDate") val airDate: Long,
    @SerialName("airedAfterEpisodeNumber") val airedAfterEpisodeNumber: Int? = null,
    @SerialName("airedAfterSeasonNumber") val airedAfterSeasonNumber: Int? = null,
    @SerialName("airedBeforeEpisodeNumber") val airedBeforeEpisodeNumber: Int? = null,
    @SerialName("airedBeforeSeasonNumber") val airedBeforeSeasonNumber: Int? = null,
    @SerialName("crew") val crew: List<EdgeEpisodeCrewModel> = emptyList(),
    @SerialName("episodeNumber") val episodeNumber: Int,
    @SerialName("guests") val guests: List<EdgeEpisodeCrewModel> = emptyList(),
    @SerialName("id") val id: Long,
    @SerialName("image") val image: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster") val poster: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("seasonNumber") val seasonNumber: Int,
    @SerialName("title") val title: String? = null,
    @SerialName("tvdbId") val tvdbId: Long? = null,
    @SerialName("tvdbShowId") val tvdbShowId: Long? = null,
) {
    /**
     * Crew / Guest credit entry for an episode.
     *
     * Mirrors original `SeriesEpisodeCrewType`.
     *
     * @param adult Whether the person is marked adult in upstream data.
     * @param character Character name (if voice / cast credit).
     * @param creditId Upstream credit identifier.
     * @param department Department grouping (Writing, Directing, etc.).
     * @param id Person id in upstream provider.
     * @param image Optional profile image URL.
     * @param job Specific job/role for this credit.
     * @param knownFor Primary department the person is generally known for.
     * @param name Display name.
     * @param order Credit order (cast ordering / importance).
     * @param originalName Original/localised name.
     * @param popularity Popularity metric from upstream.
     */
    @Serializable
    data class EdgeEpisodeCrewModel(
        @SerialName("adult") val adult: Boolean? = null,
        @SerialName("character") val character: String? = null,
        @SerialName("creditId") val creditId: String,
        @SerialName("department") val department: String? = null,
        @SerialName("id") val id: Long,
        @SerialName("image") val image: String? = null,
        @SerialName("job") val job: String? = null,
        @SerialName("knownFor") val knownFor: String,
        @SerialName("name") val name: String,
        @SerialName("order") val order: Int? = null,
        @SerialName("originalName") val originalName: String,
        @SerialName("popularity") val popularity: Float,
    )
}
