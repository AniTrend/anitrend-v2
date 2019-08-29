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

import co.anitrend.data.model.response.contract.IGraphQuery
import co.anitrend.data.usecase.thread.attributes.ThreadSort

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
    val id: Int? = null,
    val userId: Int? = null,
    val replyUserId: Int? = null,
    val subscribed: Boolean? = null,
    val categoryId: Int? = null,
    val mediaCategoryId: Int? = null,
    val search: String? = null,
    val id_in: List<Int>? = null,
    val sort: List<ThreadSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
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