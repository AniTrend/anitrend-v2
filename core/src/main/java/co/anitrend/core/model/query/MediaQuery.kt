package co.anitrend.core.model.query

import co.anitrend.core.model.contract.IGraphQuery
import co.anitrend.core.repository.media.*


/** [Media query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * @param id: Filter by the media id
 * @param idMal: Filter by the media's MyAnimeList id
 * @param startDate: Filter by the start date of the media
 * @param endDate: Filter by the end date of the media
 * @param season: Filter by the season the media was released in
 * @param seasonYear: The year of the season (Winter 2017 would also
 * @param type: Filter by the media's type
 * @param format: Filter by the media's format
 * @param status: Filter by the media's current release status
 * @param episodes: Filter by amount of episodes the media has
 * @param duration: Filter by the media's episode length
 * @param chapters: Filter by the media's chapter count
 * @param volumes: Filter by the media's volume count
 * @param isAdult: Filter by if the media's intended for 18+ adult
 * @param genre: Filter by the media's genres
 * @param tag: Filter by the media's tags
 * @param tagCategory: Filter by the media's tags with in a tag category
 * @param onList: Filter by the media on the authenticated user's lists
 * @param averageScore: Filter by the media's average score
 * @param popularity: Filter by the number of users with this media on
 * @param search: Filter by search query
 * @param id_not: Filter by the media id
 * @param id_in: Filter by the media id
 * @param id_not_in: Filter by the media id
 * @param idMal_not: Filter by the media's MyAnimeList id
 * @param idMal_in: Filter by the media's MyAnimeList id
 * @param idMal_not_in: Filter by the media's MyAnimeList id
 * @param startDate_greater: Filter by the start date of the media
 * @param startDate_lesser: Filter by the start date of the media
 * @param startDate_like: Filter by the start date of the media
 * @param endDate_greater: Filter by the end date of the media
 * @param endDate_lesser: Filter by the end date of the media
 * @param endDate_like: Filter by the end date of the media
 * @param format_in: Filter by the media's format
 * @param format_not: Filter by the media's format
 * @param format_not_in: Filter by the media's format
 * @param status_in: Filter by the media's current release status
 * @param status_not: Filter by the media's current release status
 * @param status_not_in: Filter by the media's current release status
 * @param episodes_greater: Filter by amount of episodes the media has
 * @param episodes_lesser: Filter by amount of episodes the media has
 * @param duration_greater: Filter by the media's episode length
 * @param duration_lesser: Filter by the media's episode length
 * @param chapters_greater: Filter by the media's chapter count
 * @param chapters_lesser: Filter by the media's chapter count
 * @param volumes_greater: Filter by the media's volume count
 * @param volumes_lesser: Filter by the media's volume count
 * @param genre_in: Filter by the media's genres
 * @param genre_not_in: Filter by the media's genres
 * @param tag_in: Filter by the media's tags
 * @param tag_not_in: Filter by the media's tags
 * @param tagCategory_in: Filter by the media's tags with in a tag
 * @param tagCategory_not_in: Filter by the media's tags with in a tag
 * @param onList on users list
 * @param averageScore_not: Filter by the media's average score
 * @param averageScore_greater: Filter by the media's average score
 * @param averageScore_lesser: Filter by the media's average score
 * @param popularity_not: Filter by the number of users with this media
 * @param popularity_greater: Filter by the number of users with this
 * @param popularity_lesser: Filter by the number of users with this
 * @param sort: The order the results will be returned in
 */
data class MediaQuery(
    val id: Int? = null,
    val idMal: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    @MediaSeason
    val season: String? = null,
    val seasonYear: Int? = null,
    @MediaType
    val type: String? = null,
    @MediaFormat
    val format: String? = null,
    @MediaStatus
    val status: String? = null,
    val episodes: Int? = null,
    val duration: Int? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val isAdult: Boolean? = null,
    val genre: String? = null,
    val tag: String? = null,
    val tagCategory: String? = null,
    val onList: Boolean? = null,
    val averageScore: Int? = null,
    val popularity: Int? = null,
    val search: String? = null,
    val id_not: Int? = null,
    val id_in: List<Int>? = null,
    val id_not_in: List<Int>? = null,
    val idMal_not: Int? = null,
    val idMal_in: List<Int>? = null,
    val idMal_not_in: List<Int>? = null,
    val startDate_greater: String? = null,
    val startDate_lesser: String? = null,
    val startDate_like: String? = null,
    val endDate_greater: String? = null,
    val endDate_lesser: String? = null,
    val endDate_like: String? = null,
    @MediaFormat
    val format_in: List<String>? = null,
    @MediaFormat
    val format_not: String? = null,
    @MediaFormat
    val format_not_in: List<String>? = null,
    @MediaStatus
    val status_in: List<String>? = null,
    @MediaStatus
    val status_not: String? = null,
    @MediaStatus
    val status_not_in: List<String>? = null,
    val episodes_greater: Int? = null,
    val episodes_lesser: Int? = null,
    val duration_greater: Int? = null,
    val duration_lesser: Int? = null,
    val chapters_greater: Int? = null,
    val chapters_lesser: Int? = null,
    val volumes_greater: Int? = null,
    val volumes_lesser: Int? = null,
    val genre_in: List<String>? = null,
    val genre_not_in: List<String>? = null,
    val tag_in: List<String>? = null,
    val tag_not_in: List<String>? = null,
    val tagCategory_in: List<String>? = null,
    val tagCategory_not_in: List<String>? = null,
    val averageScore_not: Int? = null,
    val averageScore_greater: Int? = null,
    val averageScore_lesser: Int? = null,
    val popularity_not: Int? = null,
    val popularity_greater: Int? = null,
    val popularity_lesser: Int? = null,
    @MediaSort
    val sort: List<String>? = null
): IGraphQuery
