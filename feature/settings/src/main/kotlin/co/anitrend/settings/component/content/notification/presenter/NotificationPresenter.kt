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
package co.anitrend.settings.component.content.notification.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tv
import co.anitrend.android.core.helpers.notification.config.NotificationConfig
import co.anitrend.android.core.helpers.notification.hasNotificationPermissionFor
import co.anitrend.android.core.helpers.notification.openAppNotificationSettings
import co.anitrend.android.core.helpers.notification.requestPostNotificationPermissionIfPossible
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.settings.R
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

class NotificationPresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
) : CorePresenter(context, settings) {
    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        // Intro hint: request permission first if missing, else open system notification settings
        val hasPermission = context.hasNotificationPermissionFor(NotificationConfig.GENERAL)
        val hintTitle = context.getString(R.string.title_settings_notification_hint)
        val hintDescription =
            if (hasPermission) {
                context.getString(R.string.description_settings_notification_hint)
            } else {
                context.getString(R.string.description_settings_notification_permission)
            }
        val hintAction: () -> Unit =
            if (hasPermission) {
                ({ context.openAppNotificationSettings() })
            } else {
                ({ context.requestPostNotificationPermissionIfPossible() })
            }

        preferenceBuilder.add(
            entries =
                listOf(
                    SettingItem.HintCard(
                        id = "notification_hint",
                        title = hintTitle,
                        description = hintDescription,
                        icon = Icons.Outlined.Notifications,
                        actionLabel =
                            if (hasPermission) {
                                context.getString(R.string.action_settings_notification_open_system_settings)
                            } else {
                                context.getString(R.string.action_settings_notification_grant_permission)
                            },
                        onClick = hintAction,
                    ),
                ),
        )

        // Section header and app-level toggles
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "notification_settings_header",
                    title = context.getString(R.string.title_settings_notification_app_notifications),
                ),
            entries =
                listOf(
                    SettingItem.SwitchSetting(
                        id = "notifications_master",
                        title = context.getString(R.string.label_settings_notifications_master_toggle),
                        summary = context.getString(R.string.summary_settings_notifications_master_toggle),
                        icon = Icons.Outlined.Notifications,
                        onValueChange = { settings.isNotificationsEnabled.value = it },
                        onClick = { settings.isNotificationsEnabled.value },
                    ),
                    SettingItem.SwitchSetting(
                        id = "notifications_anilist",
                        title = context.getString(R.string.label_settings_notifications_anilist_toggle),
                        summary = context.getString(R.string.summary_settings_notifications_anilist_toggle),
                        icon = Icons.Outlined.Tv,
                        onValueChange = { settings.isAniListNotificationsEnabled.value = it },
                        onClick = { settings.isAniListNotificationsEnabled.value },
                        enabled = { settings.isNotificationsEnabled.value },
                    ),
                    SettingItem.SwitchSetting(
                        id = "notifications_announcements",
                        title = context.getString(R.string.label_settings_notifications_announcements_toggle),
                        summary = context.getString(R.string.summary_settings_notifications_announcements_toggle),
                        icon = Icons.AutoMirrored.Outlined.Announcement,
                        onValueChange = { settings.isAnnouncementNotificationsEnabled.value = it },
                        onClick = { settings.isAnnouncementNotificationsEnabled.value },
                        enabled = { settings.isNotificationsEnabled.value },
                    ),
                ),
        )

        return preferenceBuilder.build()
    }
}
