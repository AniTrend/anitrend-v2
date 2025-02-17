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
package co.anitrend.deeplink.component.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@Composable
private fun DeepLinkContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.then(Modifier.padding(16.dp))) {
        // Show logo?
    }
}

@Composable
fun DeepLinkScreenContent(onBackPress: () -> Unit) {
    DefaultScaffold(onBackPress) { modifier ->
        DeepLinkContent(
            modifier =
                modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        )
    }
}

@Composable
@AniTrendPreview.Mobile
@AniTrendPreview.Light
@AniTrendPreview.Dark
private fun DeepLinkScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme) {
        DeepLinkContent()
    }
}
