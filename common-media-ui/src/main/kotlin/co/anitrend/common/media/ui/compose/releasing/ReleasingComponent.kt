package co.anitrend.common.media.ui.compose.releasing

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.widget.airing.MediaAiringScheduleWidget
import co.anitrend.core.android.compose.AniTrendTheme
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaStatus

@Composable
private fun AiringSchedule(
    media: Media,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = ::MediaAiringScheduleWidget,
        update = { it.setUpAiringSchedule(media) },
        onRelease = MediaAiringScheduleWidget::onViewRecycled,
        modifier = modifier
    )
}

@Composable
private fun MangaQuantity(
    category: Media.Category.Manga,
    status: MediaStatus?,
    image: IMediaCover,
    modifier: Modifier = Modifier
) {
    val chapters = pluralStringResource(
        R.plurals.label_number_of_chapters,
        category.chapters
    ).format(category.chapters)

    val volumes = pluralStringResource(
        R.plurals.label_number_of_volumes,
        category.volumes
    ).format(category.volumes)

    val textTemplate = if (category.volumes > 0)
        "$volumes $CHARACTER_SEPARATOR $chapters"
    else
        "${status?.alias.toString()} $CHARACTER_SEPARATOR $chapters"

    Text(
        text = textTemplate,
        fontWeight = FontWeight.Bold,
        color = image.rememberAccentColor(),
        style = AniTrendTheme.typography.caption,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
fun MediaReleaseStatus(
    media: Media,
    modifier: Modifier = Modifier,
) {
    when (val category = media.category) {
        is Media.Category.Anime -> AiringSchedule(
            media = media,
            modifier = modifier,
        )
        is Media.Category.Manga -> MangaQuantity(
            category = category,
            status = media.status,
            image = media.image,
            modifier = modifier,
        )
    }
}
