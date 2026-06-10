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
import androidx.compose.material.icons.outlined.Flag
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
import co.anitrend.android.core.helpers.notification.config.NotificationConfig
import co.anitrend.android.core.helpers.notification.hasNotificationPermissionFor
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.settings.common.theme.IThemeSettings
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asLocale
import co.anitrend.android.core.settings.helper.theme.model.AniTrendTheme
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.settings.cache.ICacheSettings
import co.anitrend.data.settings.feature.FeatureFlag
import co.anitrend.data.settings.feature.FeatureFlags
import co.anitrend.data.settings.notification.INotificationSettings
import co.anitrend.data.settings.power.IPowerSettings
import co.anitrend.data.settings.privacy.IPrivacySettings
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.navigation.AboutRouter
import co.anitrend.navigation.extensions.startNav3OrActivity
import co.anitrend.navigation.SettingsRouter
import co.anitrend.navigation.UpdaterRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.settings.BuildConfig
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem
import kotlin.math.max
import kotlin.math.roundToInt

class SettingsPresenter(
    context: Context,
    settings: Settings,
    private val preferenceBuilder: IPreferenceBuilder,
) : CorePresenter(context, settings) {
    data class DeveloperSettingsState(
        val automaticHeapDump: Boolean,
        val showLeakLauncher: Boolean,
        val clearDataOnSwipeRefresh: Boolean,
    )

    data class FeatureFlagSettingsState(
        val featureFlags: Set<String>,
    )

    private fun themeLabel(theme: AniTrendTheme): String =
        when (theme) {
            AniTrendTheme.SYSTEM -> context.getString(co.anitrend.android.core.R.string.global_label_system)
            AniTrendTheme.DYNAMIC -> context.getString(co.anitrend.settings.R.string.label_settings_theme_dynamic)
            AniTrendTheme.AMOLED -> context.getString(co.anitrend.settings.R.string.label_settings_theme_black)
            AniTrendTheme.LIGHT -> context.getString(co.anitrend.settings.R.string.label_settings_theme_light)
            AniTrendTheme.DARK -> context.getString(co.anitrend.settings.R.string.label_settings_theme_dark)
        }

    private fun labelForSeconds(seconds: Int): String {
        val minutes = seconds / 60
        return when {
            minutes < 60 -> context.getString(co.anitrend.settings.R.string.label_settings_sync_every_minutes, minutes)
            minutes % 60 == 0 -> {
                val hours = minutes / 60
                if (hours == 1) {
                    context.getString(co.anitrend.settings.R.string.label_settings_sync_every_hour, hours)
                } else {
                    context.getString(co.anitrend.settings.R.string.label_settings_sync_every_hours, hours)
                }
            }
            else -> {
                val hours = minutes / 60
                val rem = minutes % 60
                context.getString(co.anitrend.settings.R.string.label_settings_sync_every_hr_min, hours, rem)
            }
        }
    }

    private fun accountValue(): String {
        val authSettings = settings as IAuthenticationSettings
        return if (authSettings.isAuthenticated.value) {
            context.getString(co.anitrend.settings.R.string.label_settings_state_signed_in)
        } else {
            context.getString(co.anitrend.settings.R.string.label_settings_state_signed_out)
        }
    }

    private fun privacyValue(): String {
        val privacySettings = settings as IPrivacySettings
        val analytics = privacySettings.isAnalyticsEnabled.value
        val crashReporting = privacySettings.isCrashlyticsEnabled.value
        return when {
            analytics && crashReporting -> context.getString(co.anitrend.settings.R.string.label_settings_state_enabled)
            !analytics && !crashReporting -> context.getString(co.anitrend.settings.R.string.label_settings_state_minimal)
            else -> context.getString(co.anitrend.settings.R.string.label_settings_state_custom)
        }
    }

    private fun powerValue(): String {
        val powerSettings = settings as IPowerSettings
        return if (powerSettings.isPowerSaverOn.value) {
            context.getString(co.anitrend.settings.R.string.label_settings_state_power_saver_on)
        } else {
            context.getString(co.anitrend.settings.R.string.label_settings_state_default)
        }
    }

    private fun themeValue(): String {
        val themeSettings = settings as IThemeSettings
        return themeLabel(themeSettings.theme.value)
    }

    private fun localeValue(): String {
        val localeSettings = settings as ILocaleSettings
        return when (localeSettings.locale.value) {
            AniTrendLocale.AUTOMATIC -> context.getString(co.anitrend.android.core.R.string.global_label_system)
            else ->
                localeSettings.locale.value
                    .asLocale()
                    .getDisplayName(localeSettings.locale.value.asLocale())
        }
    }

    private fun notificationValue(): String {
        val notificationSettings = settings as INotificationSettings
        return when {
            !context.hasNotificationPermissionFor(NotificationConfig.GENERAL) ->
                context.getString(co.anitrend.settings.R.string.label_settings_state_permission_required)
            notificationSettings.isNotificationsEnabled.value ->
                context.getString(co.anitrend.settings.R.string.label_settings_state_enabled)
            else -> context.getString(co.anitrend.settings.R.string.label_settings_state_off)
        }
    }

    private fun syncValue(): String {
        val syncSettings = settings as ISyncSettings
        return labelForSeconds(max(syncSettings.listSyncInterval.value, ISyncSettings.MINIMUM_INTERVAL))
    }

    private fun storageValue(): String {
        val cacheSettings = settings as ICacheSettings
        return context.getString(
            co.anitrend.settings.R.string.label_settings_storage_percent,
            (cacheSettings.cacheUsageRatio.value * 100).roundToInt(),
        )
    }

    private fun generateGeneral(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "general_settings",
                    title = context.getString(co.anitrend.settings.R.string.preference_category_title_general_settings),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "accounts",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_accounts),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_accounts),
                        icon = Icons.Outlined.AccountCircle,
                        currentValue = ::accountValue,
                        onClick = { navigateTo(SettingsRouter.Destination.ACCOUNTS) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "privacy_top",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_privacy),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_privacy),
                        icon = Icons.Outlined.PrivacyTip,
                        currentValue = ::privacyValue,
                        onClick = { navigateTo(SettingsRouter.Destination.PRIVACY) },
                    ),
                ),
        )
    }

    private fun generateAdvanced(navigateTo: (SettingsRouter.Destination) -> Unit) {
        preferenceBuilder.add(
            category =
                SettingItem.CategoryHeader(
                    id = "advanced_settings",
                    title = context.getString(co.anitrend.settings.R.string.preference_category_title_advanced_settings),
                ),
            entries =
                listOf(
                    SettingItem.ClickableSetting(
                        id = "developer_options",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_developer_options),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_developer_options),
                        icon = Icons.Outlined.DeveloperBoard,
                        currentValue = { context.getString(co.anitrend.settings.R.string.label_settings_state_debug) },
                        onClick = { navigateTo(SettingsRouter.Destination.DEVELOPER) },
                    ),
                ),
            isVisible = BuildConfig.DEBUG,
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
                        currentValue = { context.getString(co.anitrend.settings.R.string.label_settings_state_preview) },
                        onClick = { navigateTo(SettingsRouter.Destination.FILTERS) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "power_management",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_power_management),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_power_management),
                        icon = Icons.Outlined.BatterySaver,
                        currentValue = ::powerValue,
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
                        currentValue = ::localeValue,
                        onClick = { navigateTo(SettingsRouter.Destination.LOCALE) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "theme",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_theme),
                        icon = Icons.Outlined.ColorLens,
                        currentValue = ::themeValue,
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
                        currentValue = ::notificationValue,
                        onClick = { navigateTo(SettingsRouter.Destination.NOTIFICATION) },
                    ),
                    SettingItem.ClickableSetting(
                        id = "sync",
                        title = context.getString(co.anitrend.settings.R.string.preference_title_sync),
                        summary = context.getString(co.anitrend.settings.R.string.preference_summary_sync),
                        icon = Icons.Outlined.Sync,
                        currentValue = ::syncValue,
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
                        currentValue = ::storageValue,
                        onClick = { navigateTo(SettingsRouter.Destination.STORAGE) },
                    ),
                ),
        )
    }

    fun getSettingsItems(navigateTo: (SettingsRouter.Destination) -> Unit): List<SettingItem> {
        preferenceBuilder.clear()
        preferenceBuilder.add(
            entries =
                listOf(
                    SettingItem.HintCard(
                        id = "hint_card",
                        title = context.getString(co.anitrend.settings.R.string.title_settings_root_hint),
                        description = context.getString(co.anitrend.settings.R.string.summary_settings_root_hint),
                        icon = Icons.Outlined.Interests,
                        actionLabel = context.getString(co.anitrend.settings.R.string.action_settings_learn_more),
                        onClick = { AboutRouter.startNav3OrActivity(context) },
                    ),
                ),
        )
        generateGeneral(navigateTo)
        generateApplication(navigateTo)
        generateUserExperience(navigateTo)
        generateNotificationAndSync(navigateTo)
        generateUpdatesAndStorage(navigateTo)
        generateAdvanced(navigateTo)
        return preferenceBuilder.build()
    }

    fun getDeveloperSettingsItems(
        state: DeveloperSettingsState,
        navigateTo: (SettingsRouter.Destination) -> Unit,
    ): List<SettingItem> {
        if (!BuildConfig.DEBUG) {
            return listOf(
                SettingItem.HintCard(
                    id = "developer_settings_unavailable",
                    title = context.getString(co.anitrend.settings.R.string.title_settings_developer_unavailable),
                    description = context.getString(co.anitrend.settings.R.string.summary_settings_developer_unavailable),
                    icon = Icons.Outlined.Interests,
                    onClick = {},
                ),
            )
        }

        return listOf(
            SettingItem.CategoryHeader(
                id = "developer_settings_diagnostics",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_developer_diagnostics),
            ),
            SettingItem.ClickableSetting(
                id = "log_viewer",
                title = context.getString(co.anitrend.settings.R.string.preference_title_manage_logs),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_manage_logs),
                icon = Icons.Outlined.DeveloperBoard,
                currentValue = { context.getString(co.anitrend.settings.R.string.label_settings_state_live) },
                onClick = { navigateTo(SettingsRouter.Destination.LOGS) },
            ),
            SettingItem.ClickableSetting(
                id = "work_manager_tasks",
                title = context.getString(co.anitrend.settings.R.string.preference_title_work_manager_tasks),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_work_manager_tasks),
                icon = Icons.Outlined.WorkHistory,
                currentValue = { context.getString(co.anitrend.settings.R.string.label_settings_state_runtime) },
                onClick = { navigateTo(SettingsRouter.Destination.TASK) },
            ),
            SettingItem.CategoryHeader(
                id = "developer_settings_runtime_behavior",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_developer_runtime_behavior),
            ),
            SettingItem.ClickableSetting(
                id = "feature_flags",
                title = context.getString(co.anitrend.settings.R.string.preference_title_feature_flags),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_feature_flags),
                icon = Icons.Outlined.Flag,
                currentValue = { context.getString(co.anitrend.settings.R.string.label_settings_state_debug) },
                onClick = { navigateTo(SettingsRouter.Destination.FEATURE_FLAGS) },
            ),
            SettingItem.SwitchSetting(
                id = "heap_dump",
                title = context.getString(co.anitrend.settings.R.string.preference_title_heap_dump),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_heap),
                icon = Icons.Outlined.Memory,
                onClick = { state.automaticHeapDump },
                onValueChange = { newValue -> settings.automaticHeapDump.value = newValue },
            ),
            SettingItem.SwitchSetting(
                id = "show_leak_launcher",
                title = context.getString(co.anitrend.settings.R.string.preference_title_show_leak_launcher),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_show_leak_launcher),
                icon = Icons.Outlined.DeveloperBoard,
                onClick = { state.showLeakLauncher },
                onValueChange = { newValue -> settings.showLeakLauncher.value = newValue },
            ),
            SettingItem.SwitchSetting(
                id = "clear_db_on_refresh",
                title = context.getString(co.anitrend.settings.R.string.preference_title_refresh_behavior_config),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_refresh_behavior_config),
                icon = Icons.Outlined.LayersClear,
                onClick = { state.clearDataOnSwipeRefresh },
                onValueChange = { newValue -> settings.clearDataOnSwipeRefresh.value = newValue },
            ),
        )
    }

    fun getFeatureFlagSettingsItems(state: FeatureFlagSettingsState): List<SettingItem> =
        listOf(
            SettingItem.CategoryHeader(
                id = "feature_flags_user_interface",
                title = context.getString(co.anitrend.settings.R.string.preference_category_title_feature_flags_user_interface),
            ),
            SettingItem.SwitchSetting(
                id = FeatureFlag.EXPERIMENTAL_COMPOSE_UI.key,
                title = context.getString(co.anitrend.settings.R.string.preference_title_experimental_compose_ui),
                summary = context.getString(co.anitrend.settings.R.string.preference_summary_experimental_compose_ui),
                icon = Icons.Outlined.DeveloperBoard,
                onClick = {
                    FeatureFlags.isEnabled(
                        flags = state.featureFlags,
                        flag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI,
                    )
                },
                onValueChange = { newValue ->
                    settings.featureFlags.value =
                        FeatureFlags.setEnabled(
                            flags = settings.featureFlags.value,
                            flag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI,
                            enabled = newValue,
                        )
                },
            ),
        )
}
