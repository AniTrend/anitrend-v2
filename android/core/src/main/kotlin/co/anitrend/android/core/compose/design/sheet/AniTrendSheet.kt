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
package co.anitrend.android.core.compose.design.sheet

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniTrendSheet(
    modifier: Modifier = Modifier,
    showSheetWhenOpen: Boolean = true,
    skipPartiallyExpanded: Boolean = false,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    onDismiss: () -> Unit,
    content: @Composable (ColumnScope.(SheetState) -> Unit),
) {
    var showSheet by remember { mutableStateOf(showSheetWhenOpen) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    val topCornerRadius by animateDpAsState(
        targetValue =
            when (sheetState.currentValue) {
                SheetValue.Expanded -> 4.dp
                else -> 16.dp
            },
        label = "SheetCornerRadiusAnimation",
    )
    val sheetShape =
        RoundedCornerShape(
            topStart = topCornerRadius,
            topEnd = topCornerRadius,
        )

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                onDismiss()
            },
            modifier = modifier,
            dragHandle = dragHandle,
            sheetState = sheetState,
            shape = sheetShape,
            content = { content(sheetState) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@AniTrendPreview.Default
@Composable
fun AniTrendSheetPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        AniTrendSheet(
            showSheetWhenOpen = true,
            skipPartiallyExpanded = true,
            onDismiss = {},
            content = { _ ->
                Column {
                    Text("Sheet Content")
                }
            },
        )
    }
}
