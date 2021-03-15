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

package co.anitrend.domain.media.model

import co.anitrend.domain.common.DateInt
import co.anitrend.domain.common.DateLike
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.enums.ScoreFormat

sealed class MediaParam {

    /** [Media query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
     *
     * @param id Filter by the media id
     * @param type Type of media
     * @param scoreFormat Score format to use
     */
    data class Detail(
        val id: Long,
        val type: MediaType? = null,
        val scoreFormat: ScoreFormat = ScoreFormat.POINT_100
    ) : MediaParam()

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
     * @param countryOfOrigin Filter by the media's country of origin
     * @param licensedBy Filter media by sites with a online streaming or reading license
     * @param licensedBy_in Filter media by sites with a online streaming or reading license
     * @param minimumTagRank Only apply the tags filter argument to tags above this rank. Default: 18
     * @param source Filter by the source type of the media
     * @param source_in Filter by the source type of the media
     */
    data class Find(
        var averageScore: Int? = null,
        var averageScore_greater: Int? = null,
        var averageScore_lesser: Int? = null,
        var averageScore_not: Int? = null,
        var chapters: Int? = null,
        var chapters_greater: Int? = null,
        var chapters_lesser: Int? = null,
        var countryOfOrigin: CharSequence? = null,
        var duration: Int? = null,
        var duration_greater: Int? = null,
        var duration_lesser: Int? = null,
        var endDate: DateInt? = null,
        var endDate_greater: DateInt? = null,
        var endDate_lesser: DateInt? = null,
        var endDate_like: DateLike? = null,
        var episodes: Int? = null,
        var episodes_greater: Int? = null,
        var episodes_lesser: Int? = null,
        var format: MediaFormat? = null,
        var format_in: List<MediaFormat>? = null,
        var format_not: MediaFormat? = null,
        var format_not_in: List<MediaFormat>? = null,
        var genre: String? = null,
        var genre_in: List<String>? = null,
        var genre_not_in: List<String>? = null,
        var id: Long? = null,
        var idMal: Long? = null,
        var idMal_in: List<Long>? = null,
        var idMal_not: Long? = null,
        var idMal_not_in: List<Long>? = null,
        var id_in: List<Long>? = null,
        var id_not: Long? = null,
        var id_not_in: List<Long>? = null,
        var isAdult: Boolean? = null,
        var licensedBy: String? = null,
        var licensedBy_in: List<String>? = null,
        var minimumTagRank: Int? = null,
        var onList: Boolean? = null,
        var popularity: Int? = null,
        var popularity_greater: Int? = null,
        var popularity_lesser: Int? = null,
        var popularity_not: Int? = null,
        var search: String? = null,
        var season: MediaSeason? = null,
        var seasonYear: Int? = null,
        var sort: List<MediaSort>? = null,
        var source: MediaSource? = null,
        var source_in: List<MediaSource>? = null,
        var startDate: DateInt? = null,
        var startDate_greater: DateInt? = null,
        var startDate_lesser: DateInt? = null,
        var startDate_like: DateLike? = null,
        var status: MediaStatus? = null,
        var status_in: List<MediaStatus>? = null,
        var status_not: MediaStatus? = null,
        var status_not_in: List<MediaStatus>? = null,
        var tag: String? = null,
        var tagCategory: String? = null,
        var tagCategory_in: List<String>? = null,
        var tagCategory_not_in: List<String>? = null,
        var tag_in: List<String>? = null,
        var tag_not_in: List<String>? = null,
        var type: MediaType? = null,
        var volumes: Int? = null,
        var volumes_greater: Int? = null,
        var volumes_lesser: Int? = null
    ) : MediaParam() {

        infix fun builder(param: Find.() -> Unit): Find {
            this.param()
            return this
        }
    }
}
