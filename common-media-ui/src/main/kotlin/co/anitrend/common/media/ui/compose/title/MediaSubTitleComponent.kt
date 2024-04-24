package co.anitrend.common.media.ui.compose.title

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.common.media.ui.widget.title.MediaSubTitleWidget
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media

@Composable
fun MediaSubTitle(
    media: Media,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = ::MediaSubTitleWidget,
        update = { it.setUpSubTitle(media) },
        onRelease = MediaSubTitleWidget::onViewRecycled,
        modifier = modifier
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaSubTitlePreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaSubTitle(
            media = Media.Extended.empty()
        )
    }
}
