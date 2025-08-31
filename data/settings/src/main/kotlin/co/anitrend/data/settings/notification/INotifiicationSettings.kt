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
package co.anitrend.data.settings.notification

import co.anitrend.arch.extension.settings.contract.AbstractSetting

/**
 * App-level notification preferences.
 *
 * These settings allow users to toggle AniTrend-specific notifications without
 * affecting the system notification permission or channel configuration.
 * Use these flags to gate scheduling and display of notifications in the app.
 */
interface INotificationSettings {
    /** Master toggle for all AniTrend app notifications. */
    val isNotificationsEnabled: AbstractSetting<Boolean>

    /** Toggle for AniList-related content notifications (e.g., follows, replies). */
    val isAniListNotificationsEnabled: AbstractSetting<Boolean>

    /** Toggle for announcements and important updates from AniTrend. */
    val isAnnouncementNotificationsEnabled: AbstractSetting<Boolean>
}
