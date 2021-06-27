/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.medialist.editor.component.sheet.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.koinOf
import co.anitrend.domain.media.entity.Media
import co.anitrend.medialist.editor.component.sheet.viewmodel.state.MediaListEditorState
import coil.ImageLoader
import com.google.accompanist.coil.rememberCoilPainter


@Composable
fun BackgroundHeader(
    mediaState: State<Media?>,
    imageLoader: ImageLoader = koinOf()
) {
    Row(modifier = Modifier.aspectRatio(1.7f)) {
        Image(
            painter = rememberCoilPainter(
                mediaState.value?.image?.banner,
                imageLoader = imageLoader,
            ),
            contentDescription = null
        )
    }
}

@Composable
fun MediaListEditorComponent(state: MediaListEditorState) {
    AniTrendTheme {
        val scope = rememberCoroutineScope()
        val mediaState = state.model.observeAsState()
        BackgroundHeader(mediaState)
    }
}