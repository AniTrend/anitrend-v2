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
package co.anitrend.episode.component.content.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.koin.MarkdownFlavour
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.episode.component.sheet.viewmodel.EpisodeSheetViewModel

@Composable
private fun EpisodeSheetContent(
    modifier: Modifier = Modifier,
    episode: Episode,
    onPlayClick: (String) -> Unit = {},
    onPublisherClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Column(
        modifier =
            modifier
                .verticalScroll(scrollState)
                .padding(bottom = 32.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.85f),
        ) {
            AniTrendImage(
                image = episode.thumbnail,
                imageType = RequestImage.Media.ImageType.BANNER,
                modifier = Modifier.fillMaxSize(),
                onClick = { onPlayClick(episode.guid) },
            )

            // Duration badge
            Row(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(16.dp),
                        ).padding(8.dp)
                        .align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = episode.about.episodeDuration,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Play button
            Icon(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
                modifier =
                    Modifier
                        .size(42.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape,
                        ).size(84.dp)
                        .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = episode.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.size(8.dp))

        // Publisher chip
        AssistChip(
            onClick = onPublisherClick,
            label = {
                Text(text = episode.series.seriesPublisher ?: "")
            },
            modifier =
                Modifier
                    .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.size(8.dp))
        // Episode description
        MarkdownText(
            content =
                if (episode.description.isNullOrBlank()) {
                    episode.about.episodeTitle?.let {
                        stringResource(
                            co.anitrend.episode.R.string.label_episode_has_no_summary,
                            it,
                        )
                    }
                } else {
                    episode.description
                },
            flavour = MarkdownFlavour.STANDARD,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.size(8.dp))

        // Download button

        Button(
            onClick = onDownloadClick,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.End),
        ) {
            Text(text = stringResource(id = co.anitrend.episode.R.string.label_download))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeSheetScreen(
    viewModel: EpisodeSheetViewModel,
    onPlayClick: (String) -> Unit = {},
    onPublisherClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    onDismiss: () -> Unit,
) {
    var showSheet by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    val sheetShape =
        if (sheetState.currentValue == SheetValue.Expanded) {
            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        } else {
            RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
        }

    if (showSheet) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = {
                showSheet = false
                onDismiss()
            },
            sheetState = sheetState,
            shape = sheetShape,
        ) {
            val model by viewModel.model.observeAsState()
            when (val episode = model) {
                null ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .size(24.dp)
                                    .padding(16.dp)
                                    .align(alignment = Alignment.Center),
                        )
                    }
                else ->
                    EpisodeSheetContent(
                        episode = episode,
                        onPlayClick = onPlayClick,
                        onPublisherClick = onPublisherClick,
                        onDownloadClick = onDownloadClick,
                    )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun EpisodeSheetScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        EpisodeSheetContent(
            episode = PREVIEW_EPISODE,
            onPlayClick = {},
            onPublisherClick = {},
            onDownloadClick = {},
        )
    }
}
