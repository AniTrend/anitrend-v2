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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.R
import co.anitrend.media.component.compose.connection.ConnectionRailCardWidth
import co.anitrend.media.component.compose.connection.RecommendationMediaCard
import co.anitrend.media.component.compose.connection.RelatedMediaCard
import co.anitrend.media.component.compose.people.CharacterPreviewList
import co.anitrend.media.component.compose.people.StaffPreviewList
import co.anitrend.media.component.compose.people.curatedCharacterPreview
import co.anitrend.media.component.compose.people.curatedStaffPreview
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.model.common.IParam

private const val ContributorPreviewCount = 3

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
        MediaSectionSegmentedControl(
            selected = selectedTab,
            options =
                listOf(
                    ConnectionsTab.RELATED to stringResource(R.string.title_media_related_section),
                    ConnectionsTab.RECOMMENDED to stringResource(R.string.title_media_recommendations_section),
                ),
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
private fun ConnectionRail(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    MediaSectionRail(content = content)
}

@Composable
internal fun ContributorsSection(
    characters: LazyPagingItems<MediaPerson.Character>?,
    staff: LazyPagingItems<MediaPerson.Staff>?,
    onSeeAllCharacters: () -> Unit,
    onSeeAllStaff: () -> Unit,
    onCharacterClick: (MediaPerson.Character) -> Unit,
    onStaffClick: (MediaPerson.Staff) -> Unit,
    onRetryCharacters: () -> Unit,
    onRetryStaff: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val characterPreview = characters?.itemSnapshotList?.items.orEmpty().curatedCharacterPreview(ContributorPreviewCount)
    val staffPreview = staff?.itemSnapshotList?.items.orEmpty().curatedStaffPreview(ContributorPreviewCount)
    val characterRefreshState = characters?.loadState?.refresh
    val staffRefreshState = staff?.loadState?.refresh
    var selectedSection by rememberSaveable(characterPreview.size, staffPreview.size) {
        mutableStateOf(
            preferredContributorSection(
                characterPreview = characterPreview,
                staffPreview = staffPreview,
            ),
        )
    }

    MediaHubSection(
        title = stringResource(R.string.title_media_contributors_section),
        subtitle = stringResource(R.string.subtitle_media_contributors_section),
        trailingActionLabel =
            when (selectedSection) {
                MediaPeopleRouter.Section.CHARACTERS ->
                    characterPreview.takeIf(List<MediaPerson.Character>::isNotEmpty)?.let {
                        stringResource(R.string.action_media_people_section_see_all)
                    }

                MediaPeopleRouter.Section.STAFF ->
                    staffPreview.takeIf(List<MediaPerson.Staff>::isNotEmpty)?.let {
                        stringResource(R.string.action_media_people_section_see_all)
                    }
            },
        onTrailingAction =
            when (selectedSection) {
                MediaPeopleRouter.Section.CHARACTERS ->
                    characterPreview.takeIf(List<MediaPerson.Character>::isNotEmpty)?.let { { onSeeAllCharacters() } }

                MediaPeopleRouter.Section.STAFF ->
                    staffPreview.takeIf(List<MediaPerson.Staff>::isNotEmpty)?.let { { onSeeAllStaff() } }
            },
        modifier = modifier,
    ) {
        MediaSectionSegmentedControl(
            selected = selectedSection,
            options =
                listOf(
                    MediaPeopleRouter.Section.CHARACTERS to stringResource(R.string.label_media_people_characters_heading),
                    MediaPeopleRouter.Section.STAFF to stringResource(R.string.label_media_people_staff_heading),
                ),
            onSelect = { selectedSection = it },
        )

        when (selectedSection) {
            MediaPeopleRouter.Section.CHARACTERS ->
                when {
                    characterPreview.isNotEmpty() -> {
                        CharacterPreviewList(
                            items = characterPreview,
                            onItemClick = onCharacterClick,
                        )
                    }

                    characterRefreshState == null || characterRefreshState is androidx.paging.LoadState.Loading -> {
                        LoadingSkeletonContributorRows()
                    }

                    characterRefreshState is androidx.paging.LoadState.Error -> {
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

            MediaPeopleRouter.Section.STAFF ->
                when {
                    staffPreview.isNotEmpty() -> {
                        StaffPreviewList(
                            items = staffPreview,
                            onItemClick = onStaffClick,
                        )
                    }

                    staffRefreshState == null || staffRefreshState is androidx.paging.LoadState.Loading -> {
                        LoadingSkeletonContributorRows()
                    }

                    staffRefreshState is androidx.paging.LoadState.Error -> {
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
}

private fun preferredContributorSection(
    characterPreview: List<MediaPerson.Character>,
    staffPreview: List<MediaPerson.Staff>,
): MediaPeopleRouter.Section =
    when {
        staffPreview.isNotEmpty() && characterPreview.isEmpty() -> MediaPeopleRouter.Section.STAFF
        else -> MediaPeopleRouter.Section.CHARACTERS
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
    MediaHubSectionRetryState(
        title = title,
        onRetry = onRetry,
    )
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
