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

package co.anitrend.domain.thread.model.query

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.thread.enums.ThreadSort

/** [Thread query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the thread id
 * @param userId Filter by the user id of the thread's creator
 * @param replyUserId Filter by the user id of the last user to comment on the thread
 * @param subscribed Filter by if the currently authenticated user's subscribed threads
 * @param categoryId Filter by thread category id
 * @param mediaCategoryId Filter by thread media id category
 * @param search Filter by search query
 * @param id_in Filter by the thread id
 * @param sort The order the results will be returned in
 */
data class ThreadQuery(
    val id: Long? = null,
    val userId: Long? = null,
    val replyUserId: Long? = null,
    val subscribed: Boolean? = null,
    val categoryId: Long? = null,
    val mediaCategoryId: Long? = null,
    val search: String? = null,
    val id_in: List<Long>? = null,
    val sort: List<ThreadSort>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "replyUserId" to replyUserId,
        "subscribed" to subscribed,
        "categoryId" to categoryId,
        "mediaCategoryId" to mediaCategoryId,
        "search" to search,
        "id_in" to id_in,
        "sort" to sort
    )
}