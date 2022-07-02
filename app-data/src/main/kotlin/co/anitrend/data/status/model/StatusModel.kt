/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.status.model

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.status.model.contract.IStatusModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.status.enums.StatusType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class StatusModel : IStatusModel {

    /**
     * @param media The associated media to the activity update
     * @param progress The list progress made
     * @param status The list item's textual status
     * @param isLocked If the activity is locked and can receive replies
     * @param isSubscribed If the currently authenticated user is subscribed to the activity
     * @param user The user who created the activity
     */
    @Serializable
    internal data class Progress(
        @SerialName("media") val media: MediaModel.Core?,
        @SerialName("status") val status: String?,
        @SerialName("isLocked") val isLocked: Boolean,
        @SerialName("isPinned") val isPinned: Boolean,
        @SerialName("isSubscribed") val isSubscribed: Boolean,
        @SerialName("progress") val progress: String?,
        @SerialName("user") val user: UserModel.Core?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("isLiked") override val isLiked: Boolean?,
        @SerialName("likeCount") override val likeCount: Int,
        @SerialName("replyCount") override val replyCount: Int,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("type") override val type: StatusType?,
        @SerialName("id") override val id: Long
    ) : StatusModel()


    /**
     * @param message The message text
     * @param messenger The user who sent the activity message
     * @param recipient The user who the activity message was sent to
     * @param isLocked If the activity is locked and can receive replies
     * @param isSubscribed If the currently authenticated user is subscribed to the activity
     */
    @Serializable
    internal data class Message(
        @SerialName("recipient") val recipient : UserModel.Core?,
        @SerialName("isLocked") val isLocked: Boolean,
        @SerialName("isSubscribed") val isSubscribed: Boolean,
        @SerialName("message") val message: String?,
        @SerialName("messenger") val messenger: UserModel.Core?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("isLiked") override val isLiked: Boolean?,
        @SerialName("likeCount") override val likeCount: Int,
        @SerialName("replyCount") override val replyCount: Int,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("type") override val type: StatusType?,
        @SerialName("id") override val id: Long
    ) : StatusModel()

    /**
     * @param activityId The id of the parent activity
     * @param user The user who created the activity
     * @param text The status text (Markdown)
     */
    @Serializable
    internal data class Reply(
        @SerialName("activityId") val activityId: Long?,
        @SerialName("text") val text: String?,
        @SerialName("user") val user: UserModel.Core?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("isLiked") override val isLiked: Boolean?,
        @SerialName("likeCount") override val likeCount: Int,
        @SerialName("replyCount") override val replyCount: Int,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("type") override val type: StatusType?,
        @SerialName("id") override val id: Long
    ) : StatusModel()

    /**
     * @param isLocked If the activity is locked and can receive replies
     * @param isSubscribed If the currently authenticated user is subscribed to the activity
     * @param user The user who created the activity
     * @param text The status text (Markdown)"
     */
    @Serializable
    internal data class Text(
        @SerialName("isLocked") val isLocked: Boolean,
        @SerialName("isPinned") val isPinned: Boolean,
        @SerialName("isSubscribed") val isSubscribed: Boolean,
        @SerialName("text") val text: String?,
        @SerialName("user") val user: UserModel.Core?,
        @SerialName("createdAt") override val createdAt: Long,
        @SerialName("isLiked") override val isLiked: Boolean?,
        @SerialName("likeCount") override val likeCount: Int,
        @SerialName("replyCount") override val replyCount: Int,
        @SerialName("siteUrl") override val siteUrl: String?,
        @SerialName("type") override val type: StatusType?,
        @SerialName("id") override val id: Long
    ) : StatusModel()
}