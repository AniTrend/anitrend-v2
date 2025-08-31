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
package co.anitrend.settings.component.content.storage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.component.builder.PreferenceBuilder
import co.anitrend.settings.component.compose.SettingsItemsList
import co.anitrend.settings.component.content.storage.presenter.StoragePresenter
import org.koin.compose.koinInject

@Composable
fun StorageScreen(modifier: Modifier = Modifier) {
    val presenter: StoragePresenter = koinInject()
    val items = presenter.getItems()
    SettingsItemsList(modifier = modifier, settingsItems = items)
}

@Composable
private fun StorageContent(modifier: Modifier = Modifier, presenter: StoragePresenter) {
    val items = presenter.getItems()
    SettingsItemsList(modifier = modifier, settingsItems = items)
}

@AniTrendPreview.Default
@Composable
private fun StorageScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    val context = LocalContext.current
    val presenter = StoragePresenter(
        context = context,
        settings = Settings(context),
        preferenceBuilder = PreferenceBuilder(),
    )
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        StorageContent(presenter = presenter)
    }
}
