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
import androidx.paging.PagedList
import co.anitrend.android.core.asPrettyTime
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.review.entity.Review
import co.anitrend.media.R
import org.threeten.bp.Instant

@Composable
internal fun MediaCommunitySection(
    reviews: PagedList<Review>?,
    loadState: LoadState?,
    isBlocked: Boolean,
    onSeeAllClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val previewItems = remember(reviews) { reviews?.previewItems(maxCount = 3).orEmpty() }
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
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    previewItems.forEach { review ->
                        CommunityReviewCard(review = review)
                    }
                }
            }

            loadState is LoadState.Loading || (reviews == null && loadState !is LoadState.Error) -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_community_loading),
                    message = stringResource(R.string.message_media_community_loading),
                )
            }

            loadState is LoadState.Error -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MediaHubSectionErrorState(
                        title = stringResource(R.string.label_media_community_error_title),
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

@Composable
private fun CommunityReviewCard(
    review: Review,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.76f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text =
                        review.user.name.toString().ifBlank {
                            stringResource(R.string.label_media_community_review_by_unknown)
                        },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.label_media_community_review_score, review.score),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = review.summary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = Instant.ofEpochSecond(review.createdAt).asPrettyTime(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun PagedList<Review>.previewItems(maxCount: Int): List<Review> =
    buildList {
        repeat(minOf(size, maxCount)) { index ->
            this@previewItems[index]?.let(::add)
        }
    }
