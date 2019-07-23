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

import co.anitrend.data.model.response.contract.FuzzyDateInt
import co.anitrend.data.model.response.contract.IGraphQuery
import co.anitrend.data.usecase.media.attributes.MediaTrendSort

/** [MediaTrend query][https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html]
 *
 * @param mediaId Filter by the media id
 * @param date Filter by date
 * @param trending Filter by trending amount
 * @param averageScore Filter by score
 * @param popularity Filter by popularity
 * @param episode Filter by episode number
 * @param releasing Filter to stats recorded while the media was releasing
 * @param mediaId_not Filter by the media id
 * @param mediaId_in Filter by the media id
 * @param mediaId_not_in Filter by the media id
 * @param date_greater Filter by date
 * @param date_lesser Filter by date
 * @param trending_greater Filter by trending amount
 * @param trending_lesser Filter by trending amount
 * @param trending_not Filter by trending amount
 * @param averageScore_greater Filter by score
 * @param averageScore_lesser Filter by score
 * @param averageScore_not Filter by score
 * @param popularity_greater Filter by popularity
 * @param popularity_lesser Filter by popularity
 * @param popularity_not Filter by popularity
 * @param episode_greater Filter by episode number
 * @param episode_lesser Filter by episode number
 * @param episode_not Filter by episode number
 * @param sort The order the results will be returned in
 */
data class MediaTrendQuery(
    val mediaId: Int? = null,
    val date: FuzzyDateInt? = null,
    val trending: Int? = null,
    val averageScore: Int? = null,
    val popularity: Int? = null,
    val episode: Int? = null,
    val releasing: Boolean? = null,
    val mediaId_not: Int? = null,
    val mediaId_in: List<Int>? = null,
    val mediaId_not_in: List<Int>? = null,
    val date_greater: FuzzyDateInt? = null,
    val date_lesser: FuzzyDateInt? = null,
    val trending_greater: Int? = null,
    val trending_lesser: Int? = null,
    val trending_not: Int? = null,
    val averageScore_greater: Int? = null,
    val averageScore_lesser: Int? = null,
    val averageScore_not: Int? = null,
    val popularity_greater: Int? = null,
    val popularity_lesser: Int? = null,
    val popularity_not: Int? = null,
    val episode_greater: Int? = null,
    val episode_lesser: Int? = null,
    val episode_not: Int? = null,
    val sort: List<MediaTrendSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "mediaId" to mediaId,
        "date" to date,
        "trending" to trending,
        "averageScore" to averageScore,
        "popularity" to popularity,
        "episode" to episode,
        "releasing" to releasing,
        "mediaId_not" to mediaId_not,
        "mediaId_in" to mediaId_in,
        "mediaId_not_in" to mediaId_not_in,
        "date_greater" to date_greater,
        "date_lesser" to date_lesser,
        "trending_greater" to trending_greater,
        "trending_lesser" to trending_lesser,
        "trending_not" to trending_not,
        "averageScore_greater" to averageScore_greater,
        "averageScore_lesser" to averageScore_lesser,
        "averageScore_not" to averageScore_not,
        "popularity_greater" to popularity_greater,
        "popularity_lesser" to popularity_lesser,
        "popularity_not" to popularity_not,
        "episode_greater" to episode_greater,
        "episode_lesser" to episode_lesser,
        "episode_not" to episode_not
    )
}