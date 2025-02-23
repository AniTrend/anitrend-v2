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
package co.anitrend.common.media.ui.compose.widget.releasing

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.entity.contract.IMediaCover
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaStatus

@Composable
private fun AiringSchedule(
    media: Media,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = ::MediaAiringScheduleWidget,
        update = { it.setUpAiringSchedule(media) },
        onRelease = MediaAiringScheduleWidget::onViewRecycled,
        modifier = modifier,
    )
}

@Composable
private fun MangaQuantity(
    category: Media.Category.Manga,
    status: MediaStatus?,
    image: IMediaCover,
    modifier: Modifier = Modifier,
) {
    val chapters =
        pluralStringResource(
            R.plurals.label_number_of_chapters,
            category.chapters,
        ).format(category.chapters)

    val volumes =
        pluralStringResource(
            R.plurals.label_number_of_volumes,
            category.volumes,
        ).format(category.volumes)

    val textTemplate =
        if (category.volumes > 0) {
            "$volumes $CHARACTER_SEPARATOR $chapters"
        } else {
            "${status?.alias} $CHARACTER_SEPARATOR $chapters"
        }

    Text(
        text = textTemplate,
        fontWeight = FontWeight.Bold,
        color = image.rememberAccentColor(),
        style = MaterialTheme.typography.bodySmall,
        modifier = modifier.padding(4.dp),
    )
}

@Composable
fun MediaReleaseStatus(
    media: Media,
    modifier: Modifier = Modifier,
) {
    when (val category = media.category) {
        is Media.Category.Anime ->
            AiringSchedule(
                media = media,
                modifier = modifier,
            )
        is Media.Category.Manga ->
            MangaQuantity(
                category = category,
                status = media.status,
                image = media.image,
                modifier = modifier,
            )
    }
}
