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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BatterySaver
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.DeveloperBoard
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.LayersClear
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material.icons.outlined.WorkHistory
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.SettingsRouter
import co.anitrend.navigation.UpdaterRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.settings.BuildConfig
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

class SettingsPresenter(
    context: Context,
    settings: Settings,
    private val preferenceBuilder: IPreferenceBuilder,
) : CorePresenter(context, settings) {
    private fun generateGeneral(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "general_settings",
                    title = "General Settings",
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "accounts",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_accounts),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_accounts),
                        icon = Icons.Outlined.AccountCircle,
                        onClick = { navigateTo(SettingsRouter.Destination.ACCOUNTS) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "privacy_top",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_privacy),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_privacy),
                        icon = Icons.Outlined.PrivacyTip,
                        onClick = { navigateTo(SettingsRouter.Destination.PRIVACY) },
                    ),
                ),
        )
    }

    private fun generateApplication(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            SettingItem.CategoryHeader(
                id = "application_behavior",
                title = context.getString(co.anitrend.settings.R.string.preference_group_title_application_behavior),
            ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "content_filtering",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_content_filtering),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_content_filtering),
                        icon = Icons.Outlined.FilterList,
                        onClick = { navigateTo(SettingsRouter.Destination.FILTERS) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "power_management",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_power_management),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_power_management),
                        icon = Icons.Outlined.BatterySaver,
                        onClick = { navigateTo(SettingsRouter.Destination.POWER) },
                    ),
                ),
        )
    }

    private fun generateUserExperience(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "user_interface",
                    title = context.getString(co.anitrend.settings.R.string.preference_category_title_user_interface),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "locale",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_locale),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_locale),
                        icon = Icons.Outlined.Translate,
                        onClick = { navigateTo(SettingsRouter.Destination.LOCALE) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "theme",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_theme),
                        icon = Icons.Outlined.ColorLens,
                        onClick = { navigateTo(SettingsRouter.Destination.THEME) },
                    ),
                ),
        )
    }

    private fun generateNotificationAndSync(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "notifications_sync",
                    title = context.getString(co.anitrend.settings.R.string.preference_category_title_notifications_and_sync_settings),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "notifications",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_notifications),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_notifications),
                        icon = Icons.Outlined.Notifications,
                        onClick = { navigateTo(SettingsRouter.Destination.NOTIFICATION) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "sync",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_sync),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_sync),
                        icon = Icons.Outlined.Sync,
                        onClick = { navigateTo(SettingsRouter.Destination.SYNCHRONIZATION) },
                    ),
                ),
        )
    }

    private fun generateUpdatesAndStorage(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "updates_storage",
                    title = context.getString(co.anitrend.settings.R.string.preference_category_title_updates_and_storage),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "updates",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_updates),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_updates),
                        icon = Icons.Outlined.SystemUpdate,
                        onClick = { UpdaterRouter.startActivity(context) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "storage",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_storage),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_storage),
                        icon = Icons.Outlined.Storage,
                        onClick = { navigateTo(SettingsRouter.Destination.STORAGE) },
                    ),
                ),
        )
    }

    private fun generateDeveloperOptions(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "developer_settings",
                    title = context.getString(co.anitrend.settings.R.string.preference_group_title_developer_settings),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "log_viewer",
                        title = "Manage logs",
                        summary = "Audit application logs",
                        icon = Icons.Outlined.DeveloperBoard,
                        onClick = { navigateTo(SettingsRouter.Destination.LOGS) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "work_manager_tasks",
                        title = "Work Manager Tasks",
                        summary = "Manage application background tasks",
                        icon = Icons.Outlined.WorkHistory,
                        onClick = { navigateTo(SettingsRouter.Destination.TASK) },
                    ),
                    SettingItem.SwitchSetting(
                        id = "heap_dump",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_heap_dump),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_heap),
                        icon = Icons.Outlined.Memory,
                        onClick = { settings.automaticHeapDump.value },
                        onValueChange = { newValue -> settings.automaticHeapDump.value = newValue },
                    ),
                    SettingItem.SwitchSetting(
                        id = "clear_db_on_refresh",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_refresh_behavior_config),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_refresh_behavior_config),
                        icon = Icons.Outlined.LayersClear,
                        onClick = { settings.clearDataOnSwipeRefresh.value },
                        onValueChange = { newValue -> settings.clearDataOnSwipeRefresh.value = newValue },
                    ),
                ),
            isVisible = BuildConfig.DEBUG,
        )
    }

    fun getSettingsItems(navigateTo: (SettingsRouter.Destination) -> Unit): List<SettingItem> {
        preferenceBuilder.clear()
        preferenceBuilder.add(
            entries =
                listOf(
                    SettingItem.HintCard(
                        id = "hint_card",
                        title = "Heads up!",
                        description = "Between app version we may reset or apply new defaults. Tap to learn more",
                        icon = Icons.Outlined.Interests,
                        onClick = { AboutRouter.startActivity(context) },
                    ),
                ),
        )
        generateDeveloperOptions(navigateTo)
        generateGeneral(navigateTo)
        generateApplication(navigateTo)
        generateUserExperience(navigateTo)
        generateNotificationAndSync(navigateTo)
        generateUpdatesAndStorage(navigateTo)
        return preferenceBuilder.build()
    }
}
