package co.anitrend.common.media.ui.compose.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.controller.compose.CarouselController
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.model.common.IParam

@Composable
private fun HeaderTitle(
    data: CarouselController.Data.Header,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = data.title,
            fontWeight = FontWeight.Bold,
            style = AniTrendTheme.typography.body1,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = data.description,
            fontWeight = FontWeight.Bold,
            style = AniTrendTheme.typography.caption,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
private fun CarouselHeader(
    mediaType: MediaType,
    carouselType: MediaCarousel.CarouselType,
    mediaItem: Media?,
    headerSeeMoreClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val carouselData = CarouselController(
        mediaType = mediaType,
        carouselType = carouselType,
        mediaItem = mediaItem,
    ).createCarouselData()

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderTitle(
            data = carouselData.header,
            modifier = Modifier.weight(1f),
        )
        TextButton(
            onClick = { headerSeeMoreClick(carouselData.param) },
        ) {
            Text(text = stringResource(co.anitrend.common.media.ui.R.string.label_carousel_see_more), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun CarouselItems(
    mediaItems: List<Media>,
    mediaPreferenceData: MediaPreferenceData,
    modifier: Modifier = Modifier,
    mediaItemClick: (IParam) -> Unit,
) {
    MediaCompactItemList(
        mediaItems = mediaItems,
        mediaPreferenceData = mediaPreferenceData,
        mediaItemClick = mediaItemClick,
        modifier = modifier,
    )
}

@Composable
fun MediaCarouselItem(
    carouselItems: List<MediaCarousel>,
    mediaPreferenceData: MediaPreferenceData,
    carouselItemClick: (IParam) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        items(
            count = carouselItems.size,
            key = { carouselItems[it].id },
            contentType = { carouselItems[it].carouselType },
        ) { index ->
            val carouselItem = carouselItems[index]
            CarouselHeader(
                mediaType = carouselItem.mediaType,
                carouselType = carouselItem.carouselType,
                mediaItem = carouselItem.mediaItems.firstOrNull(),
                headerSeeMoreClick = carouselItemClick,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            CarouselItems(
                mediaItems = carouselItem.mediaItems,
                mediaPreferenceData = mediaPreferenceData,
                mediaItemClick = carouselItemClick,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaCarouselItemPreview() {
    PreviewTheme(wrapInSurface = true) {
        CarouselHeader(
            mediaType = MediaType.ANIME,
            carouselType = MediaCarousel.CarouselType.AIRING_SOON,
            mediaItem = Media.Core.empty(),
            headerSeeMoreClick = {}
        )
    }
}
