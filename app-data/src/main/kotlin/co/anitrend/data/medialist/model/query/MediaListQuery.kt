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

package co.anitrend.data.medialist.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.medialist.model.MediaListParam

internal sealed class MediaListQuery : IGraphPayload {

    data class Entry(
        val param: MediaListParam.Entry
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "scoreFormat" to param.scoreFormat,
            "mediaId" to param.mediaId,
            "userId" to param.userId
        )
    }

    data class Paged(
        val param: MediaListParam.Paged
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "id" to param.id,
            "userId" to param.userId,
            "type" to param.type,
            "status" to param.status,
            "mediaId" to param.mediaId,
            "scoreFormat" to param.scoreFormat,
            "isFollowing" to param.isFollowing,
            "notes" to param.notes,
            "startedAt" to param.startedAt,
            "completedAt" to param.completedAt,
            "userId_in" to param.userId_in,
            "notes_like" to param.notes_like,
            "status_in" to param.status_in,
            "status_not" to param.status_not,
            "status_not_in" to param.status_not_in,
            "startedAt_greater" to param.startedAt_greater,
            "startedAt_lesser" to param.startedAt_lesser,
            "startedAt_like" to param.startedAt_like,
            "completedAt_greater" to param.completedAt_greater,
            "completedAt_lesser" to param.completedAt_lesser,
            "completedAt_like" to param.completedAt_like,
            "sort" to param.sort,
            "compareWithAuthList" to param.compareWithAuthList
        )
    }

    data class Collection(
        val param: MediaListParam.Collection
    ) : IGraphPayload {

        /**
         * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
         */
        override fun toMap() = mapOf(
            "chunk" to param.chunk,
            "scoreFormat" to param.scoreFormat,
            "completedAt" to param.completedAt,
            "completedAt_greater" to param.completedAt_greater,
            "completedAt_lesser" to param.completedAt_lesser,
            "completedAt_like" to param.completedAt_like,
            "forceSingleCompletedList" to param.forceSingleCompletedList,
            "notes" to param.notes,
            "notes_like" to param.notes_like,
            "perChunk" to param.perChunk,
            "sort" to param.sort,
            "startedAt" to param.startedAt,
            "startedAt_greater" to param.startedAt_greater,
            "startedAt_lesser" to param.startedAt_lesser,
            "startedAt_like" to param.startedAt_like,
            "status" to param.status,
            "status_in" to param.status_in,
            "status_not" to param.status_not,
            "status_not_in" to param.status_not_in,
            "type" to param.type,
            "userId" to param.userId
        )
    }
}