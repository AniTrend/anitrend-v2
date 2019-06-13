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

package co.anitrend.data.model.query.thread

import co.anitrend.data.model.contract.IGraphQuery

/** [ThreadComment query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the comment id
 * @param threadId Filter by the thread id
 * @param userId Filter by the user id of the comment's creator
 */
data class ThreadCommentQuery(
    val id: Int? = null,
    val threadId: Int? = null,
    val userId: Int? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "threadId" to threadId,
        "userId" to userId
    )
}