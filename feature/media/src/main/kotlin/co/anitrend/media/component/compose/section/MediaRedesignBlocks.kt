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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.PagedList
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.connection.ConnectionRailCardWidth
import co.anitrend.media.component.compose.connection.RecommendationMediaCard
import co.anitrend.media.component.compose.connection.RelatedMediaCard
import co.anitrend.media.component.compose.people.CharacterPreviewRail
import co.anitrend.media.component.compose.people.StaffPreviewList
import co.anitrend.navigation.model.common.IParam

private enum class ConnectionsTab {
    RELATED,
    RECOMMENDED,
}

@Composable
internal fun MediaConnectionsBrowserSection(
    relations: List<MediaRelationEntry>?,
    relationsLoadState: LoadState?,
    recommendations: List<MediaRecommendationEntry>?,
    recommendationsLoadState: LoadState?,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    onSeeAllRelated: () -> Unit,
    onSeeAllRecommendations: () -> Unit,
    onRetryRelations: () -> Unit,
    onRetryRecommendations: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val relatedPreview = remember(relations) { selectRelationPreview(relations.orEmpty()) }
    val recommendationPreview = remember(recommendations) { selectRecommendationPreview(recommendations.orEmpty()) }
    var selectedTab by rememberSaveable(relations?.size, recommendations?.size) {
        mutableStateOf(
            if (relatedPreview.isNotEmpty() || recommendations.isNullOrEmpty()) {
                ConnectionsTab.RELATED
            } else {
                ConnectionsTab.RECOMMENDED
            },
        )
    }

    MediaHubSection(
        title = stringResource(R.string.title_media_connections_browser_section),
        subtitle = stringResource(R.string.subtitle_media_connections_browser_section),
        trailingActionLabel =
            when (selectedTab) {
                ConnectionsTab.RELATED ->
                    relatedPreview.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let {
                        stringResource(R.string.action_media_related_section_see_all)
                    }

                ConnectionsTab.RECOMMENDED ->
                    recommendationPreview.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let {
                        stringResource(R.string.action_media_recommendations_section_see_all)
                    }
            },
        onTrailingAction =
            when (selectedTab) {
                ConnectionsTab.RELATED -> relatedPreview.takeIf(List<MediaRelationEntry>::isNotEmpty)?.let { { onSeeAllRelated() } }
                ConnectionsTab.RECOMMENDED ->
                    recommendationPreview.takeIf(List<MediaRecommendationEntry>::isNotEmpty)?.let { { onSeeAllRecommendations() } }
            },
        modifier = modifier,
    ) {
        ConnectionsSegmentedControl(
            selectedTab = selectedTab,
            onSelect = { selectedTab = it },
        )

        when (selectedTab) {
            ConnectionsTab.RELATED ->
                when {
                    relatedPreview.isNotEmpty() -> {
                        ConnectionRail {
                            items(relatedPreview, key = MediaRelationEntry::id) { relation ->
                                RelatedMediaCard(
                                    relation = relation,
                                    scoreFormat = scoreFormat,
                                    onMediaItemClick = onMediaItemClick,
                                    modifier = Modifier.width(ConnectionRailCardWidth),
                                )
                            }
                        }
                    }

                    relationsLoadState is LoadState.Loading || (relations == null && relationsLoadState !is LoadState.Error) -> {
                        LoadingSkeletonRail()
                    }

                    relationsLoadState is LoadState.Error -> {
                        RetryStateBlock(
                            title = stringResource(R.string.label_media_related_error_title),
                            onRetry = onRetryRelations,
                        )
                    }

                    else -> {
                        EmptyStateBlock(
                            title = stringResource(R.string.label_media_related_empty_title),
                            message = stringResource(R.string.message_media_related_empty),
                        )
                    }
                }

            ConnectionsTab.RECOMMENDED ->
                when {
                    recommendationPreview.isNotEmpty() -> {
                        ConnectionRail {
                            items(recommendationPreview, key = MediaRecommendationEntry::id) { recommendation ->
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

                    recommendationsLoadState is LoadState.Loading ||
                        (recommendations == null && recommendationsLoadState !is LoadState.Error) -> {
                        LoadingSkeletonRail()
                    }

                    recommendationsLoadState is LoadState.Error -> {
                        RetryStateBlock(
                            title = stringResource(R.string.label_media_recommendations_error_title),
                            onRetry = onRetryRecommendations,
                        )
                    }

                    else -> {
                        EmptyStateBlock(
                            title = stringResource(R.string.label_media_recommendations_empty_title),
                            message = stringResource(R.string.message_media_recommendations_empty),
                        )
                    }
                }
        }
    }
}

@Composable
private fun ConnectionsSegmentedControl(
    selectedTab: ConnectionsTab,
    onSelect: (ConnectionsTab) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f)),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                ConnectionsTab.RELATED to stringResource(R.string.title_media_related_section),
                ConnectionsTab.RECOMMENDED to stringResource(R.string.title_media_recommendations_section),
            ).forEach { (tab, label) ->
                val selected = tab == selectedTab
                Surface(
                    modifier = Modifier.weight(1f),
                    color =
                        if (selected) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                        },
                    contentColor =
                        if (selected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    shape = RoundedCornerShape(20.dp),
                    onClick = { onSelect(tab) },
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConnectionRail(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        content = content,
    )
}

@Composable
internal fun ContributorsSection(
    characters: PagedList<MediaPerson.Character>?,
    charactersLoadState: LoadState?,
    staff: PagedList<MediaPerson.Staff>?,
    staffLoadState: LoadState?,
    onCharacterClick: (MediaPerson.Character) -> Unit,
    onStaffClick: (MediaPerson.Staff) -> Unit,
    onRetryCharacters: () -> Unit,
    onRetryStaff: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val characterPreview = characters?.take(6).orEmpty()
    val staffPreview = staff?.take(4).orEmpty()

    MediaHubSection(
        title = stringResource(R.string.title_media_contributors_section),
        subtitle = stringResource(R.string.subtitle_media_contributors_section),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.label_media_people_characters_heading),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when {
            characterPreview.isNotEmpty() -> {
                CharacterPreviewRail(
                    items = characterPreview,
                    onItemClick = onCharacterClick,
                )
            }

            charactersLoadState is LoadState.Loading || (characters == null && charactersLoadState !is LoadState.Error) -> {
                LoadingSkeletonRail()
            }

            charactersLoadState is LoadState.Error -> {
                RetryStateBlock(
                    title = stringResource(R.string.label_media_people_characters_error_title),
                    onRetry = onRetryCharacters,
                )
            }

            else -> {
                EmptyStateBlock(
                    title = stringResource(R.string.label_media_people_characters_empty_title),
                    message = stringResource(R.string.message_media_people_characters_empty),
                )
            }
        }

        Text(
            text = stringResource(R.string.label_media_people_staff_heading),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when {
            staffPreview.isNotEmpty() -> {
                StaffPreviewList(
                    items = staffPreview,
                    onItemClick = onStaffClick,
                )
            }

            staffLoadState is LoadState.Loading || (staff == null && staffLoadState !is LoadState.Error) -> {
                LoadingSkeletonContributorRows()
            }

            staffLoadState is LoadState.Error -> {
                RetryStateBlock(
                    title = stringResource(R.string.label_media_people_staff_error_title),
                    onRetry = onRetryStaff,
                )
            }

            else -> {
                EmptyStateBlock(
                    title = stringResource(R.string.label_media_people_staff_empty_title),
                    message = stringResource(R.string.message_media_people_staff_empty),
                )
            }
        }
    }
}

@Composable
internal fun EmptyStateBlock(
    title: String,
    message: String,
) {
    MediaHubSectionEmptyState(
        title = title,
        message = message,
    )
}

@Composable
private fun RetryStateBlock(
    title: String,
    onRetry: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MediaHubSectionErrorState(title = title)
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(18.dp),
        ) {
            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
        }
    }
}

@Composable
internal fun LoadingSkeletonRail() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
    ) {
        items(3) {
            Surface(
                modifier = Modifier.width(ConnectionRailCardWidth).height(136.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(22.dp),
            ) {}
        }
    }
}

@Composable
internal fun LoadingSkeletonContributorRows() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        repeat(3) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp),
            ) {}
        }
    }
}
