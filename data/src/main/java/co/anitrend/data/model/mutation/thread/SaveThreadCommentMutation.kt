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

package co.anitrend.data.model.mutation.thread

import co.anitrend.data.model.response.contract.IGraphQuery

/** [SaveThreadComment mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update a thread comment
 *
 * @param id The comment id, required for updating
 * @param threadId The id of thread the comment belongs to
 * @param parentCommentId The id of thread comment to reply to
 * @param comment The comment markdown text
 */
data class SaveThreadCommentMutation(
    val id: Int? = null,
    val threadId: Int,
    val parentCommentId: Int,
    val comment: String
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "threadId" to threadId,
        "parentCommentId" to parentCommentId,
        "comment" to comment
    )
}