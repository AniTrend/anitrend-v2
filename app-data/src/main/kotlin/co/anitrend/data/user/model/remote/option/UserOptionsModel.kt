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

/** [UserOptions](https://anilist.github.io/ApiV2-GraphQL-Docs/useroptions.doc.html)
 * A user's general options
 *
 * @param airingNotifications Whether the user receives notifications when a show they are watching aires
 * @param displayAdultContent Whether the user has enabled viewing of 18+ content
 * @param notificationOptions Notification options
 * @param profileColor Profile highlight color (blue, purple, pink, orange, red, green, grey)
 * @param titleLanguage The language the user wants to see media titles in
 */
internal data class UserOptionsModel(
    val airingNotifications: Boolean?,
    val displayAdultContent: Boolean?,
    val notificationOptions: List<NotificationOptions>?,
    val profileColor: String?,
    val titleLanguage: UserTitleLanguage?
) {

    /**
     * @param enabled Whether this type of notification is enabled
     * @param notificationType The type of notification
     */
    internal data class NotificationOptions(
        val enabled: Boolean?,
        val notificationType: NotificationType?
    )
}