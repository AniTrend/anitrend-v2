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

package co.anitrend.data.model.query.activity

import co.anitrend.data.model.contract.FuzzyDateInt
import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.usecase.feed.attributes.ActivitySort
import co.anitrend.data.usecase.feed.attributes.ActivityType

/** [Activity query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the activity id
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
data class ActivityQuery(
    val id: Int? = null,
    val userId: Int? = null,
    val messengerId: Int? = null,
    val mediaId: Int? = null,
    val type: ActivityType? = null,
    val isFollowing: Boolean? = null,
    val hasReplies: Boolean? = null,
    val hasRepliesOrTypeText: Boolean? = null,
    val createdAt: Int? = null,
    val id_not: Int? = null,
    val id_in: List<Int>? = null,
    val id_not_in: List<Int>? = null,
    val userId_not: Int? = null,
    val userId_in: List<Int>? = null,
    val userId_not_in: List<Int>? = null,
    val messengerId_not: Int? = null,
    val messengerId_in: List<Int>? = null,
    val messengerId_not_in: List<Int>? = null,
    val mediaId_not: Int? = null,
    val mediaId_in: List<Int>? = null,
    val mediaId_not_in: List<Int>? = null,
    val type_not: ActivitySort? = null,
    val type_in: List<ActivitySort>? = null,
    val type_not_in: List<ActivitySort>? = null,
    val createdAt_greater: FuzzyDateInt? = null,
    val createdAt_lesser: FuzzyDateInt? = null,
    val sort: List<ActivitySort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "messengerId" to messengerId,
        "mediaId" to mediaId,
        "type" to type,
        "isFollowing" to isFollowing,
        "hasReplies" to hasReplies,
        "hasRepliesOrTypeText" to hasRepliesOrTypeText,
        "createdAt" to createdAt,
        "id_not" to id_not,
        "id_in" to id_in,
        "id_not_in" to id_not_in,
        "userId_not" to userId_not,
        "userId_in" to userId_in,
        "userId_not_in" to userId_not_in,
        "messengerId_not" to messengerId_not,
        "messengerId_in" to messengerId_in,
        "messengerId_not_in" to messengerId_not_in,
        "mediaId_not" to mediaId_not,
        "mediaId_in" to mediaId_in,
        "mediaId_not_in" to mediaId_not_in,
        "type_not" to type_not,
        "type_in" to type_in,
        "type_not_in" to type_not_in,
        "createdAt_greater" to createdAt_greater,
        "createdAt_lesser" to createdAt_lesser,
        "sort" to sort
    )
}