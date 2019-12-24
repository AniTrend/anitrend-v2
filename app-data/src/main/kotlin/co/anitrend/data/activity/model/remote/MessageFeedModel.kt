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

import androidx.room.PrimaryKey
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.activity.entity.MessageFeedEntity
import co.anitrend.data.activity.model.remote.common.IFeedModel
import co.anitrend.data.model.core.user.User
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
data class MessageFeedModel(
    val replies: List<ReplyFeedModel>?,
    val recipient : User?,
    val recipientId : Long?,
    val isLocked: Boolean,
    val message: String?,
    val messenger: User?,
    val messengerId: Long?,
    @PrimaryKey
    override val id: Long,
    override val createdAt: Long,
    override val likes: List<User>?,
    override val siteUrl: String?,
    override val type: ActivityType?,
    override val replyCount: Int
) : IFeedModel {

    companion object : ISupportMapperHelper<MessageFeedModel, MessageFeedEntity> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: MessageFeedModel): MessageFeedEntity {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}