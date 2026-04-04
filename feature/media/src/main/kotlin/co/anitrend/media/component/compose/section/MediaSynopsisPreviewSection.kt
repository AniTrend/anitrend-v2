/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.media.component.compose.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.domain.common.entity.contract.ISynopsis
import co.anitrend.common.media.ui.R as MediaUiR

@Composable
fun MediaSynopsisPreviewSection(
    synopsis: ISynopsis,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = 4,
) {
    val description =
        synopsis.description
            ?.toString()
            ?.trim()
            .orEmpty()
    val canCollapse =
        description.length > 280 ||
            description.count { it == '\n' } >= 3
    var isExpanded by rememberSaveable(description, collapsedMaxLines) {
        mutableStateOf(!canCollapse)
    }

    MediaHubSection(
        title = stringResource(MediaUiR.string.label_media_synopsis_section_title),
        subtitle = stringResource(co.anitrend.media.R.string.subtitle_media_synopsis_section),
        modifier = modifier,
    ) {
        if (description.isBlank()) {
            MediaHubSectionEmptyState(
                title = stringResource(co.anitrend.media.R.string.label_media_synopsis_empty_title),
                message = stringResource(co.anitrend.media.R.string.label_media_synopsis_empty_message),
            )
        } else {
            MarkdownText(
                synopsis = synopsis,
                maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines,
            )
            if (canCollapse) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                    ) {
                        Text(
                            text =
                                stringResource(
                                    if (isExpanded) {
                                        MediaUiR.string.action_media_synopsis_section_show_less
                                    } else {
                                        MediaUiR.string.action_media_synopsis_section_show_more
                                    },
                                ),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
