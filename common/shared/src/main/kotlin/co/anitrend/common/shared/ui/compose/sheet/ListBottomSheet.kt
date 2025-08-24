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
package co.anitrend.common.shared.ui.compose.sheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

/**
 * Simple reusable bottom sheet wrapper that mirrors Material3 ModalBottomSheet behavior.
 *
 * Usage pattern: conditionally compose this when you want it visible, and remove it on dismiss.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val sheetShape =
        if (sheetState.currentValue == SheetValue.Expanded) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        } else {
            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = sheetShape,
        modifier = modifier,
    ) {
        content()
    }
}

@AniTrendPreview.Default
@Composable
private fun ListBottomSheetPreview() {
    PreviewTheme(wrapInSurface = true) {
        ListBottomSheet(onDismiss = {}) {
            // Preview content placeholder
        }
    }
}
