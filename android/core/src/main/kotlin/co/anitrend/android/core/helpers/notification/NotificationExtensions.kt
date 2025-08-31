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

import android.Manifest
import android.content.Context
import android.os.Build
import android.content.Intent
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.helpers.notification.NotificationHelper.Companion.POST_NOTIFICATION_PERMISSION_REQUEST_CODE
import co.anitrend.android.core.helpers.notification.config.NotificationConfig

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

fun Context.openAppNotificationSettings() {
    val intent =
        Intent().apply {
            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            // Fallback extras for older APIs
            putExtra("app_package", packageName)
            putExtra("app_uid", applicationInfo.uid)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    try {
        startActivity(intent)
    } catch (_: Exception) {
        // Fallback to general app settings if specific action not available
        val fallback =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.parse("package:$packageName")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        startActivity(fallback)
    }
}

fun Context.requestPostNotificationPermissionIfPossible() {
    when (this) {
        is FragmentActivity -> this.requestPostNotificationPermission()
        else -> openAppNotificationSettings()
    }
}
