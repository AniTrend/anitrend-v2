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
package co.anitrend.settings.component.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale
import co.anitrend.android.core.settings.helper.theme.model.AniTrendTheme
import co.anitrend.data.settings.feature.FeatureFlag
import co.anitrend.settings.R
import co.anitrend.settings.model.SettingItem

@Composable
internal fun previewData(): List<SettingItem> =
    listOf(
        // Appearance category
        SettingItem.HintCard(
            id = "hint_card",
            title = stringResource(R.string.title_settings_preview_about_app),
            description = stringResource(R.string.summary_settings_preview_about_app),
            icon = Icons.Default.BarChart,
            onClick = { },
        ),
        SettingItem.CategoryHeader(
            id = "appearance_category",
            title = stringResource(R.string.preference_category_title_user_interface),
        ),
        SettingItem.DialogSetting(
            id = "locale",
            title = stringResource(R.string.preference_title_locale_config),
            summary = stringResource(R.string.preference_summary_locale),
            icon = Icons.Default.Translate,
            options = AniTrendLocale.entries.toList(),
            selectedOption = { true },
            onOptionSelected = { },
            displayText = { locale -> locale.toString() },
            displayDescription = { locale -> locale.toString() },
        ),
        SettingItem.DialogSetting(
            id = "theme",
            title = stringResource(R.string.preference_title_theme),
            summary = stringResource(R.string.preference_summary_theme),
            icon = Icons.Default.ColorLens,
            options = AniTrendTheme.entries.toList(),
            selectedOption = { AniTrendTheme.SYSTEM },
            onOptionSelected = { },
            displayText = { theme -> theme.toString() },
            displayDescription = { theme -> theme.toString() },
        ),
        // Analytics & Reporting category
        SettingItem.CategoryHeader(
            id = "analytics_category",
            title = stringResource(R.string.title_settings_preview_analytics_reporting),
        ),
        SettingItem.SwitchSetting(
            id = "analytics",
            title = stringResource(R.string.preference_title_privacy_analytics_config),
            summary = stringResource(R.string.preference_summary_privacy_analytics_config),
            icon = Icons.Default.BarChart,
            onClick = { true },
            onValueChange = { },
        ),
        SettingItem.SwitchSetting(
            id = "crash_reporting",
            title = stringResource(R.string.preference_title_privacy_crash_analytics_config),
            summary = stringResource(R.string.preference_summary_privacy_crash_analytics_config),
            icon = Icons.Default.Report,
            onClick = { true },
            onValueChange = { },
        ),
        // Behavior category
        SettingItem.CategoryHeader(
            id = "behavior_category",
            title = stringResource(R.string.title_settings_preview_behavior),
        ),
        SettingItem.SwitchSetting(
            id = "clear_on_refresh",
            title = stringResource(R.string.preference_title_refresh_behavior_config),
            summary = stringResource(R.string.preference_summary_refresh_behavior_config),
            icon = Icons.Default.Refresh,
            onClick = { false },
            onValueChange = { },
        ),
        SettingItem.SwitchSetting(
            id = "auto_heap_dump",
            title = stringResource(R.string.preference_title_heap_dump),
            summary = stringResource(R.string.preference_summary_heap),
            icon = Icons.Default.Memory,
            onClick = { true },
            onValueChange = { },
        ),
        SettingItem.CategoryHeader(
            id = "privacy_category",
            title = stringResource(R.string.preference_title_privacy),
        ),
        SettingItem.ClickableSetting(
            id = "privacy",
            title = stringResource(R.string.preference_title_privacy),
            summary = stringResource(R.string.preference_summary_privacy),
            icon = Icons.Default.PrivacyTip,
            onClick = { },
        ),
        SettingItem.CategoryHeader(
            id = "advanced_category",
            title = stringResource(R.string.preference_category_title_advanced_settings),
        ),
        SettingItem.ClickableSetting(
            id = "developer_options",
            title = stringResource(R.string.preference_title_developer_options),
            summary = stringResource(R.string.preference_summary_developer_options),
            icon = Icons.Default.DeveloperBoard,
            onClick = { },
        ),
    )

@Composable
internal fun previewDeveloperData(): List<SettingItem> =
    listOf(
        SettingItem.CategoryHeader(
            id = "developer_settings_diagnostics",
            title = stringResource(R.string.preference_category_title_developer_diagnostics),
        ),
        SettingItem.ClickableSetting(
            id = "log_viewer",
            title = stringResource(R.string.preference_title_manage_logs),
            summary = stringResource(R.string.preference_summary_manage_logs),
            icon = Icons.Default.DeveloperBoard,
            onClick = { },
        ),
        SettingItem.ClickableSetting(
            id = "work_manager_tasks",
            title = stringResource(R.string.preference_title_work_manager_tasks),
            summary = stringResource(R.string.preference_summary_work_manager_tasks),
            icon = Icons.Default.WorkHistory,
            onClick = { },
        ),
        SettingItem.CategoryHeader(
            id = "developer_settings_runtime_behavior",
            title = stringResource(R.string.preference_category_title_developer_runtime_behavior),
        ),
        SettingItem.ClickableSetting(
            id = "feature_flags",
            title = stringResource(R.string.preference_title_feature_flags),
            summary = stringResource(R.string.preference_summary_feature_flags),
            icon = Icons.Default.Flag,
            onClick = { },
        ),
        SettingItem.SwitchSetting(
            id = "clear_on_refresh",
            title = stringResource(R.string.preference_title_refresh_behavior_config),
            summary = stringResource(R.string.preference_summary_refresh_behavior_config),
            icon = Icons.Default.Refresh,
            onClick = { false },
            onValueChange = { },
        ),
        SettingItem.SwitchSetting(
            id = "auto_heap_dump",
            title = stringResource(R.string.preference_title_heap_dump),
            summary = stringResource(R.string.preference_summary_heap),
            icon = Icons.Default.Memory,
            onClick = { true },
            onValueChange = { },
        ),
    )

@Composable
internal fun previewFeatureFlagData(enabledFlag: FeatureFlag? = null): List<SettingItem> =
    listOf(
        SettingItem.CategoryHeader(
            id = "feature_flags_user_interface",
            title = stringResource(R.string.preference_category_title_feature_flags_user_interface),
        ),
        SettingItem.SwitchSetting(
            id = FeatureFlag.EXPERIMENTAL_COMPOSE_UI.key,
            title = stringResource(R.string.preference_title_experimental_compose_ui),
            summary = stringResource(R.string.preference_summary_experimental_compose_ui),
            icon = Icons.Default.DeveloperBoard,
            onClick = { enabledFlag == FeatureFlag.EXPERIMENTAL_COMPOSE_UI },
            onValueChange = { },
        ),
    )
