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

package co.anitrend.data.notification.model.remote

import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.notification.model.contract.INotificationModel
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.notification.enums.NotificationType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationunion.doc.html)
 * Notification with shared objects
 *
 * @property activityId The id of the activity which was liked
 * @property user The user related to this notification type
 * @property media The associated media of the airing schedule
 * @property contexts The notification context text
 */
@Serializable
internal sealed class NotificationModel : INotificationModel {

    abstract val activityId: Int?
    abstract val user: UserModel?
    abstract val media: MediaModel?
    abstract val contexts: List<String>?

    @Serializable
    internal data class Core(
        @SerialName("activityId") override val activityId: Int? = null,
        @SerialName("user") override val user: UserModel.Core? = null,
        @SerialName("media") override val media: MediaModel.Core? = null,
        @SerialName("contexts") override val contexts: List<String>? = null,
        @SerialName("context") override val context: String? = null,
        @SerialName("createdAt") override val createdAt: Long? = null,
        @SerialName("type") override val type: NotificationType? = null,
        @SerialName("id") override val id: Long,
    ) : NotificationModel()

    @Serializable
    internal data class Extended(
        @SerialName("activityId") override val activityId: Int? = null,
        @SerialName("user") override val user: UserModel.Core? = null,
        @SerialName("media") override val media: MediaModel.Core? = null,
        @SerialName("contexts") override val contexts: List<String>? = null,
        @SerialName("context") override val context: String? = null,
        @SerialName("createdAt") override val createdAt: Long? = null,
        @SerialName("type") override val type: NotificationType? = null,
        @SerialName("id") override val id: Long,
    ) : NotificationModel()
}