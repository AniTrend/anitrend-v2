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
package co.anitrend.media.carousel.component.compose

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCarouselItem
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.carousel.component.viewmodel.state.CarouselState
import co.anitrend.navigation.model.common.IParam

@Composable
fun CarouselScreen(
    carouselState: CarouselState,
    mediaPreferenceData: MediaPreferenceData,
    carouselItemClick: (IParam) -> Unit,
) {
    val state = carouselState.model.observeAsState()
    val carouselItems: List<MediaCarousel> = state.value ?: return
    Surface {
        MediaCarouselItem(
            carouselItems = carouselItems,
            mediaPreferenceData = mediaPreferenceData,
            carouselItemClick = carouselItemClick,
        )
    }
}

@AniTrendPreview.Mobile
@Composable
private fun CarouselScreenPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaCarouselItem(
            carouselItems = emptyList(),
            mediaPreferenceData = MediaPreferenceData(scoreFormat = ScoreFormat.POINT_100),
            carouselItemClick = {},
        )
    }
}
