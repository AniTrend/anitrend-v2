package co.anitrend.common.media.ui.compose.widget.title

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor

import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.PreviewTheme
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaFormat.Companion.isQuantitative

@Composable
@ReadOnlyComposable
private fun Media.Category.BuildTextUsing(
    builder: AnnotatedString.Builder
) {
    val unknown = stringResource(co.anitrend.core.R.string.label_place_holder_to_be_announced)

    when(this) {
        is Media.Category.Anime -> {
            when (episodes) {
                0 -> builder.withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(AnnotatedString(unknown))
                }
                else -> builder.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        pluralStringResource(
                            R.plurals.label_number_of_episodes,
                            episodes,
                            episodes
                        )
                    )
                }
            }
        }
        is Media.Category.Manga -> {
            when (chapters) {
                0 -> builder.withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                    append(unknown)
                }
                else -> builder.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(
                        pluralStringResource(
                            R.plurals.label_number_of_chapters,
                            chapters,
                            chapters
                        )
                    )
                }
            }
        }
    }
}

@Composable
@ReadOnlyComposable
private fun Media.annotatedSubTitle(color: Color): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val unknown = stringResource(
        co.anitrend.core.R.string.label_place_holder_to_be_announced
    )
    builder.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        withStyle(style = SpanStyle(color = color)) {
            if (startDate.isDateNotSet()) append(unknown)
            else append("${startDate.year}")
        }
    }
    builder.append(" $CHARACTER_SEPARATOR ")

    val isQuantitative = format.isQuantitative()

    if (format != null) {
        builder.withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(requireNotNull(format?.alias))
        }
        if (isQuantitative)
            builder.append(" $CHARACTER_SEPARATOR ")
    }

    if (isQuantitative)
        category.BuildTextUsing(builder)

    return builder.toAnnotatedString()
}

/**
 * Displays subtitle text in the following format for anime and manga respectively
 * > **2018** • TV • 25 Episodes
 *
 * > **2018** • Novel • 48 Chapters
 */
@Composable
fun MediaSubTitleText(
    media: Media,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    val color = media.image.rememberAccentColor()
    val styledSubTitle = media.annotatedSubTitle(color)

    Text(
        text = styledSubTitle,
        overflow = TextOverflow.Ellipsis,
        style = style,
        maxLines = 1,
        modifier = modifier
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaSubTitleTextPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaSubTitleText(
            media = Media.Core.empty().copy(
                image = MediaImage.empty().copy(color = "#e4a15d"),
                startDate = FuzzyDate.empty().copy(2018),
                format = MediaFormat.TV,
                category = Media.Category.Anime.empty().copy(25)
            )
        )
    }
}
