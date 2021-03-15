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

package co.anitrend.data.recommendation.model.mutation

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.recommendation.RecommendationParam

internal sealed class RecommendationMutation {

    internal data class Save(
        val param: RecommendationParam.Save
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "mediaId" to param.mediaId,
            "mediaRecommendationId" to param.mediaRecommendationId,
            "rating" to param.rating
        )
    }
}
