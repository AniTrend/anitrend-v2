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

package co.anitrend.data.notification.model.input

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.notification.enums.NotificationType
import kotlinx.android.parcel.Parcelize

/** [NotificationOptionInput](https://anilist.github.io/ApiV2-GraphQL-Docs/notificationoptioninput.doc.html)
 *
 * Notification option input
 *
 * @param enabled Whether this type of notification is enabled
 * @param type The type of notification
 */
@Parcelize
data class NotificationOptionInput(
    val enabled: Boolean,
    val type: NotificationType
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "enabled" to enabled,
        "type" to type
    )
}