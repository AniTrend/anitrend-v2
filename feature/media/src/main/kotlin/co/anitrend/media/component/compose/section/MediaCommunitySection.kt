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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import co.anitrend.common.review.ui.compose.ReviewBrowseCard
import co.anitrend.common.review.ui.compose.ReviewCardVariant
import co.anitrend.common.review.ui.compose.ReviewLoadingCard
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.media.R

@Composable
internal fun MediaCommunitySection(
    reviews: LazyPagingItems<Review>?,
    scoreFormat: ScoreFormat,
    isBlocked: Boolean,
    authenticatedUserId: Long,
    onSeeAllClick: () -> Unit,
    onRetry: () -> Unit,
    onReviewClick: (Long) -> Unit,
    isVotePending: (Long) -> Boolean,
    onVoteRequested: (Review, ReviewRating) -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewItems =
        remember(reviews?.itemSnapshotList) {
            reviews
                ?.itemSnapshotList
                ?.items
                .orEmpty()
                .take(2)
        }
    val refreshState = reviews?.loadState?.refresh
    val canSeeAll = !isBlocked && previewItems.isNotEmpty()

    MediaHubSection(
        title = stringResource(R.string.title_media_community_section),
        subtitle = stringResource(R.string.subtitle_media_community_section),
        trailingActionLabel =
            if (canSeeAll) {
                stringResource(R.string.action_media_community_section_see_all)
            } else {
                null
            },
        onTrailingAction = if (canSeeAll) ({ onSeeAllClick() }) else null,
        modifier = modifier,
    ) {
        if (isBlocked) {
            MediaHubSectionEmptyState(
                title = stringResource(R.string.label_media_community_blocked_title),
                message = stringResource(R.string.message_media_community_blocked),
            )
            return@MediaHubSection
        }

        when {
            previewItems.isNotEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    previewItems.forEach { review ->
                        ReviewBrowseCard(
                            review = review,
                            scoreFormat = scoreFormat,
                            variant = ReviewCardVariant.InlineCommunity,
                            canVote = !review.isOwnedBy(authenticatedUserId),
                            isVotePending = isVotePending(review.id),
                            onOpen = { onReviewClick(review.id) },
                            onVoteRequested = { rating -> onVoteRequested(review, rating) },
                        )
                    }
                }
            }

            refreshState is LoadState.Loading || reviews == null -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    repeat(2) {
                        ReviewLoadingCard(
                            variant = ReviewCardVariant.InlineCommunity,
                        )
                    }
                }
            }

            refreshState is LoadState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MediaHubSectionErrorState(
                        title = stringResource(R.string.label_media_community_error_title),
                        message = stringResource(R.string.message_media_community_error),
                    )
                    OutlinedButton(
                        onClick = onRetry,
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
                    }
                }
            }

            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_community_empty_title),
                    message = stringResource(R.string.message_media_community_empty),
                )
            }
        }
    }
}

private fun Review.isOwnedBy(authenticatedUserId: Long): Boolean =
    authenticatedUserId != IAuthenticationSettings.INVALID_USER_ID && authenticatedUserId == userId
