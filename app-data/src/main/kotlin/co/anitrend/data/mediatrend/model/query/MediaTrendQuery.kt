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

package co.anitrend.data.mediatrend.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.mediatrend.model.MediaTrendParam

internal data class MediaTrendQuery(
    val param: MediaTrendParam.Find
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "mediaId" to param.mediaId,
        "date" to param.date,
        "trending" to param.trending,
        "averageScore" to param.averageScore,
        "popularity" to param.popularity,
        "episode" to param.episode,
        "releasing" to param.releasing,
        "mediaId_not" to param.mediaId_not,
        "mediaId_in" to param.mediaId_in,
        "mediaId_not_in" to param.mediaId_not_in,
        "date_greater" to param.date_greater,
        "date_lesser" to param.date_lesser,
        "trending_greater" to param.trending_greater,
        "trending_lesser" to param.trending_lesser,
        "trending_not" to param.trending_not,
        "averageScore_greater" to param.averageScore_greater,
        "averageScore_lesser" to param.averageScore_lesser,
        "averageScore_not" to param.averageScore_not,
        "popularity_greater" to param.popularity_greater,
        "popularity_lesser" to param.popularity_lesser,
        "popularity_not" to param.popularity_not,
        "episode_greater" to param.episode_greater,
        "episode_lesser" to param.episode_lesser,
        "episode_not" to param.episode_not
    )
}