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

package co.anitrend.domain.thread.model

import co.anitrend.domain.thread.enums.ThreadSort

sealed class ThreadParam {

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
    data class Find(
        val id: Long? = null,
        val userId: Long? = null,
        val replyUserId: Long? = null,
        val subscribed: Boolean? = null,
        val categoryId: Long? = null,
        val mediaCategoryId: Long? = null,
        val search: String? = null,
        val id_in: List<Long>? = null,
        val sort: List<ThreadSort>? = null
    ) : ThreadParam()

    /** [ThreadComment query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by the comment id
     * @param threadId Filter by the thread id
     * @param userId Filter by the user id of the comment's creator
     */
    data class FindComment(
        val id: Long? = null,
        val threadId: Long? = null,
        val userId: Long? = null
    ) : ThreadParam()

    /** [DeleteThread mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     *  Delete a thread
     *
     *  @param id The id of the thread to delete
     */
    data class Delete(
        val id: Long
    ) : ThreadParam()

    /** [SaveThreadComment mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Create or update a thread comment
     *
     * @param id The comment id, required for updating
     * @param threadId The id of thread the comment belongs to
     * @param parentCommentId The id of thread comment to reply to
     * @param comment The comment markdown text
     */
    data class SaveComment(
        val id: Long? = null,
        val threadId: Long,
        val parentCommentId: Long,
        val comment: String
    ) : ThreadParam()

    /** [SaveThread mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Create or update a forum thread
     *
     * @param id The thread id, required for updating
     * @param title The title of the thread
     * @param body The main text body of the thread
     * @param categories Forum categories the thread should be within
     * @param mediaCategories Media related to the contents of the thread
     */
    data class Save(
        val id: Long? = null,
        val title: String,
        val body: String,
        val categories: List<Long>,
        val mediaCategories: List<Long>
    ) : ThreadParam()

    /** [ToggleThreadSubscription mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Toggle the subscription of a forum thread
     *
     * @param threadId The id of the forum thread to un/subscribe
     * @param subscribe Whether to subscribe or unsubscribe from the forum thread
     */
    data class ToggleSubscribe(
        val threadId: Long,
        val subscribe: Boolean
    ) : ThreadParam()
}
