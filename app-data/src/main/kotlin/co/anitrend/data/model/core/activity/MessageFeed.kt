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

package co.anitrend.data.model.core.activity

import androidx.room.PrimaryKey
import co.anitrend.domain.activity.entities.IFeed
import co.anitrend.data.model.core.user.User
import co.anitrend.domain.activity.enums.ActivityType
import com.google.gson.annotations.SerializedName

/** [MessageActivity](https://anilist.github.io/ApiV2-GraphQL-Docs/messageactivity.doc.html)
 * User message activity
 *
 * @param replies The written replies to the activity
 * @param user The user who sent the activity message
 * @param userId The user id of the activity's sender
 * @param recipient The user who the activity message was sent to
 * @param recipientId The user id of the activity's recipient
 * @param isLocked If the activity is locked and can receive replies
 */
data class MessageFeed(
    val replies: List<FeedReply>?,
    val recipient : User?,
    val recipientId : Long?,
    val isLocked: Boolean,
    @PrimaryKey
    override val id: Long,
    override val createdAt: Long,
    override val likes: List<User>?,
    override val replyCount: Int,
    override val siteUrl: String?,
    @SerializedName("message")
    override val text: String?,
    override val type: ActivityType?,
    @SerializedName("messenger")
    override val user: User?,
    @SerializedName("messengerId")
    override val userId: Long?
) : IFeed