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

package co.anitrend.data.review.model.mutation

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.review.model.ReviewParam

internal sealed class ReviewMutation : IGraphPayload {

    data class Delete(
        val param: ReviewParam.Delete
    ) : ReviewMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id
        )
    }

    data class Rate(
        val param: ReviewParam.Rate
    ) : ReviewMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "rating" to param.rating
        )
    }

    data class Save(
        val param: ReviewParam.Save
    ) : ReviewMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "mediaId" to param.mediaId,
            "body" to param.body,
            "summary" to param.summary,
            "score" to param.score,
            "private" to param.private
        )
    }
}
