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

package co.anitrend.data.user.model.remote.option

import co.anitrend.domain.notification.enums.NotificationType
import co.anitrend.domain.user.enums.UserTitleLanguage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** [UserOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/useroptions.doc.html)
 * A user's general options
 *
 * @param airingNotifications Whether the user receives notifications when a show they are watching airs
 * @param displayAdultContent Whether the user has enabled viewing of 18+ content
 * @param notificationOptions Notification options
 * @param profileColor Profile highlight color (blue, purple, pink, orange, red, green, grey)
 * @param titleLanguage The language the user wants to see media titles in
 */
@Serializable
internal data class UserOptionsModel(
    @SerialName("airingNotifications") val airingNotifications: Boolean?,
    @SerialName("displayAdultContent") val displayAdultContent: Boolean?,
    @SerialName("notificationOptions") val notificationOptions: List<NotificationOptions>?,
    @SerialName("profileColor") val profileColor: String?,
    @SerialName("titleLanguage") val titleLanguage: UserTitleLanguage?
) {

    /**
     * @param enabled Whether this type of notification is enabled
     * @param notificationType The type of notification
     */
    @Serializable
    internal data class NotificationOptions(
        @SerialName("enabled") val enabled: Boolean,
        @SerialName("type") val notificationType: NotificationType
    )
}