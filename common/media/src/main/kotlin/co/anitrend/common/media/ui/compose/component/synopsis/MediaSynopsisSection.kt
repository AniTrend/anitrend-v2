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
package co.anitrend.common.media.ui.compose.component.synopsis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.common.media.ui.R
import co.anitrend.domain.common.entity.contract.ISynopsis

@Composable
fun MediaSynopsisSection(
    synopsis: ISynopsis,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int? = null,
    initiallyExpanded: Boolean = false,
) {
    val description =
        synopsis.description
            ?.toString()
            ?.trim()
            .orEmpty()
    val canCollapse =
        collapsedMaxLines != null &&
            (
                description.length > 280 ||
                    description.count { it == '\n' } >= 3
            )
    var isExpanded by rememberSaveable(description, collapsedMaxLines) {
        mutableStateOf(!canCollapse || initiallyExpanded)
    }

    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.label_media_synopsis_section_title),
            modifier = Modifier.padding(all = 14.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
        MarkdownText(
            synopsis = synopsis,
            maxLines = if (isExpanded) Int.MAX_VALUE else requireNotNull(collapsedMaxLines),
            modifier =
                Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = if (canCollapse) 0.dp else 20.dp,
                ),
        )
        if (canCollapse) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                ) {
                    Text(
                        text =
                            stringResource(
                                if (isExpanded) {
                                    R.string.action_media_synopsis_section_show_less
                                } else {
                                    R.string.action_media_synopsis_section_show_more
                                },
                            ),
                    )
                }
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaSynopsisSectionPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaSynopsisSection(
            synopsis =
                object : ISynopsis {
                    override val description: CharSequence? =
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                            "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                            "exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                },
            modifier = Modifier.padding(16.dp),
        )
    }
}
