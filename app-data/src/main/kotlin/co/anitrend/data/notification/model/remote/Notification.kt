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

import co.anitrend.data.media.model.remote.Media
import co.anitrend.data.user.model.remote.User
import co.anitrend.data.notification.model.contract.INotification
import co.anitrend.domain.notification.enums.NotificationType

/** [Notification](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationunion.doc.html)
 * Notification with shared objects
 *
 * @param activityId The id of the activity which was liked
 * @param user The user related to this notification type
 * @param media The associated media of the airing schedule
 * @param contexts The notification context text
 */
internal data class Notification(
    val activityId: Int?,
    val user: User?,
    val media: Media?,
    val contexts: List<String>?,
    override val id: Long,
    override val context: String?,
    override val createdAt: Long?,
    override val type: NotificationType?
) : INotification