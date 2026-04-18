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
package co.anitrend.settings.component.content.notification

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Announcement
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.helpers.notification.config.NotificationConfig
import co.anitrend.android.core.helpers.notification.hasNotificationPermissionFor
import co.anitrend.android.core.helpers.notification.openAppNotificationSettings
import co.anitrend.android.core.helpers.notification.requestPostNotificationPermissionIfPossible
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.settings.notification.INotificationSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsHeroCard
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsToggleRow
import org.koin.compose.koinInject

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    settings: INotificationSettings = koinInject(),
) {
    val context = LocalContext.current
    val hasPermission = context.hasNotificationPermissionFor(NotificationConfig.GENERAL)
    var notificationsEnabled by remember { mutableStateOf(settings.isNotificationsEnabled.value) }
    var aniListEnabled by remember { mutableStateOf(settings.isAniListNotificationsEnabled.value) }
    var announcementsEnabled by remember { mutableStateOf(settings.isAnnouncementNotificationsEnabled.value) }

    NotificationContent(
        modifier = modifier,
        hasPermission = hasPermission,
        notificationsEnabled = notificationsEnabled,
        aniListEnabled = aniListEnabled,
        announcementsEnabled = announcementsEnabled,
        onSystemSettingsClick = {
            if (hasPermission) {
                context.openAppNotificationSettings()
            } else {
                context.requestPostNotificationPermissionIfPossible()
            }
        },
        onNotificationsEnabledChange = {
            settings.isNotificationsEnabled.value = it
            notificationsEnabled = it
        },
        onAniListEnabledChange = {
            settings.isAniListNotificationsEnabled.value = it
            aniListEnabled = it
        },
        onAnnouncementsEnabledChange = {
            settings.isAnnouncementNotificationsEnabled.value = it
            announcementsEnabled = it
        },
    )
}

@Composable
private fun NotificationContent(
    modifier: Modifier = Modifier,
    hasPermission: Boolean,
    notificationsEnabled: Boolean,
    aniListEnabled: Boolean,
    announcementsEnabled: Boolean,
    onSystemSettingsClick: () -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit = {},
    onAniListEnabledChange: (Boolean) -> Unit = {},
    onAnnouncementsEnabledChange: (Boolean) -> Unit = {},
) {
    val permissionState =
        if (hasPermission) {
            stringResource(R.string.label_settings_notification_permission_ready)
        } else {
            stringResource(R.string.label_settings_notification_permission_required)
        }

    LazyColumn(modifier = modifier) {
        item {
            SettingsHeroCard(
                title = stringResource(R.string.title_settings_notification_hint),
                description =
                    if (hasPermission) {
                        stringResource(R.string.description_settings_notification_hint)
                    } else {
                        stringResource(R.string.description_settings_notification_permission)
                    },
                icon = Icons.Outlined.Notifications,
                currentValue = permissionState,
                actionLabel =
                    if (hasPermission) {
                        stringResource(R.string.action_settings_notification_open_system_settings)
                    } else {
                        stringResource(R.string.action_settings_notification_grant_permission)
                    },
                onClick = onSystemSettingsClick,
            )
        }
        item {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_notification_delivery_controls),
                description = stringResource(R.string.summary_settings_notification_delivery_controls),
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.label_settings_notifications_master_toggle),
                    summary = stringResource(R.string.summary_settings_notifications_master_toggle),
                    icon = Icons.Outlined.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationsEnabledChange,
                )
            }
        }
        item {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_notification_categories),
                description =
                    if (notificationsEnabled) {
                        stringResource(R.string.summary_settings_notification_categories)
                    } else {
                        stringResource(R.string.summary_settings_notification_categories_disabled)
                    },
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.label_settings_notifications_anilist_toggle),
                    summary = stringResource(R.string.summary_settings_notifications_anilist_toggle),
                    icon = Icons.Outlined.Tv,
                    checked = aniListEnabled,
                    enabled = notificationsEnabled,
                    onCheckedChange = onAniListEnabledChange,
                )
                SettingsToggleRow(
                    title = stringResource(R.string.label_settings_notifications_announcements_toggle),
                    summary = stringResource(R.string.summary_settings_notifications_announcements_toggle),
                    icon = Icons.AutoMirrored.Outlined.Announcement,
                    checked = announcementsEnabled,
                    enabled = notificationsEnabled,
                    onCheckedChange = onAnnouncementsEnabledChange,
                )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun NotificationScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        NotificationContent(
            hasPermission = false,
            notificationsEnabled = true,
            aniListEnabled = true,
            announcementsEnabled = false,
            onSystemSettingsClick = {},
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun NotificationScreenDisabledPreview() {
    PreviewTheme(wrapInSurface = true, darkTheme = true) {
        NotificationContent(
            hasPermission = true,
            notificationsEnabled = false,
            aniListEnabled = false,
            announcementsEnabled = false,
            onSystemSettingsClick = {},
        )
    }
}
