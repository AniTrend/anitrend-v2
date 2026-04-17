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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.connection.ConnectionRailCardWidth
import co.anitrend.media.component.compose.connection.RecommendationMediaCard
import co.anitrend.media.component.compose.connection.RelatedMediaCard
import co.anitrend.navigation.model.common.IParam

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
        trailingActionLabel =
            previewItems.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let {
                stringResource(R.string.action_media_related_section_see_all)
            },
        onTrailingAction = previewItems.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let { { onSeeAllClick() } },
        modifier = modifier,
    ) {
        when {
            previewItems.isNotEmpty() -> {
                ConnectionPreviewRail {
                    items(previewItems, key = MediaRelationEntry::id) { relation ->
                        RelatedMediaCard(
                            relation = relation,
                            scoreFormat = scoreFormat,
                            onMediaItemClick = onMediaItemClick,
                            modifier = Modifier.width(ConnectionRailCardWidth),
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
        trailingActionLabel =
            previewItems.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let {
                stringResource(R.string.action_media_recommendations_section_see_all)
            },
        onTrailingAction = previewItems.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let { { onSeeAllClick() } },
        modifier = modifier,
    ) {
        when {
            previewItems.isNotEmpty() -> {
                ConnectionPreviewRail {
                    items(previewItems, key = MediaRecommendationEntry::id) { recommendation ->
                        RecommendationMediaCard(
                            recommendation = recommendation,
                            scoreFormat = scoreFormat,
                            onMediaItemClick = onMediaItemClick,
                            modifier = Modifier.width(ConnectionRailCardWidth),
                            rationaleMaxLines = 1,
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
private fun ConnectionPreviewRail(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    MediaSectionRail(content = content)
}

@Composable
private fun MediaSectionRetryState(
    title: String,
    onRetry: () -> Unit,
) {
    MediaHubSectionRetryState(
        title = title,
        onRetry = onRetry,
    )
}
