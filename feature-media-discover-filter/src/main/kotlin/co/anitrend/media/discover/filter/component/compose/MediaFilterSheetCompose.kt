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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.arch.extension.util.date.contract.AbstractSupportDateHelper
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
private fun FilterSheetContent(
    modifier: Modifier = Modifier,
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    val nestedSrollInterop = rememberNestedScrollInteropConnection()
    // Use a Box that fills the height; nestedScroll enables nested scrolling behavior.
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .nestedScroll(nestedSrollInterop),
    ) {
        MediaFilterScreen(
            dateHelper = dateHelper,
            param = param,
            onParamChange = onParamChange,
            onDismiss = onDismiss,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaFilterSheetScreen(
    dateHelper: AbstractSupportDateHelper,
    param: MediaDiscoverRouter.MediaDiscoverParam,
    onParamChange: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onDismiss: () -> Unit,
) {
    // State to control sheet visibility
    var showSheet by remember { mutableStateOf(true) }
    // Create a remembered sheet state (configure skipPartiallyExpanded as needed)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Compute a dynamic shape based on whether the sheet is expanded.
    // (This sample assumes sheetState.currentValue is accessible and returns a SheetValue)
    val sheetShape =
        if (sheetState.currentValue == SheetValue.Expanded) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        } else {
            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        }

    // Show the ModalBottomSheet when needed
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                onDismiss()
            },
            sheetState = sheetState,
            shape = sheetShape,
        ) {
            FilterSheetContent(
                dateHelper = dateHelper,
                param = param,
                onParamChange = onParamChange,
                onDismiss = onDismiss,
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
fun MediaFilterSheetScreenPreview(
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
