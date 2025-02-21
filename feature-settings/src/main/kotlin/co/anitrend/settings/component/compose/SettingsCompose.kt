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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.settings.model.SettingItem

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(count = settingsItems.size) { index ->
            when (val item = settingsItems[index]) {
                is SettingItem.CategoryHeader -> PreferenceCategory(item = item)
                is SettingItem.SwitchSetting -> PreferenceSwitch(item = item)
                is SettingItem.ClickableSetting -> PreferenceItem(item = item)
                is SettingItem.DialogSetting<*> -> PreferenceDialog(item = item)
            }
        }
    }
}

@Composable
fun SettingsContentScreen(
    settingsItems: List<SettingItem>,
    onBackPress: () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        SettingsContent(
            modifier = Modifier.padding(padding),
            settingsItems = settingsItems,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        SettingsContent(settingsItems = PreviewData)
    }
}
