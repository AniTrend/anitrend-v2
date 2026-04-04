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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.navigation.model.common.IParam

private val PreviewRailCardWidth = 150.dp
private val PreviewRailCardHeight = 268.dp

@Composable
internal fun MediaRelatedPreviewSection(
    relations: List<MediaRelationEntry>?,
    loadState: LoadState?,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewItems = remember(relations) { selectRelationPreview(relations.orEmpty()) }

    MediaHubSection(
        title = stringResource(R.string.title_media_related_section),
        subtitle = stringResource(R.string.subtitle_media_related_section),
        trailingActionLabel = previewItems.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let {
            stringResource(R.string.action_media_related_section_see_all)
        },
        onTrailingAction = previewItems.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let { { onSeeAllClick() } },
        modifier = modifier,
    ) {
        when {
            previewItems.isNotEmpty() -> {
                ConnectionPreviewRail {
                    items(previewItems, key = MediaRelationEntry::id) { relation ->
                        MediaConnectionRailItem(
                            metadata = relation.relation?.alias?.toString(),
                            scoreFormat = scoreFormat,
                            onMediaItemClick = onMediaItemClick,
                            param = relation.media,
                        )
                    }
                }
            }

            loadState is LoadState.Loading || (relations == null && loadState !is LoadState.Error) -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_related_loading),
                    message = stringResource(R.string.message_media_related_loading),
                )
            }

            loadState is LoadState.Error -> {
                MediaSectionRetryState(
                    title = stringResource(R.string.label_media_related_error_title),
                    onRetry = onRetry,
                )
            }

            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_related_empty_title),
                    message = stringResource(R.string.message_media_related_empty),
                )
            }
        }
    }
}

@Composable
internal fun MediaRecommendationsPreviewSection(
    recommendations: List<MediaRecommendationEntry>?,
    loadState: LoadState?,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewItems = remember(recommendations) { selectRecommendationPreview(recommendations.orEmpty()) }

    MediaHubSection(
        title = stringResource(R.string.title_media_recommendations_section),
        subtitle = stringResource(R.string.subtitle_media_recommendations_section),
        trailingActionLabel = previewItems.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let {
            stringResource(R.string.action_media_recommendations_section_see_all)
        },
        onTrailingAction = previewItems.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let { { onSeeAllClick() } },
        modifier = modifier,
    ) {
        when {
            previewItems.isNotEmpty() -> {
                ConnectionPreviewRail {
                    items(previewItems, key = MediaRecommendationEntry::id) { recommendation ->
                        MediaConnectionRailItem(
                            metadata = recommendationRatingLabel(recommendation),
                            supportingText = recommendation.userName?.takeIf(String::isNotBlank),
                            scoreFormat = scoreFormat,
                            onMediaItemClick = onMediaItemClick,
                            param = recommendation.media,
                        )
                    }
                }
            }

            loadState is LoadState.Loading || (recommendations == null && loadState !is LoadState.Error) -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_recommendations_loading),
                    message = stringResource(R.string.message_media_recommendations_loading),
                )
            }

            loadState is LoadState.Error -> {
                MediaSectionRetryState(
                    title = stringResource(R.string.label_media_recommendations_error_title),
                    onRetry = onRetry,
                )
            }

            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_recommendations_empty_title),
                    message = stringResource(R.string.message_media_recommendations_empty),
                )
            }
        }
    }
}

@Composable
private fun ConnectionPreviewRail(
    content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content,
    )
}

@Composable
private fun MediaConnectionRailItem(
    param: co.anitrend.domain.media.entity.Media,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    metadata: String?,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
) {
    val preferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.width(PreviewRailCardWidth),
    ) {
        metadata
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?.let {
                ConnectionMetaPill(
                    label = it,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

        MediaCompactItem(
            media = param,
            mediaPreferenceData = preferenceData,
            mediaItemClick = onMediaItemClick,
            modifier = Modifier.fillMaxWidth().height(PreviewRailCardHeight),
        )

        supportingText
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?.let {
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
        modifier = modifier,
    ) {
        Text(
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 7.dp),
        )
    }
}

@Composable
private fun recommendationRatingLabel(recommendation: MediaRecommendationEntry): String =
    recommendation.rating?.let { "+$it" } ?: stringResource(R.string.label_media_recommendations_rating_unknown)

@Composable
private fun MediaSectionRetryState(
    title: String,
    onRetry: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MediaHubSectionErrorState(title = title)
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
        }
    }
}
