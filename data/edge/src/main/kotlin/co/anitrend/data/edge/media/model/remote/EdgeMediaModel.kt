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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class EdgeMediaModel(
    @SerialName("mediaById") val media: Media?,
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
     * @param type Internal GraphQL typename mapping (not present in schema as a property)
     * @param source Source material of the media (e.g., ORIGINAL, MANGA, LIGHT_NOVEL, GAME).
     * @param status Current status of the media (e.g., RELEASING, FINISHED, NOT_YET_RELEASED, CANCELLED).
     * @param ageRating Age rating of the media (e.g., G, PG, R17).
     * @param isAdult Indicates if the media is considered adult content.
     * @param mediaId A collection of alternative identifiers for the media from various sources.
     * @param image Collection of images for the media (backdrops, logos, posters).
     * @param schedule Airing schedule information for the media.
     * @param seasons List of seasons for the media, if applicable.
     * @param networks List of networks associated with the media.
     * @param themeSongs List of theme songs (openings and endings).
     * @param trailers List of trailers for the media.
     * @param updatedAt Timestamp of when the media information was last updated (epoch seconds).
     */
    @Serializable
    data class Media(
        @SerialName("id") val id: String,
        @SerialName("title") val title: SeriesTitleType,
        @SerialName("cover") val cover: SeriesCoverImageType,
        @SerialName("banner") val banner: String? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("fanart") val fanart: String? = null,
        @SerialName("format") val format: String? = null,
        @SerialName("homepage") val homepage: String? = null,
        @SerialName("airedEpisodes") val airedEpisodes: Int? = null,
        @SerialName("source") val source: String? = null,
        @SerialName("status") val status: String? = null,
        @SerialName("ageRating") val ageRating: String? = null,
        @SerialName("isAdult") val isAdult: Boolean? = null,
        @SerialName("mediaId") val mediaId: SeriesIdType,
        @SerialName("image") val image: SeriesImageType,
        @SerialName("schedule") val schedule: SeriesScheduleType? = null,
        @SerialName("seasons") val seasons: List<SeriesSeasonType>? = null,
        @SerialName("networks") val networks: List<SeriesNetworkType>,
        @SerialName("themeSongs") val themeSongs: List<AnimeThemeType>,
        @SerialName("trailers") val trailers: List<SeriesTrailerType>,
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
     * Backdrop/poster/logo image information.
     *
     * @param url URL to the backdrop image
     * @param height Height of the backdrop image in pixels
     * @param locale Locale of the backdrop image (e.g., en, ja)
     * @param width Width of the backdrop image in pixels
     */
    @Serializable
    data class SeriesImageBackdropType(
        @SerialName("url") val url: String,
        @SerialName("height") val height: Int,
        @SerialName("locale") val locale: String? = null,
        @SerialName("width") val width: Int,
    )

    /**
     * Collection of images for the media (backdrops, logos, posters).
     *
     * @param backdrops List of backdrop images
     * @param logos List of logo images
     * @param posters List of poster images
     */
    @Serializable
    data class SeriesImageType(
        @SerialName("backdrops") val backdrops: List<SeriesImageBackdropType>,
        @SerialName("logos") val logos: List<SeriesImageBackdropType>,
        @SerialName("posters") val posters: List<SeriesImageBackdropType>,
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
        @SerialName("firstAirDate") val firstAirDate: Long,
        @SerialName("lastAirDate") val lastAirDate: Long,
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
        @SerialName("airDate") val airDate: Long,
        @SerialName("episodeNumber") val episodeNumber: Int,
        @SerialName("id") val id: Int,
        @SerialName("image") val image: String? = null,
        @SerialName("name") val name: String,
        @SerialName("overview") val overview: String,
        @SerialName("productionCode") val productionCode: String,
        @SerialName("runtime") val runtime: Int,
        @SerialName("seasonNumber") val seasonNumber: Int,
        @SerialName("tmdbId") val tmdbId: Int,
    )

    /**
     * Details about a season of a series.
     *
     * @param name Name of the season
     * @param tmdbId TheMovieDB ID for the season
     * @param overview Brief overview or summary of the season
     * @param image Images associated with the season (posters, backdrops)
     * @param episodeCount Number of episodes in this season
     * @param cover URL to a cover image for the season
     * @param airDate Air date of the first episode of the season
     * @param number Season number
     * @param episodes List of episodes in this season
     */
    @Serializable
    data class SeriesSeasonType(
        @SerialName("name") val name: String,
        @SerialName("tmdbId") val tmdbId: Int,
        @SerialName("overview") val overview: String,
        @SerialName("image") val image: SeriesImageType,
        @SerialName("episodeCount") val episodeCount: Int,
        @SerialName("cover") val cover: String? = null,
        @SerialName("airDate") val airDate: Long,
        @SerialName("number") val number: Int,
        @SerialName("episodes") val episodes: List<SeriesEpisodeType>,
    )

    /**
     * Episode information for a season.
     *
     * @param absoluteEpisodeNumber Absolute episode number across all seasons
     * @param airDate Air date and time of the episode
     * @param airedAfterEpisodeNumber If this episode aired after a specific episode number
     * @param airedAfterSeasonNumber If this episode aired after a specific season number
     * @param airedBeforeEpisodeNumber If this episode aired before a specific episode number
     * @param airedBeforeSeasonNumber If this episode aired before a specific season number
     * @param crew List of crew members for this episode
     * @param episodeNumber Episode number within the season
     * @param guests List of guest stars for this episode
     * @param id Unique ID for the episode
     * @param image URL to an image for the episode
     * @param name Name of the episode
     * @param overview Brief overview or summary of the episode
     * @param poster URL to a poster image for the episode
     * @param runtime Runtime of the episode in minutes
     * @param seasonNumber Season number this episode belongs to
     * @param title Title of the episode
     * @param tvdbId TheTVDB Episode ID
     * @param tvdbShowId TheTVDB Show ID this episode belongs to
     */
    @Serializable
    data class SeriesEpisodeType(
        @SerialName("absoluteEpisodeNumber") val absoluteEpisodeNumber: Int? = null,
        @SerialName("airDate") val airDate: Long,
        @SerialName("airedAfterEpisodeNumber") val airedAfterEpisodeNumber: Int? = null,
        @SerialName("airedAfterSeasonNumber") val airedAfterSeasonNumber: Int? = null,
        @SerialName("airedBeforeEpisodeNumber") val airedBeforeEpisodeNumber: Int? = null,
        @SerialName("airedBeforeSeasonNumber") val airedBeforeSeasonNumber: Int? = null,
        @SerialName("crew") val crew: List<SeriesEpisodeCrewType>,
        @SerialName("episodeNumber") val episodeNumber: Int,
        @SerialName("guests") val guests: List<SeriesEpisodeCrewType>,
        @SerialName("id") val id: Int,
        @SerialName("image") val image: String? = null,
        @SerialName("name") val name: String? = null,
        @SerialName("overview") val overview: String? = null,
        @SerialName("poster") val poster: String? = null,
        @SerialName("runtime") val runtime: Int? = null,
        @SerialName("seasonNumber") val seasonNumber: Int,
        @SerialName("title") val title: String? = null,
        @SerialName("tvdbId") val tvdbId: Int? = null,
        @SerialName("tvdbShowId") val tvdbShowId: Int? = null,
    )

    /**
     * Episode crew member information.
     *
     * @param adult Indicates if the crew member is associated with adult content
     * @param character Character name if the crew member is a voice actor/actress for this episode
     * @param creditId Credit ID for the crew member
     * @param department Department of the crew member (e.g., Directing, Writing)
     * @param id Unique ID for the crew member
     * @param image URL to an image of the crew member
     * @param job Job title of the crew member for this episode (e.g., Director, Writer)
     * @param knownFor Department the crew member is known for
     * @param name Name of the crew member
     * @param order Order of appearance or importance
     * @param originalName Original name of the crew member
     * @param popularity Popularity score of the crew member
     */
    @Serializable
    data class SeriesEpisodeCrewType(
        @SerialName("adult") val adult: Boolean? = null,
        @SerialName("character") val character: String? = null,
        @SerialName("creditId") val creditId: String,
        @SerialName("department") val department: String? = null,
        @SerialName("id") val id: Int,
        @SerialName("image") val image: String? = null,
        @SerialName("job") val job: String? = null,
        @SerialName("knownFor") val knownFor: String,
        @SerialName("name") val name: String,
        @SerialName("order") val order: Int? = null,
        @SerialName("originalName") val originalName: String,
        @SerialName("popularity") val popularity: Float,
    )

    @Serializable
    data class SeriesNetworkType(
        @SerialName("isPrimary") val isPrimary: Boolean,
        @SerialName("category") val category: String,
        @SerialName("id") val id: Int,
        @SerialName("logoPath") val logoPath: String? = null,
        @SerialName("name") val name: String,
        @SerialName("originCountry") val originCountry: String,
    )

    @Serializable
    data class SeriesTrailerType(
        @SerialName("id") val id: String,
        @SerialName("site") val site: String,
        @SerialName("thumbnail") val thumbnail: String? = null,
    )

    /**
     * Theme song information for the media.
     *
     * @param audio URL to the audio of the theme song
     * @param id Unique ID for the anime theme song
     * @param meta Metadata about the theme song (type, number, version)
     * @param name Name or title of the theme song
     * @param video URL to the video of the theme song
     */
    @Serializable
    data class AnimeThemeType(
        @SerialName("audio") val audio: String? = null,
        @SerialName("id") val id: String,
        @SerialName("meta") val meta: AnimeThemeMetaType,
        @SerialName("name") val name: String,
        @SerialName("video") val video: String,
    )

    @Serializable
    data class AnimeThemeMetaType(
        @SerialName("number") val number: Int,
        @SerialName("type") val type: String,
        @SerialName("version") val version: Int,
    )

    @Serializable
    data class SeriesIdType(
        @SerialName("anidb") val anidb: Int? = null,
        @SerialName("anilist") val anilist: Int? = null,
        @SerialName("animePlanet") val animePlanet: String? = null,
        @SerialName("anisearch") val anisearch: Int? = null,
        @SerialName("imdb") val imdb: String? = null,
        @SerialName("kitsu") val kitsu: Int? = null,
        @SerialName("livechart") val livechart: Int? = null,
        @SerialName("myanimelist") val myanimelist: Int? = null,
        @SerialName("notify") val notify: String? = null,
        @SerialName("shoboi") val shoboi: Int? = null,
        @SerialName("slug") val slug: String? = null,
        @SerialName("themoviedb") val themoviedb: Int? = null,
        @SerialName("trakt") val trakt: Int? = null,
        @SerialName("tvdb") val tvdb: Int? = null,
        @SerialName("tvMazeId") val tvMazeId: Int? = null,
        @SerialName("tvrage") val tvrage: String? = null,
    )
}
