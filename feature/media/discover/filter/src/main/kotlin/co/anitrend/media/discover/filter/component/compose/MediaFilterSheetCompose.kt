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
package co.anitrend.media.discover.filter.component.compose

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.sheet.AniTrendSheet
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
private fun FilterSheetContent(
    modifier: Modifier = Modifier,
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    var discoverParam by remember { mutableStateOf(param) }
    val scrollState = rememberScrollState()
    MediaFilterScreen(
        modifier = modifier.verticalScroll(scrollState),
        dateHelper = dateHelper,
        param = discoverParam,
        onParamChange = {
            discoverParam = it
            onParamChange(it)
        },
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaFilterSheetScreen(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    AniTrendSheet(onDismiss = onDismiss) { _ ->
        FilterSheetContent(
            dateHelper = dateHelper,
            param = param,
            onParamChange = onParamChange,
            onDismiss = onDismiss,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaFilterSheetScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        FilterSheetContent(
            dateHelper = PreviewData.dateHelper,
            param = PreviewData.mediaDiscoverParam,
            onParamChange = {},
            onDismiss = {},
        )
    }
}
