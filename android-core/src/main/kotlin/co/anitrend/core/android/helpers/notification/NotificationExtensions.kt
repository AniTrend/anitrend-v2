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
package co.anitrend.core.android.helpers.notification

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.helpers.notification.NotificationHelper.Companion.POST_NOTIFICATION_PERMISSION_REQUEST_CODE
import co.anitrend.core.android.helpers.notification.config.NotificationConfig

fun Context.hasNotificationPermissionFor(config: NotificationConfig): Boolean {
    val hasPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(this).areNotificationsEnabled()
        }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasPermission) {
        val channel = NotificationManagerCompat.from(this).getNotificationChannel(config.name)
        if (channel != null && channel.importance == config.importance) {
            return false
        }
    }
    return hasPermission
}

fun FragmentActivity.requestPostNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            // context =
            this,
            // permission =
            Manifest.permission.POST_NOTIFICATIONS,
        ) != PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            // activity =
            this,
            // permissions =
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            // requestCode =
            POST_NOTIFICATION_PERMISSION_REQUEST_CODE,
        )
    }
}
