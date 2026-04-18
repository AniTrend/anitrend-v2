/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.settings.component.content.developer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.component.compose.previewDeveloperData
import co.anitrend.settings.component.compose.SettingsItemsList
import co.anitrend.settings.model.SettingItem

@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier,
    settingsItems: List<SettingItem>,
) {
    SettingsItemsList(modifier = modifier, settingsItems = settingsItems)
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun DeveloperScreenPreview() {
    PreviewTheme(wrapInSurface = true) {
        DeveloperScreen(settingsItems = previewDeveloperData())
    }
}
