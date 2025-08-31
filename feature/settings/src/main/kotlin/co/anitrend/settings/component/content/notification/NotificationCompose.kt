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

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.component.compose.SettingsItemsList
import co.anitrend.settings.component.content.notification.presenter.NotificationPresenter
import org.koin.compose.koinInject

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    presenter: NotificationPresenter = koinInject(),
) {
    NotificationContent(modifier = modifier, presenter = presenter)
}

@Composable
private fun NotificationContent(
    modifier: Modifier = Modifier,
    presenter: NotificationPresenter,
) {
    val items = presenter.getItems()
    SettingsItemsList(modifier = modifier, settingsItems = items)
}

@AniTrendPreview.Default
@Composable
private fun NotificationScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        // Local preview without DI
        val presenter =
            NotificationPresenter(
                context = androidx.compose.ui.platform.LocalContext.current,
                settings =
                    co.anitrend.android.core.settings
                        .Settings(androidx.compose.ui.platform.LocalContext.current),
                preferenceBuilder =
                    co.anitrend.settings.component.builder
                        .PreferenceBuilder(),
            )
        NotificationContent(presenter = presenter)
    }
}
