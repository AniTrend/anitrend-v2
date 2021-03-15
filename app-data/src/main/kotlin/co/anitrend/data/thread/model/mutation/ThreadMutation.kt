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

package co.anitrend.data.thread.model.mutation

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.thread.model.ThreadParam

internal sealed class ThreadMutation : IGraphPayload {

    data class Delete(
        val param: ThreadParam.Delete
    ) : ThreadMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id
        )
    }

    data class SaveComment(
        val param: ThreadParam.SaveComment
    ) : ThreadMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "threadId" to param.threadId,
            "parentCommentId" to param.parentCommentId,
            "comment" to param.comment
        )
    }

    data class Save(
        val param: ThreadParam.Save
    ) : ThreadMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "title" to param.title,
            "body" to param.body,
            "categories" to param.categories,
            "mediaCategories" to param.mediaCategories
        )
    }

    data class ToggleSubscribe(
        val param: ThreadParam.ToggleSubscribe
    ) : ThreadMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "threadId" to param.threadId,
            "subscribe" to param.subscribe
        )
    }
}
