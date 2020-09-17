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

package co.anitrend.data.activity.model.remote

import co.anitrend.data.activity.model.remote.common.IFeedModel
import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.domain.activity.enums.ActivityType
import com.google.gson.annotations.SerializedName

/** [MessageActivity](https://anilist.github.io/ApiV2-GraphQL-Docs/messageactivity.doc.html)
 * User message activity
 *
 * @param replies The written replies to the activity
 * @param message The message text (Markdown)
 * @param messenger The user who sent the activity message
 * @param messengerId The user id of the activity's sender
 * @param recipient The user who the activity message was sent to
 * @param recipientId The user id of the activity's recipient
 * @param isLocked If the activity is locked and can receive replies
 */
internal data class MessageFeed(
    val replies: List<ReplyFeed>?,
    val recipient : UserModelCore?,
    val recipientId : Long?,
    val isLocked: Boolean,
    val message: String?,
    val messenger: UserModelCore?,
    val messengerId: Long?,
    @SerializedName("id") override val id: Long,
    @SerializedName("createdAt") override val createdAt: Long,
    @SerializedName("likes") override val likes: List<UserModelCore>?,
    @SerializedName("siteUrl") override val siteUrl: String?,
    @SerializedName("type") override val type: ActivityType?,
    @SerializedName("replyCount") override val replyCount: Int
) : IFeedModel