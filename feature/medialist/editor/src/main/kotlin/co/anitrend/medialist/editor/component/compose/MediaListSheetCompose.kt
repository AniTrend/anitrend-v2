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
package co.anitrend.medialist.editor.component.compose

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import co.anitrend.android.core.compose.design.sheet.AniTrendSheet
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.component.compose.state.MediaListEditorState
import co.anitrend.medialist.editor.component.compose.state.rememberMediaListEditorState
import co.anitrend.medialist.editor.component.viewmodel.MediaListEditorViewModel
import co.anitrend.navigation.MediaListTaskRouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListEditorSheetScreen(
    viewModel: MediaListEditorViewModel,
    dateHelper: AniTrendDateHelper,
    scoreFormat: ScoreFormat,
    onDismiss: () -> Unit,
    onSave: (MediaListTaskRouter.Param.SaveEntry) -> Unit,
    onDelete: (MediaListTaskRouter.Param.DeleteEntry) -> Unit,
) {
    val media by viewModel.model.observeAsState()
    media ?: return
    val editorState =
        rememberMediaListEditorState(
            media = requireNotNull(media),
            scoreFormat = scoreFormat,
            dateHelper = dateHelper,
        )

    AniTrendSheet(onDismiss = onDismiss) {
        MediaListEditorContent(
            state = editorState,
            onAction = {
                when (it) {
                    OnMediaListEditorAction.SAVE -> {
                        val params = editorState.createSaveEntryParams()
                        onSave(params)
                    }

                    OnMediaListEditorAction.DELETE -> {
                        val params = editorState.createDeleteEntryParams()
                        params?.also(onDelete)
                    }
                }
                onDismiss()
            },
        )
    }
}

@Composable
private fun MediaListEditorContent(
    modifier: Modifier = Modifier,
    state: MediaListEditorState,
    onAction: (OnMediaListEditorAction) -> Unit,
) {
    val scrollState = rememberScrollState()
    MediaListEditorScreen(
        modifier = modifier.verticalScroll(scrollState),
        state = state,
        onAction = onAction,
    )
}
