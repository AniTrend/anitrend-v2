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
package co.anitrend.data.edge.media.model.remote

import co.anitrend.data.edge.image.model.EdgeImageModel
import co.anitrend.data.edge.network.model.EdgeNetworkModel
import co.anitrend.data.edge.theme.model.EdgeThemeModel
import co.anitrend.data.edge.theme.model.serializer.EdgeThemeListSerializer
import co.anitrend.data.edge.trailer.model.EdgeTrailerModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeMediaModel(
    @SerialName("series") val series: Series?,
) {
    /**
     * Matches MediaType in anitrend.schema.graphql
     *
     * @param id The unique identifier for the media entity (often corresponds to 'notify' ID or a combined key).
     * @param title Titles of the media in various languages (english, romaji, native, etc.).
     * @param cover Cover images for the media (extraLarge, large, medium, color).
     * @param banner URL to a banner image for the media.
     * @param description Synopsis or description of the media.
     * @param fanart URL to a fanart image for the media.
     * @param format Format of the media (e.g., TV, MOVIE, OVA, ONA, MANGA).
     * @param homepage URL to the official homepage of the media.
     * @param airedEpisodes Total number of aired episodes.
     * @param broadcast Broadcast schedule information (day/time string) for anime entries.
     * @param source Source material of the media (e.g., ORIGINAL, MANGA, LIGHT_NOVEL, GAME).
     * @param status Current status of the media (e.g., RELEASING, FINISHED, NOT_YET_RELEASED, CANCELLED).
     * @param ageRating Age rating of the media (e.g., G, PG, R17).
     * @param isAdult Indicates if the media is considered adult content.
     * @param kind Distinguishes the upstream media bucket (e.g., ANIME, MANGA).
     * @param chapters Total number of chapters (manga specific).
     * @param volumes Total number of volumes (manga specific).
     * @param moreInfo External link to additional upstream information.
     * @param publishedFrom Publication start timestamp (epoch seconds) for print media.
     * @param publishedTo Publication end timestamp (epoch seconds) for print media.
     * @param mediaId A collection of alternative identifiers for the media from various sources.
     * @param images Collection of images for the media (backdrops, logos, posters).
     * @param schedule Airing schedule information for the media.
     * @param networks List of networks associated with the media.
     * @param themeSongs List of theme songs (openings and endings).
     * @param trailers List of trailers for the media.
     * @param updatedAt Timestamp of when the media information was last updated (epoch seconds).
     */
    @Serializable
    data class Series(
        @SerialName("title") val title: SeriesTitleType,
        @SerialName("cover") val cover: SeriesCoverImageType,
        @SerialName("banner") val banner: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("fanart") val fanart: String? = null,
        @SerialName("format") val format: String? = null,
        @SerialName("homepage") val homepage: String? = null,
        @SerialName("airedEpisodes") val airedEpisodes: Int? = null,
        @SerialName("broadcast") val broadcast: String? = null,
        @SerialName("source") val source: String? = null,
        @SerialName("status") val status: String? = null,
        @SerialName("ageRating") val ageRating: String? = null,
        @SerialName("isAdult") val isAdult: Boolean? = null,
        @SerialName("kind") val kind: String,
        @SerialName("chapters") val chapters: Int? = null,
        @SerialName("volumes") val volumes: Int? = null,
        @SerialName("moreInfo") val moreInfo: String? = null,
        @SerialName("publishedFrom") val publishedFrom: Long? = null,
        @SerialName("publishedTo") val publishedTo: Long? = null,
        @SerialName("mediaId") val mediaId: SeriesIdType,
        @SerialName("images") val images: List<EdgeImageModel> = emptyList(),
        @SerialName("schedule") val schedule: SeriesScheduleType? = null,
        @SerialName("networks") val networks: List<EdgeNetworkModel> = emptyList(),
        @Serializable(with = EdgeThemeListSerializer::class)
        @SerialName("themeSongs") val themeSongs: List<EdgeThemeModel> = emptyList(),
        @SerialName("trailers") val trailers: List<EdgeTrailerModel> = emptyList(),
        @SerialName("updatedAt") val updatedAt: Long,
    )

    /**
     * Titles of the media in various languages.
     *
     * @param canonical Canonical title
     * @param english English title
     * @param harigana Hiragana title (Note: source field name was 'harigana')
     * @param japanese Japanese title
     * @param romaji Romaji title
     * @param synonyms List of alternative titles or synonyms
     */
    @Serializable
    data class SeriesTitleType(
        @SerialName("canonical") val canonical: String? = null,
        @SerialName("english") val english: String? = null,
        @SerialName("harigana") val harigana: String? = null,
        @SerialName("japanese") val japanese: String? = null,
        @SerialName("romaji") val romaji: String? = null,
        @SerialName("synonyms") val synonyms: List<String>? = null,
    )

    /**
     * Cover image information for a media item.
     *
     * @param color Dominant color of the cover image (hex code)
     * @param extraLarge URL to an extra large cover image
     * @param large URL to a large cover image
     * @param medium URL to a medium cover image
     */
    @Serializable
    data class SeriesCoverImageType(
        @SerialName("color") val color: String? = null,
        @SerialName("extraLarge") val extraLarge: String? = null,
        @SerialName("large") val large: String? = null,
        @SerialName("medium") val medium: String? = null,
    )

    /**
     * Details about scheduled episodes and schedule meta.
     *
     * @param firstAirDate First air date of the series
     * @param lastAirDate Last air date of the series
     * @param lastAiredEpisode Details of the last aired episode
     * @param nextEpisodeToAir Details of the next episode to air
     */
    @Serializable
    data class SeriesScheduleType(
        @SerialName("firstAirDate") val firstAirDate: Long? = null,
        @SerialName("lastAirDate") val lastAirDate: Long? = null,
        @SerialName("lastAiredEpisode") val lastAiredEpisode: SeriesScheduleEpisodeType? = null,
        @SerialName("nextEpisodeToAir") val nextEpisodeToAir: SeriesScheduleEpisodeType? = null,
    )

    /**
     * Represents a scheduled episode for a series.
     *
     * @param airDate Air date and time of the episode
     * @param episodeNumber Episode number in the season
     * @param id Unique ID for the scheduled episode
     * @param image URL to an image for the episode
     * @param name Name or title of the episode
     * @param overview Brief overview or summary of the episode
     * @param productionCode Production code of the episode
     * @param runtime Runtime of the episode in minutes
     * @param seasonNumber Season number this episode belongs to
     * @param tmdbId TheMovieDB ID for the episode
     */
    @Serializable
    data class SeriesScheduleEpisodeType(
        @SerialName("airDate") val airDate: Long? = null,
        @SerialName("episodeNumber") val episodeNumber: Int? = null,
        @SerialName("id") val id: Long,
        @SerialName("image") val image: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("overview") val overview: String? = null,
        @SerialName("productionCode") val productionCode: String? = null,
        @SerialName("runtime") val runtime: Int? = null,
        @SerialName("seasonNumber") val seasonNumber: Int? = null,
        @SerialName("tmdbId") val tmdbId: Long? = null,
    )

    @Serializable
    data class SeriesIdType(
        @SerialName("anidb") val aniDb: Long? = null,
        @SerialName("anilist") val aniList: Long? = null,
        @SerialName("animePlanet") val animePlanet: String? = null,
        @SerialName("anisearch") val aniSearch: Long? = null,
        @SerialName("imdb") val imdb: String? = null,
        @SerialName("kitsu") val kitsu: Long? = null,
        @SerialName("livechart") val liveChart: Long? = null,
        @SerialName("myanimelist") val myAnimeList: Long? = null,
        @SerialName("notify") val notify: String? = null,
        @SerialName("shoboi") val shoboi: Long? = null,
        @SerialName("slug") val slug: String? = null,
        @SerialName("themoviedb") val tmdb: Long? = null,
        @SerialName("trakt") val trakt: Long? = null,
        @SerialName("tvdb") val tvDb: Long? = null,
        @SerialName("tvMazeId") val tvMaze: Long? = null,
        @SerialName("tvrage") val tvRage: String? = null,
    )
}
