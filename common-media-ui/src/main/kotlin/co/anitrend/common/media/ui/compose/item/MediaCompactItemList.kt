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
package co.anitrend.common.media.ui.compose.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.model.common.IParam

@Composable
fun MediaCompactItemList(
    mediaItems: List<Media>,
    mediaPreferenceData: MediaPreferenceData,
    mediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items(
            count = mediaItems.size,
            key = { mediaItems[it].hashCode() },
            contentType = { mediaItems[it].category },
        ) { index ->
            MediaCompactItem(
                media = mediaItems[index],
                mediaPreferenceData = mediaPreferenceData,
                mediaItemClick = mediaItemClick,
                modifier =
                    Modifier
                        .height(285.dp)
                        .aspectRatio(.55f),
            )
        }
    }
}
