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

package co.anitrend.data.activity.model.mutation

import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.domain.activity.model.ActivityParam

internal sealed class ActivityMutation : IGraphPayload {

    data class SaveActivity(
        val param: ActivityParam.SaveActivity
    ) : ActivityMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.activityId,
            "text" to param.text
        )
    }

    data class Delete(
        val param: ActivityParam.Delete
    ) : ActivityMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.activityId
        )
    }

    data class SaveReply(
        val param: ActivityParam.SaveReply
    ) : ActivityMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "activityId" to param.activityId,
            "id" to param.activityReplyId,
            "text" to param.text
        )
    }

    data class SaveMessage(
        val param: ActivityParam.SaveMessage
    ) : ActivityMutation() {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.activityId,
            "message" to param.message,
            "recipientId" to param.recipientId
        )
    }
}
