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
package co.anitrend.android.core.helpers.notification

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import co.anitrend.android.core.helpers.notification.config.NotificationConfig

class NotificationHelper(
    private val notificationManager: NotificationManagerCompat,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannels() {
        val channels =
            NotificationConfig.entries.map { config ->
                return with(NotificationChannel(config.name, config.title, config.importance)) {
                    description = config.description
                    group = config.group
                    setShowBadge(true)
                    enableLights(false)
                }
            }
        notificationManager.createNotificationChannels(channels)
    }

    companion object {
        const val POST_NOTIFICATION_PERMISSION_REQUEST_CODE = 0x12

        fun notificationVisibilityFor(isAdult: Boolean) = if (isAdult) NotificationCompat.VISIBILITY_SECRET else NotificationCompat.VISIBILITY_PUBLIC
    }
}
