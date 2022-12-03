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

package co.anitrend.common.genre.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.forActivity
import co.anitrend.navigation.extensions.startActivity
import com.google.android.material.chip.Chip

@Composable
private fun GenreItem(
    modifier: Modifier = Modifier,
    genre: Genre
) {
    AndroidView(
        factory = ::Chip,
        modifier = modifier,
    ) { chip ->
        chip.setOnClickListener {
            MediaDiscoverRouter.startActivity(
                view = it,
                navPayload = MediaDiscoverRouter.Param(
                    genre = genre.name
                ).asNavPayload()
            )
        }

        chip.text = genre.name
        chip.chipIcon = TextDrawable(
            chip.context,
            genre.emoji
        )
    }
}

@Composable
fun GenresListComponent(
    genres: List<Genre>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(all = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(genres.size) { index ->
            GenreItem(
                genre = genres[index]
            )
        }
    }
}
