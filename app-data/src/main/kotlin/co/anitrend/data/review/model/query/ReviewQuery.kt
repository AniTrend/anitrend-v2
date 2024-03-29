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

package co.anitrend.data.review.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.review.model.ReviewParam

internal sealed class ReviewQuery : IGraphPayload {

    data class Entry(
        val param: ReviewParam.Entry
    ) : ReviewQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
        )
    }

    data class Paged(
        val param: ReviewParam.Paged
    ) : ReviewQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "mediaId" to param.mediaId,
            "userId" to param.userId,
            "mediaType" to param.mediaType,
            "sort" to param.sort,
            "scoreFormat" to param.scoreFormat,
        )
    }
}