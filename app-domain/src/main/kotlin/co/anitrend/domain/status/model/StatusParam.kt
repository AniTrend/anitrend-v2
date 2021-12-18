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

package co.anitrend.domain.status.model

import co.anitrend.domain.status.enums.StatusSort
import co.anitrend.domain.status.enums.StatusType
import co.anitrend.domain.common.DateInt

sealed class StatusParam {

    /** [SaveTextActivity mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Create or update text activity for the currently authenticated user
     *
     * @param text The activity text
     * @param activityId The activity's id, required if updating
     */
    data class Save(
        val text: String,
        val activityId: Long? = null,
    ) : StatusParam()

    /** [DeleteActivity mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html) &
     * [DeleteActivityReply mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Delete an activity item of the authenticated users
     *
     * @param activityId The id of the activity to delete
     */
    data class Delete(
        val activityId: Long
    ) : StatusParam()

    /** [SaveActivityReply mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     *  Create or update an activity reply
     *
     * @param activityId The id of the parent activity being replied to
     * @param text The reply text
     * @param activityReplyId The activity reply id, required for updating
     */
    data class SaveReply(
        val activityId: Long,
        val text: String,
        val activityReplyId: Long? = null,
    ) : StatusParam()

    /** [SaveMessageActivity mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
     *
     * Create or update message activity for the currently authenticated user
     *
     * @param message The activity message text
     * @param recipientId The id of the user the message is being sent to
     * @param activityId The activity id, required for updating
     */
    data class SaveMessage(
        val message: String,
        val recipientId: Long,
        val activityId: Long? = null
    ) : StatusParam()

    /** [ActivityReply query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param id Filter by the reply id
     * @param activityId Filter by the parent id
     */
    data class FindReply(
        val id: Long? = null,
        val activityId: Long? = null
    ) : StatusParam()

    /** [Activity query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
     *
     * @param activityId Filter by the activity id
     * @param userId Filter by the owner user id
     * @param messengerId Filter by the id of the user who sent a message
     * @param mediaId Filter by the associated media id of the activity
     * @param type Filter by the type of activity
     * @param isFollowing Filter activity to users who are being followed by the authenticated user
     * @param hasReplies Filter activity to only activity with replies
     * @param hasRepliesOrTypeText Filter activity to only activity with replies or is of type text
     * @param createdAt Filter by the time the activity was created
     * @param id_not Filter by the activity id
     * @param id_in Filter by the activity id
     * @param id_not_in Filter by the activity id
     * @param userId_not Filter by the owner user id
     * @param userId_in Filter by the owner user id
     * @param userId_not_in Filter by the owner user id
     * @param messengerId_not Filter by the id of the user who sent a message
     * @param messengerId_in Filter by the id of the user who sent a message
     * @param messengerId_not_in Filter by the id of the user who sent a message
     * @param mediaId_not Filter by the associated media id of the activity
     * @param mediaId_in Filter by the associated media id of the activity
     * @param mediaId_not_in Filter by the associated media id of the activity
     * @param type_not Filter by the type of activity
     * @param type_in Filter by the type of activity
     * @param type_not_in Filter by the type of activity
     * @param createdAt_greater Filter by the time the activity was created
     * @param createdAt_lesser Filter by the time the activity was created
     * @param sort The order the results will be returned in
     */
    data class Find(
        val activityId: Long? = null,
        val userId: Long? = null,
        val messengerId: Long? = null,
        val mediaId: Long? = null,
        val type: StatusType? = null,
        val isFollowing: Boolean? = null,
        val hasReplies: Boolean? = null,
        val hasRepliesOrTypeText: Boolean? = null,
        val createdAt: Long? = null,
        val id_not: Long? = null,
        val id_in: List<Long>? = null,
        val id_not_in: List<Long>? = null,
        val userId_not: Long? = null,
        val userId_in: List<Long>? = null,
        val userId_not_in: List<Long>? = null,
        val messengerId_not: Long? = null,
        val messengerId_in: List<Long>? = null,
        val messengerId_not_in: List<Long>? = null,
        val mediaId_not: Long? = null,
        val mediaId_in: List<Long>? = null,
        val mediaId_not_in: List<Long>? = null,
        val type_not: StatusSort? = null,
        val type_in: List<StatusSort>? = null,
        val type_not_in: List<StatusSort>? = null,
        val createdAt_greater: DateInt? = null,
        val createdAt_lesser: DateInt? = null,
        val sort: List<StatusSort>? = null
    ) : StatusParam() {
        infix fun builder(param: Find.() -> Unit): Find {
            this.param()
            return this
        }
    }
}