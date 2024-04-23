package co.anitrend.common.media.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.common.media.ui.controller.model.MediaCompactItem
import co.anitrend.common.media.ui.databinding.MediaCompactItemBinding
import co.anitrend.core.android.compose.series_aspect_ration
import co.anitrend.core.android.compose.series_image_lg
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun MediaCompactItemContent(
    index: Int,
    media: Media,
    settings: Settings,
    modifier: Modifier = Modifier,
) {
    val mediaCompactItem = remember(media.hashCode()) { MediaCompactItem(media, settings) }
    AndroidView(
        modifier = modifier,
        factory = {
            MediaCompactItemBinding.inflate(it.getLayoutInflater()).root
        },
        update = {
            mediaCompactItem.bind(
                view = it,
                position = index,
                payloads = emptyList(),
                stateFlow = MutableStateFlow(ClickableItem.None),
                selectionMode = null
            )
        },
        onRelease = mediaCompactItem::unbind,
    )
}

@Composable
fun MediaCompactItemList(
    mediaItems: List<Media>,
    settings: Settings,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            count = mediaItems.size,
            key = { mediaItems[it].id },
            contentType = { mediaItems[it].category.type }
        ) { index ->
            MediaCompactItemContent(
                index = index,
                media = mediaItems[index],
                settings = settings,
                modifier = Modifier.width(138.dp)
                    .aspectRatio(0.5f),
            )
        }
    }
}
