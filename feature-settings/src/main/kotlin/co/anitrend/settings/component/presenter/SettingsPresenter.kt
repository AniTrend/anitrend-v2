/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.settings.component.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LayersClear
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Translate
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.android.settings.helper.locale.model.AniTrendLocale
import co.anitrend.core.android.settings.helper.locale.model.AniTrendLocale.Companion.asLocaleString
import co.anitrend.core.android.settings.helper.theme.model.AniTrendTheme
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.settings.model.SettingItem

class SettingsPresenter(
    context: Context,
    settings: Settings,
) : CorePresenter(context, settings) {
    fun getSettingsItems(): List<SettingItem> =
        listOf(
            // Top-level preferences (not in a category)
            SettingItem.ClickableSetting(
                id = "accounts",
                title = context.getString(co.anitrend.settings.R.string.preference_title_accounts),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_accounts),
                icon = Icons.Filled.AccountCircle,
                onClick = { /* TODO: Navigate to Accounts screen */ },
            ),
            SettingItem.ClickableSetting(
                id = "privacy_top",
                title = context.getString(co.anitrend.settings.R.string.preference_title_privacy),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_privacy),
                icon = Icons.Filled.PrivacyTip,
                onClick = { /* TODO: Navigate to Privacy screen */ },
            ),
            // Developer Settings category
            SettingItem.CategoryHeader(
                id = "developer_settings",
                title = context.getString(co.anitrend.settings.R.string.preference_group_title_developer_settings),
            ),
            SettingItem.SwitchSetting(
                id = "heap_dump",
                title = context.getString(co.anitrend.settings.R.string.preference_title_heap_dump),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_heap),
                icon = Icons.Filled.Memory,
                checked = { settings.automaticHeapDump.value },
                onValueChange = { newValue -> settings.automaticHeapDump.value = newValue },
            ),
            SettingItem.SwitchSetting(
                id = "clear_db_on_refresh",
                title = context.getString(co.anitrend.settings.R.string.preference_title_refresh_behavior_config),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_refresh_behavior_config),
                icon = Icons.Filled.LayersClear,
                checked = { settings.clearDataOnSwipeRefresh.value },
                onValueChange = { newValue -> settings.clearDataOnSwipeRefresh.value = newValue },
            ),
            // Application Behavior category
            SettingItem.CategoryHeader(
                id = "application_behavior",
                title = context.getString(co.anitrend.settings.R.string.preference_group_title_application_behavior),
            ),
            SettingItem.ClickableSetting(
                id = "content_filtering",
                title = context.getString(co.anitrend.settings.R.string.preference_title_content_filtering),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_content_filtering),
                icon = Icons.Filled.FilterList,
                onClick = { /* TODO: Navigate to Content Filtering settings */ },
            ),
            SettingItem.ClickableSetting(
                id = "power_management",
                title = context.getString(co.anitrend.settings.R.string.preference_title_power_management),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_power_management),
                icon = Icons.Filled.Power,
                onClick = { /* TODO: Navigate to Power Management settings */ },
            ),
            // User Interface category
            SettingItem.CategoryHeader(
                id = "user_interface",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_user_interface),
            ),
            SettingItem.DialogSetting(
                id = "locale",
                title = context.getString(co.anitrend.settings.R.string.preference_title_locale),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_locale),
                icon = Icons.Filled.Translate,
                options = AniTrendLocale.entries.toList(),
                selectedOption = { settings.locale.value },
                onOptionSelected = { newLocale -> settings.locale.value = newLocale },
                displayText = { it.asLocaleString() },
            ),
            SettingItem.DialogSetting(
                id = "theme",
                title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_theme),
                icon = Icons.Filled.ColorLens,
                options = AniTrendTheme.entries.toList(),
                selectedOption = { settings.theme.value },
                onOptionSelected = { newTheme -> settings.theme.value = newTheme },
                displayText = { it.toString() },
            ),
            SettingItem.ClickableSetting(
                id = "presentation",
                title = context.getString(co.anitrend.settings.R.string.preference_title_presentation),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_presentation),
                icon = Icons.Filled.PhotoFilter,
                onClick = { /* TODO: Navigate to Presentation settings */ },
            ),
            // Notifications & Sync category
            SettingItem.CategoryHeader(
                id = "notifications_sync",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_notifications_and_sync_settings),
            ),
            SettingItem.ClickableSetting(
                id = "notifications",
                title = context.getString(co.anitrend.settings.R.string.preference_title_notifications),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_notifications),
                icon = Icons.Filled.Notifications,
                onClick = { /* TODO: Navigate to Notifications settings */ },
            ),
            SettingItem.ClickableSetting(
                id = "sync",
                title = context.getString(co.anitrend.settings.R.string.preference_title_sync),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_sync),
                icon = Icons.Filled.Sync,
                onClick = { /* TODO: Navigate to Synchronization settings */ },
            ),
            // Updates & Storage category
            SettingItem.CategoryHeader(
                id = "updates_storage",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_updates_and_storage),
            ),
            SettingItem.ClickableSetting(
                id = "updates",
                title = context.getString(co.anitrend.settings.R.string.preference_title_updates),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_updates),
                icon = Icons.Filled.SystemUpdate,
                onClick = { /* TODO: Navigate to Updates settings */ },
            ),
            SettingItem.ClickableSetting(
                id = "storage",
                title = context.getString(co.anitrend.settings.R.string.preference_title_storage),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_storage),
                icon = Icons.Filled.Storage,
                onClick = { /* TODO: Navigate to Storage settings */ },
            ),
        )
}
