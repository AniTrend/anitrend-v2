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

package co.anitrend.data.thread.model.query

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.thread.enums.ThreadSort
import co.anitrend.domain.thread.model.ThreadParam

internal sealed class ThreadQuery : IGraphPayload {

    data class Find(
        val param: ThreadParam.Find
    ) : ThreadQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "userId" to param.userId,
            "replyUserId" to param.replyUserId,
            "subscribed" to param.subscribed,
            "categoryId" to param.categoryId,
            "mediaCategoryId" to param.mediaCategoryId,
            "search" to param.search,
            "id_in" to param.id_in,
            "sort" to param.sort
        )
    }

    data class FindComment(
        val param: ThreadParam.FindComment
    ) : ThreadQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "threadId" to param.threadId,
            "userId" to param.userId
        )
    }
}