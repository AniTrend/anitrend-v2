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

package co.anitrend.data.activity.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.activity.model.ActivityParam

internal sealed class ActivityQuery : IGraphPayload {

    data class Find(
        val param: ActivityParam.Find
    ) : ActivityQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.activityId,
            "userId" to param.userId,
            "messengerId" to param.messengerId,
            "mediaId" to param.mediaId,
            "type" to param.type,
            "isFollowing" to param.isFollowing,
            "hasReplies" to param.hasReplies,
            "hasRepliesOrTypeText" to param.hasRepliesOrTypeText,
            "createdAt" to param.createdAt,
            "id_not" to param.id_not,
            "id_in" to param.id_in,
            "id_not_in" to param.id_not_in,
            "userId_not" to param.userId_not,
            "userId_in" to param.userId_in,
            "userId_not_in" to param.userId_not_in,
            "messengerId_not" to param.messengerId_not,
            "messengerId_in" to param.messengerId_in,
            "messengerId_not_in" to param.messengerId_not_in,
            "mediaId_not" to param.mediaId_not,
            "mediaId_in" to param.mediaId_in,
            "mediaId_not_in" to param.mediaId_not_in,
            "type_not" to param.type_not,
            "type_in" to param.type_in,
            "type_not_in" to param.type_not_in,
            "createdAt_greater" to param.createdAt_greater,
            "createdAt_lesser" to param.createdAt_lesser,
            "sort" to param.sort
        )
    }

    data class FindReply(
        val param: ActivityParam.FindReply
    ) : ActivityQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "activityId" to param.activityId
        )
    }

    data class Markdown(
        val markdown: String
    ) : ActivityQuery() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "markdown" to markdown
        )
    }
}