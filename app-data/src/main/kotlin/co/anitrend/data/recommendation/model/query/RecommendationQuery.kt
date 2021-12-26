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

package co.anitrend.data.recommendation.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.recommendation.model.RecommendationParam

internal data class RecommendationQuery(
    val param: RecommendationParam.Find
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to param.id,
        "mediaId" to param.mediaId,
        "mediaRecommendationId" to param.mediaRecommendationId,
        "onList" to param.onList,
        "rating" to param.rating,
        "rating_greater" to param.rating_greater,
        "rating_lesser" to param.rating_lesser,
        "sort" to param.sort,
        "userId" to param.userId
    )
}