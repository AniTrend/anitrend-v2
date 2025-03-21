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
package co.anitrend.medialist.editor.component.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import org.koin.compose.koinInject

@Composable
private fun BackgroundHeader(
    mediaCover: LiveData<IMediaCover>,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = koinInject(),
) {
    val mediaCoverState = mediaCover.observeAsState()
    Row(modifier = modifier.aspectRatio(1.7f)) {
        Image(
            painter =
                rememberAsyncImagePainter(
                    model = mediaCoverState.value?.banner,
                    imageLoader = imageLoader,
                ),
            contentDescription = null,
        )
    }
}

@Composable
private fun MediaListEditorSheetContent(media: Media) {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaListEditorSheetScreen(
    stream: LiveData<Media>,
    onDismiss: () -> Unit,
) {
    val state by stream.observeAsState()

    var showSheet by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val sheetShape =
        if (sheetState.currentValue == SheetValue.Expanded) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        } else {
            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                onDismiss()
            },
            sheetState = sheetState,
            shape = sheetShape,
        ) {
            state?.also { MediaListEditorSheetContent(it) }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaListEditorPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        MediaListEditorSheetContent(
            media = Media.Extended.empty(),
        )
    }
}
