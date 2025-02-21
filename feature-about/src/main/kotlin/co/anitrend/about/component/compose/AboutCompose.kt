/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.about.component.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.about.component.compose.state.WorkItem
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import kotlinx.coroutines.flow.Flow

@Composable
private fun AboutContent(
    workItems: List<WorkItem>,
    onCancelWork: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    WorkManagerStatusScreen(
        modifier = modifier,
        workItems = workItems,
        onCancelWork = onCancelWork,
    )
}

@Composable
fun AboutScreenContent(
    workItemFlow: Flow<List<WorkItem>>,
    onCancelWork: (String) -> Unit,
    onBackPress: () -> Unit,
) {
    val workItems by workItemFlow.collectAsState(emptyList())
    DefaultScaffold(onBackPress) { modifier ->
        AboutContent(
            modifier = modifier.fillMaxSize(),
            workItems = workItems,
            onCancelWork = onCancelWork,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun AboutContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        AboutContent(
            workItems = emptyList(),
            onCancelWork = {}
        )
    }
}
