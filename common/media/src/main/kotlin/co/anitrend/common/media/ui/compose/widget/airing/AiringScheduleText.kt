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
package co.anitrend.common.media.ui.compose.widget.airing

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import co.anitrend.android.core.asPrettyTime
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.compose.extensions.rememberAccentColor
import co.anitrend.common.media.ui.widget.airing.controller.MediaAiringScheduleController
import co.anitrend.domain.media.entity.Media

@Composable
@ReadOnlyComposable
private fun createDecoratedAiringText(
    controller: MediaAiringScheduleController,
    decoratorColor: Color,
    format: AiringScheduleTextFormat,
): AnnotatedString.Builder {
    val schedule = controller.getSchedule()
    val builder = AnnotatedString.Builder()
    if (format == AiringScheduleTextFormat.WITHOUT_PREFIX) {
        builder.append(schedule.asPrettyTime())
    } else {
        builder.withStyle(style = SpanStyle(color = decoratorColor)) {
            append(
                stringResource(
                    R.string.label_episode_airing_in_time,
                    schedule.episode,
                    schedule.asPrettyTime(),
                ),
            )
        }
    }
    return builder
}

enum class AiringScheduleTextFormat {
    WITH_PREFIX,
    WITHOUT_PREFIX,
}

@Composable
fun AiringScheduleText(
    media: Media,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    format: AiringScheduleTextFormat = AiringScheduleTextFormat.WITH_PREFIX,
) {
    val controller = remember(media) { MediaAiringScheduleController(media) }
    if (controller.shouldHideWidget()) {
        return
    }

    val palette = media.image.rememberAccentColor()
    val decoratedText =
        createDecoratedAiringText(
            controller = controller,
            decoratorColor = palette,
            format = format,
        )

    Text(
        text = decoratedText.toAnnotatedString(),
        overflow = TextOverflow.Ellipsis,
        style = style,
        maxLines = 1,
        modifier = modifier,
    )
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun AiringScheduleTextPreview() {
    PreviewTheme(wrapInSurface = true) {
        AiringScheduleText(
            media =
                Media.Core.empty().copy(
                    category =
                        Media.Category.Anime(
                            episodes = 12,
                            duration = 24,
                            broadcast = null,
                            premiered = null,
                            schedule = null,
                        ),
                ),
        )
    }
}
