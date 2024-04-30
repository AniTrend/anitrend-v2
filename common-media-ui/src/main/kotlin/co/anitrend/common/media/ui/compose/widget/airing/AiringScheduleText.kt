package co.anitrend.common.media.ui.compose.widget.airing

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.widget.airing.controller.MediaAiringScheduleController
import co.anitrend.core.android.asPrettyTime
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.domain.media.entity.Media


@Composable
@ReadOnlyComposable
private fun createDecoratedAiringText(
    controller: MediaAiringScheduleController,
    decoratorColor: Color,
): AnnotatedString.Builder {
    val schedule = controller.getSchedule()
    val builder = AnnotatedString.Builder()
    builder.withStyle(style = SpanStyle(color = decoratorColor)) {
        append(
            stringResource(
                R.string.label_episode_airing_in_time,
                schedule.episode,
                schedule.asPrettyTime()
            )
        )
    }
    return builder
}

@Composable
fun AiringScheduleText(
    media: Media,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val controller = MediaAiringScheduleController(media)
    if (controller.shouldHideWidget()) {
        return
    }

    val palette = media.image.rememberAccentColor()
    val decoratedText = createDecoratedAiringText(
        controller = controller,
        decoratorColor = palette
    )

    Text(
        text = decoratedText.toAnnotatedString(),
        overflow = TextOverflow.Ellipsis,
        style = style,
        maxLines = 1,
        modifier = modifier,
    )
}

@AniTrendPreview.Default
@Composable
private fun AiringScheduleTextPreview() {
    PreviewTheme(wrapInSurface = true) {
        AiringScheduleText(
            media = Media.Core.empty().copy(
                category = Media.Category.Anime(
                    episodes = 12,
                    duration = 24,
                    broadcast = null,
                    premiered = null,
                    schedule = null
                )
            ),
        )
    }
}
