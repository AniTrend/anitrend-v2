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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.sheet.AniTrendSheet
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.media.R

private const val collapsedMaxLinesDefault = 4
private const val collapsedCharacterThreshold = 280
private const val collapsedLineBreakThreshold = 3

private fun String.shouldShowExpandedReader(): Boolean =
    length > collapsedCharacterThreshold ||
        count { it == '\n' } >= collapsedLineBreakThreshold

@Composable
fun MediaSupplementalInfoSection(
    extraInfo: String?,
    modifier: Modifier = Modifier,
    collapsedMaxLines: Int = collapsedMaxLinesDefault,
) {
    val content = extraInfo?.trim().orEmpty()

    if (content.isBlank()) {
        return
    }

    val canExpand = content.shouldShowExpandedReader()
    var showSheet by rememberSaveable(content, collapsedMaxLines) {
        mutableStateOf(false)
    }

    MediaHubSection(
        title = stringResource(R.string.label_media_supplemental_info_section_title),
        subtitle = stringResource(R.string.subtitle_media_supplemental_info_section),
        modifier = modifier,
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (canExpand) collapsedMaxLines else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis,
        )

        if (canExpand) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { showSheet = true },
                ) {
                    Text(
                        text = stringResource(R.string.action_media_supplemental_info_read_full),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }

    if (showSheet) {
        MediaSupplementalInfoSheet(
            extraInfo = content,
            onDismiss = { showSheet = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MediaSupplementalInfoSheet(
    extraInfo: String,
    onDismiss: () -> Unit,
) {
    val scrollState = rememberScrollState()

    AniTrendSheet(
        onDismiss = onDismiss,
        skipPartiallyExpanded = true,
    ) { _ ->
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MediaHubSectionHeader(
                title = stringResource(R.string.label_media_supplemental_info_section_title),
                subtitle = stringResource(R.string.subtitle_media_supplemental_info_section),
                trailingActionLabel = stringResource(R.string.action_media_supplemental_info_close),
                onTrailingAction = onDismiss,
            )
            Text(
                text = extraInfo,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun SupplementalInfoPreviewHost(
    extraInfo: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Status placeholder",
            style = MaterialTheme.typography.titleMedium,
        )
        MediaSupplementalInfoSection(extraInfo = extraInfo)
        Text(
            text = "Synopsis placeholder",
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaSupplementalInfoBlankPreview() {
    PreviewTheme(wrapInSurface = true) {
        SupplementalInfoPreviewHost(extraInfo = null)
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaSupplementalInfoShortPreview() {
    PreviewTheme(wrapInSurface = true) {
        SupplementalInfoPreviewHost(
            extraInfo =
                "Mangaka Kouhei Horikoshi has noted that American superhero comics inspired the series.",
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaSupplementalInfoLongPreview() {
    PreviewTheme(wrapInSurface = true) {
        SupplementalInfoPreviewHost(
            extraInfo =
                "Episode 492 is the second part of a two part special called Toriko x One Piece Collabo Special. " +
                    "The first part aired on Toriko's timeslot at 9:00 and the second part aired on One Piece's timeslot at 9:30. " +
                    "Episode 590 is the second part of another crossover special with Toriko and Dragon Ball Z.\n\n" +
                    "Source: AniDB. The airing time was Wednesdays 19:00 between October 20, 1999 and March 2001, " +
                    "then changed to Sundays 19:30 between April 2001 and December 2004.",
        )
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun MediaSupplementalInfoSheetPreview() {
    PreviewTheme(wrapInSurface = true) {
        MediaSupplementalInfoSheet(
            extraInfo =
                "Episode 492 is the second part of a two part special called Toriko x One Piece Collabo Special. " +
                    "The first part aired on Toriko's timeslot at 9:00 and the second part aired on One Piece's timeslot at 9:30.\n\n" +
                    "Episode 542 is the second part of another crossover special with Toriko.\n\n" +
                    "Source: AniDB. The airing time was Wednesdays 19:00 between October 20, 1999 and March 2001, " +
                    "then changed to Sundays 19:30 between April 2001 and December 2004.",
            onDismiss = {},
        )
    }
}
