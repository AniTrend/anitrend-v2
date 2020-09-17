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

import co.anitrend.data.media.model.MediaModelCore
import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.data.user.model.remote.UserModelCore
import co.anitrend.data.notification.model.contract.INotificationModel
import co.anitrend.domain.notification.enums.NotificationType
import com.google.gson.annotations.SerializedName

/** [Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationunion.doc.html)
 * Notification with shared objects
 *
 * @param activityId The id of the activity which was liked
 * @param user The user related to this notification type
 * @param media The associated media of the airing schedule
 * @param contexts The notification context text
 */
internal data class NotificationModel(
    @SerializedName("activityId") val activityId: Int?,
    @SerializedName("user") val user: UserModelCore?,
    @SerializedName("media") val media: MediaModelCore?,
    @SerializedName("contexts") val contexts: List<String>?,
    @SerializedName("context") override val context: String?,
    @SerializedName("createdAt") override val createdAt: Long?,
    @SerializedName("type") override val type: NotificationType?,
    @SerializedName("id") override val id: Long,
) : INotificationModel