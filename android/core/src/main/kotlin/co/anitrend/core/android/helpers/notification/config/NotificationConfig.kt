/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.core.android.helpers.notification.config

import android.app.NotificationManager

enum class NotificationConfig(
    val title: String,
    val description: String,
    val importance: Int,
    val group: String,
) {
    GENERAL(
        title = "General",
        description = "AniTrend specific notifications",
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        group = "co.anitrend.notification.group.GENERAL",
    ),
    ANILIST(
        title = "AniList",
        description = "AniList related notifications",
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        group = "co.anitrend.notification.group.ANILIST",
    ),
    ANNOUNCEMENT(
        title = "Announcements",
        description = "Announcements and other important information",
        importance = NotificationManager.IMPORTANCE_HIGH,
        group = "co.anitrend.notification.group.ANNOUNCEMENT",
    ),
}
