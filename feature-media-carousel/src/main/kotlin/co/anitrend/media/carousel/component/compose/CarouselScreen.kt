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
