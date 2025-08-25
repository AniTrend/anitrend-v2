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

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
) {
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
            modifier = Modifier.padding(all = 16.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
        MarkdownText(
            synopsis = synopsis,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        )
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
