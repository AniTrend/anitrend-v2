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

package co.anitrend.data.model.query.media

import co.anitrend.data.model.contract.FuzzyDateInt
import co.anitrend.data.model.contract.FuzzyDateLike
import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.media.attributes.MediaFormat
import co.anitrend.data.repository.media.attributes.MediaSeason
import co.anitrend.data.repository.media.attributes.MediaSort
import co.anitrend.data.repository.media.attributes.MediaStatus


/** [Media query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * @param id Filter by the media id
 * @param idMal Filter by the media's MyAnimeList id
 * @param startDate Filter by the start date of the media
 * @param endDate Filter by the end date of the media
 * @param season Filter by the season the media was released in
 * @param seasonYear The year of the season (Winter 2017 would also
 * @param type Filter by the media's type
 * @param format Filter by the media's format
 * @param status Filter by the media's current release status
 * @param episodes Filter by amount of episodes the media has
 * @param duration Filter by the media's episode length
 * @param chapters Filter by the media's chapter count
 * @param volumes Filter by the media's volume count
 * @param isAdult Filter by if the media's intended for 18+ adult
 * @param genre Filter by the media's genres
 * @param tag Filter by the media's tags
 * @param tagCategory Filter by the media's tags with in a tag category
 * @param onList Filter by the media on the authenticated user's lists
 * @param averageScore Filter by the media's average score
 * @param popularity Filter by the number of users with this media on
 * @param search Filter by search query
 * @param id_not Filter by the media id
 * @param id_in Filter by the media id
 * @param id_not_in Filter by the media id
 * @param idMal_not Filter by the media's MyAnimeList id
 * @param idMal_in Filter by the media's MyAnimeList id
 * @param idMal_not_in Filter by the media's MyAnimeList id
 * @param startDate_greater Filter by the start date of the media
 * @param startDate_lesser Filter by the start date of the media
 * @param startDate_like Filter by the start date of the media
 * @param endDate_greater Filter by the end date of the media
 * @param endDate_lesser Filter by the end date of the media
 * @param endDate_like Filter by the end date of the media
 * @param format_in Filter by the media's format
 * @param format_not Filter by the media's format
 * @param format_not_in Filter by the media's format
 * @param status_in Filter by the media's current release status
 * @param status_not Filter by the media's current release status
 * @param status_not_in Filter by the media's current release status
 * @param episodes_greater Filter by amount of episodes the media has
 * @param episodes_lesser Filter by amount of episodes the media has
 * @param duration_greater Filter by the media's episode length
 * @param duration_lesser Filter by the media's episode length
 * @param chapters_greater Filter by the media's chapter count
 * @param chapters_lesser Filter by the media's chapter count
 * @param volumes_greater Filter by the media's volume count
 * @param volumes_lesser Filter by the media's volume count
 * @param genre_in Filter by the media's genres
 * @param genre_not_in Filter by the media's genres
 * @param tag_in Filter by the media's tags
 * @param tag_not_in Filter by the media's tags
 * @param tagCategory_in Filter by the media's tags with in a tag
 * @param tagCategory_not_in Filter by the media's tags with in a tag
 * @param onList n users list
 * @param averageScore_not Filter by the media's average score
 * @param averageScore_greater Filter by the media's average score
 * @param averageScore_lesser Filter by the media's average score
 * @param popularity_not Filter by the number of users with this media
 * @param popularity_greater Filter by the number of users with this
 * @param popularity_lesser Filter by the number of users with this
 * @param sort The order the results will be returned in
 */
data class MediaQuery(
    val id: Int? = null,
    val idMal: Int? = null,
    val startDate: String? = null,
    val endDate: FuzzyDateInt? = null,
    val season: MediaSeason? = null,
    val seasonYear: Int? = null,
    val type: String? = null,
    val format: MediaFormat? = null,
    val status: MediaStatus? = null,
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
    val startDate_greater: FuzzyDateInt? = null,
    val startDate_lesser: FuzzyDateInt? = null,
    val startDate_like: FuzzyDateLike? = null,
    val endDate_greater: FuzzyDateInt? = null,
    val endDate_lesser: FuzzyDateInt? = null,
    val endDate_like: FuzzyDateLike? = null,
    val format_in: List<MediaFormat>? = null,
    val format_not: MediaFormat? = null,
    val format_not_in: List<MediaFormat>? = null,
    val status_in: List<MediaStatus>? = null,
    val status_not: MediaStatus? = null,
    val status_not_in: List<MediaStatus>? = null,
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
    val sort: List<MediaSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "idMal" to idMal,
        "startDate" to startDate,
        "endDate" to endDate,
        "season" to season,
        "seasonYear" to seasonYear,
        "type" to type,
        "format" to format,
        "status" to status,
        "episodes" to episodes,
        "duration" to duration,
        "chapters" to chapters,
        "volumes" to volumes,
        "isAdult" to isAdult,
        "genre" to genre,
        "tag" to tag,
        "tagCategory" to tagCategory,
        "onList" to onList,
        "averageScore" to averageScore,
        "popularity" to popularity,
        "search" to search,
        "id_not" to id_not,
        "id_in" to id_in,
        "id_not_in" to id_not_in,
        "idMal_not" to idMal_not,
        "idMal_in" to idMal_in,
        "idMal_not_in" to idMal_not_in,
        "startDate_greater" to startDate_greater,
        "startDate_lesser" to startDate_lesser,
        "startDate_like" to startDate_like,
        "endDate_greater" to endDate_greater,
        "endDate_lesser" to endDate_lesser,
        "endDate_like" to endDate_like,
        "format_in" to format_in,
        "format_not" to format_not,
        "format_not_in" to format_not_in,
        "status_in" to status_in,
        "status_not" to status_not,
        "status_not_in" to status_not_in,
        "episodes_greater" to episodes_greater,
        "episodes_lesser" to episodes_lesser,
        "duration_greater" to duration_greater,
        "duration_lesser" to duration_lesser,
        "chapters_greater" to chapters_greater,
        "chapters_lesser" to chapters_lesser,
        "volumes_greater" to volumes_greater,
        "volumes_lesser" to volumes_lesser,
        "genre_in" to genre_in,
        "genre_not_in" to genre_not_in,
        "tag_in" to tag_in,
        "tag_not_in" to tag_not_in,
        "tagCategory_in" to tagCategory_in,
        "tagCategory_not_in" to tagCategory_not_in,
        "onList" to onList ,
        "averageScore_not" to averageScore_not,
        "averageScore_greater" to averageScore_greater,
        "averageScore_lesser" to averageScore_lesser,
        "popularity_not" to popularity_not,
        "popularity_greater" to popularity_greater,
        "popularity_lesser" to popularity_lesser,
        "sort" to sort
    )
}
