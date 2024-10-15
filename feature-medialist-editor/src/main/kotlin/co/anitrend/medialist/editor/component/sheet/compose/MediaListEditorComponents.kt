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
package co.anitrend.medialist.editor.component.sheet.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import co.anitrend.core.android.ui.theme.AniTrendTheme3
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import co.anitrend.medialist.editor.component.sheet.viewmodel.state.MediaListEditorState
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.get
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
fun MediaListEditorComponent(state: MediaListEditorState) {
    AniTrendTheme3 {
        Surface {
            BackgroundHeader(state.model.map(Media::image))
        }
    }
}
