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
package co.anitrend.studio.component.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.studio.entity.StudioDetailData

@Composable
fun StudioScreenContent(
    state: StudioDetailData?,
    loadState: LoadState?,
    onBackPress: () -> Unit,
    onRetry: () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) {
        StudioDetailContent(
            state = state,
            loadState = loadState,
            onRetry = onRetry,
            modifier = Modifier.padding(it),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun StudioScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme {
        StudioDetailContent(
            state = null,
            loadState = LoadState.Loading(),
            onRetry = {},
        )
    }
}
