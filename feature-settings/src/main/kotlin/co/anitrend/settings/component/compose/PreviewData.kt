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
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Translate
import co.anitrend.core.android.settings.helper.locale.model.AniTrendLocale
import co.anitrend.core.android.settings.helper.theme.model.AniTrendTheme
import co.anitrend.settings.model.SettingItem

internal val PreviewData =
    listOf(
        // Appearance category
        SettingItem.CategoryHeader(
            id = "appearance_category",
            title = "Appearance",
        ),
        SettingItem.DialogSetting(
            id = "locale",
            title = "Application Language",
            summary = "Change the application language",
            icon = Icons.Default.Translate,
            options = AniTrendLocale.entries.toList(),
            selectedOption = { true },
            onOptionSelected = { },
            displayText = { locale -> locale.toString() },
        ),
        SettingItem.DialogSetting(
            id = "theme",
            title = "Theme",
            summary = "Change the application theme",
            icon = Icons.Default.ColorLens,
            options = AniTrendTheme.entries.toList(),
            selectedOption = { AniTrendTheme.SYSTEM },
            onOptionSelected = { },
            displayText = { theme -> theme.toString() },
        ),
        // Analytics & Reporting category
        SettingItem.CategoryHeader(
            id = "analytics_category",
            title = "Analytics & Reporting",
        ),
        SettingItem.SwitchSetting(
            id = "analytics",
            title = "Usage Analytics",
            summary = "Allows usage statistics to be sent to the developer",
            icon = Icons.Default.BarChart,
            checked = { true },
            onValueChange = { },
        ),
        SettingItem.SwitchSetting(
            id = "crash_reporting",
            title = "Crash Reporting",
            summary = "Automatically report crashes",
            icon = Icons.Default.Report,
            checked = { true },
            onValueChange = { },
        ),
        // Behavior category
        SettingItem.CategoryHeader(
            id = "behavior_category",
            title = "Behavior",
        ),
        SettingItem.SwitchSetting(
            id = "clear_on_refresh",
            title = "Clear Database on Refresh",
            summary = "Typically the database is cleared when you pull to refresh",
            icon = Icons.Default.Refresh,
            checked = { false },
            onValueChange = { },
        ),
        SettingItem.SwitchSetting(
            id = "auto_heap_dump",
            title = "Auto Heap Dump",
            summary = "Dump heap automatically when leak is detected",
            icon = Icons.Default.Memory,
            checked = { true },
            onValueChange = { },
        ),
        SettingItem.CategoryHeader(
            id = "privacy_category",
            title = "Privacy",
        ),
        SettingItem.ClickableSetting(
            id = "privacy",
            title = "Privacy",
            summary = "Dial in the right level of privacy for you",
            icon = Icons.Default.PrivacyTip,
            onClick = { },
        ),
    )
