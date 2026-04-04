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
package co.anitrend.media.component.compose.connection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.section.selectRecommendationPreview
import co.anitrend.media.component.compose.section.selectRelationPreview
import co.anitrend.media.component.viewmodel.MediaRecommendationsViewModel
import co.anitrend.media.component.viewmodel.MediaRelationsViewModel
import co.anitrend.navigation.model.common.IParam
import org.koin.androidx.compose.koinViewModel

private val ConnectionGridCardHeight = 280.dp

@Composable
fun MediaRelationsRoute(
    mediaId: Long,
    mediaTitle: String?,
    scoreFormat: ScoreFormat,
    onBackPress: () -> Unit,
    onMediaItemClick: (IParam) -> Unit = {},
    viewModel: MediaRelationsViewModel = koinViewModel(),
) {
    val relations by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()

    LaunchedEffect(mediaId) {
        viewModel(mediaId)
    }

    ConnectionScreenScaffold(
        title = stringResource(R.string.title_media_related_screen),
        subtitle = stringResource(R.string.subtitle_media_related_screen),
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
    ) {
        when {
            !relations.isNullOrEmpty() -> {
                RelationGrid(
                    relations = selectRelationPreview(relations.orEmpty(), maxCount = relations.orEmpty().size),
                    scoreFormat = scoreFormat,
                    onMediaItemClick = onMediaItemClick,
                )
            }

            loadState is LoadState.Loading || (relations == null && loadState !is LoadState.Error) -> {
                CenteredConnectionState(
                    title = stringResource(R.string.label_media_related_loading),
                    subtitle = stringResource(R.string.message_media_related_loading),
                )
            }

            loadState is LoadState.Error -> {
                RetryConnectionState(
                    title = stringResource(R.string.label_media_related_error_title),
                    onRetry = { viewModel(mediaId) },
                )
            }

            else -> {
                CenteredConnectionState(
                    title = stringResource(R.string.label_media_related_empty_title),
                    subtitle = stringResource(R.string.message_media_related_empty),
                )
            }
        }
    }
}

@Composable
fun MediaRecommendationsRoute(
    mediaId: Long,
    mediaTitle: String?,
    scoreFormat: ScoreFormat,
    onBackPress: () -> Unit,
    onMediaItemClick: (IParam) -> Unit = {},
    viewModel: MediaRecommendationsViewModel = koinViewModel(),
) {
    val recommendations by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()

    LaunchedEffect(mediaId) {
        viewModel(mediaId, perPage = 24)
    }

    ConnectionScreenScaffold(
        title = stringResource(R.string.title_media_recommendations_screen),
        subtitle = stringResource(R.string.subtitle_media_recommendations_screen),
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
    ) {
        when {
            !recommendations.isNullOrEmpty() -> {
                RecommendationGrid(
                    recommendations =
                        selectRecommendationPreview(
                            recommendations.orEmpty(),
                            maxCount = recommendations.orEmpty().size,
                        ),
                    scoreFormat = scoreFormat,
                    onMediaItemClick = onMediaItemClick,
                )
            }

            loadState is LoadState.Loading || (recommendations == null && loadState !is LoadState.Error) -> {
                CenteredConnectionState(
                    title = stringResource(R.string.label_media_recommendations_loading),
                    subtitle = stringResource(R.string.message_media_recommendations_loading),
                )
            }

            loadState is LoadState.Error -> {
                RetryConnectionState(
                    title = stringResource(R.string.label_media_recommendations_error_title),
                    onRetry = { viewModel(mediaId, perPage = 24) },
                )
            }

            else -> {
                CenteredConnectionState(
                    title = stringResource(R.string.label_media_recommendations_empty_title),
                    subtitle = stringResource(R.string.message_media_recommendations_empty),
                )
            }
        }
    }
}

@Composable
private fun ConnectionScreenScaffold(
    title: String,
    subtitle: String,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    content: @Composable () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                mediaTitle
                    ?.takeIf(String::isNotBlank)
                    ?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Composable
private fun RelationGrid(
    relations: List<MediaRelationEntry>,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(relations, key = MediaRelationEntry::id) { relation ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ConnectionMetaPill(
                    label = relation.relation?.alias?.toString().orEmpty(),
                )
                MediaCompactItem(
                    media = relation.media,
                    mediaPreferenceData = mediaPreferenceData,
                    mediaItemClick = onMediaItemClick,
                    modifier = Modifier.fillMaxWidth().height(ConnectionGridCardHeight),
                )
            }
        }
    }
}

@Composable
private fun RecommendationGrid(
    recommendations: List<MediaRecommendationEntry>,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat) }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 156.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(recommendations, key = MediaRecommendationEntry::id) { recommendation ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ConnectionMetaPill(
                    label = recommendation.rating?.let { "+$it" } ?: stringResource(R.string.label_media_recommendations_rating_unknown),
                )
                MediaCompactItem(
                    media = recommendation.media,
                    mediaPreferenceData = mediaPreferenceData,
                    mediaItemClick = onMediaItemClick,
                    modifier = Modifier.fillMaxWidth().height(ConnectionGridCardHeight),
                )
                Text(
                    text = recommendation.userName ?: stringResource(R.string.label_media_recommendations_source_unknown),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ConnectionMetaPill(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
        )
    }
}

@Composable
private fun CenteredConnectionState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun RetryConnectionState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(20.dp),
            ) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}
