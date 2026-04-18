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
package co.anitrend.profile.component.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@Composable
internal fun ProfileFeedTab(
    state: ProfileSectionState<ProfileFeed>,
    selectedFilter: ProfileActivityFilter,
    scoreFormat: ScoreFormat?,
    onFilterSelected: (ProfileActivityFilter) -> Unit,
    onMediaSelected: (Long, MediaType?) -> Unit,
    onReviewSelected: (Long, ScoreFormat?) -> Unit,
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ProfileFeedFilterSelector(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected,
        )

        when (state) {
            ProfileSectionState.Loading ->
                ProfileActivityStateSection(
                    message = stringResource(R.string.message_profile_feed_loading),
                    onRetry = null,
                )

            is ProfileSectionState.Error ->
                ProfileActivityStateSection(
                    message = stringResource(R.string.message_profile_feed_empty),
                    onRetry = onRetry,
                )

            ProfileSectionState.Empty ->
                ProfileActivityStateSection(
                    message = stringResource(R.string.message_profile_feed_empty),
                    onRetry = null,
                )

            is ProfileSectionState.Content ->
                FeedContent(
                    feed = state.data,
                    selectedFilter = selectedFilter,
                    scoreFormat = scoreFormat,
                    onMediaSelected = onMediaSelected,
                    onReviewSelected = onReviewSelected,
                )

            is ProfileSectionState.Partial ->
                FeedContent(
                    feed = state.data,
                    selectedFilter = selectedFilter,
                    scoreFormat = scoreFormat,
                    onMediaSelected = onMediaSelected,
                    onReviewSelected = onReviewSelected,
                )
        }
    }
}

@Composable
private fun ProfileFeedFilterSelector(
    selectedFilter: ProfileActivityFilter,
    onFilterSelected: (ProfileActivityFilter) -> Unit,
) {
    ProfileCompactSegmentedControl(
        items = ProfileActivityFilter.entries,
        selectedItem = selectedFilter,
        labelFor = { filter ->
            when (filter) {
                ProfileActivityFilter.All -> stringResource(R.string.label_profile_activity_filter_all)
                ProfileActivityFilter.Reviews -> stringResource(R.string.label_profile_activity_filter_reviews)
                ProfileActivityFilter.ListUpdates -> stringResource(R.string.label_profile_activity_filter_list_updates)
            }
        },
        onItemSelected = onFilterSelected,
    )
}

@Composable
private fun FeedContent(
    feed: ProfileFeed,
    selectedFilter: ProfileActivityFilter,
    scoreFormat: ScoreFormat?,
    onMediaSelected: (Long, MediaType?) -> Unit,
    onReviewSelected: (Long, ScoreFormat?) -> Unit,
) {
    val spotlight = remember(feed, selectedFilter) { feed.reviewSpotlight(selectedFilter) }
    val reviewArchive = remember(feed, selectedFilter) { feed.reviewArchive(selectedFilter) }
    val listUpdates = remember(feed, selectedFilter) { feed.filteredListUpdates(selectedFilter) }
    val hasContent = spotlight != null || reviewArchive.isNotEmpty() || listUpdates.isNotEmpty()

    if (!hasContent) {
        ProfileActivityStateSection(
            message = stringResource(R.string.message_profile_feed_empty),
            onRetry = null,
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        spotlight?.let { review ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    title = stringResource(R.string.title_profile_section_review_spotlight),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )
                Column(modifier = Modifier.padding(horizontal = SectionHorizontalPadding)) {
                    ReviewSpotlightCard(
                        review = review,
                        scoreFormat = scoreFormat,
                        onReviewSelected = onReviewSelected,
                    )
                }
            }
        }

        if (reviewArchive.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    title = stringResource(R.string.title_profile_section_recent_reviews),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )
                Column(
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    reviewArchive.forEach { review ->
                        ReviewPreviewCard(
                            review = review,
                            scoreFormat = scoreFormat,
                            onReviewSelected = onReviewSelected,
                        )
                    }
                }
            }
        }

        if (listUpdates.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    title = stringResource(R.string.title_profile_section_recent_list_updates),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )
                Column(
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    listUpdates.forEach { activity ->
                        ProfilePosterActivityRow(
                            item = activity,
                            onMediaSelected = onMediaSelected,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileActivityStateSection(
    message: String,
    onRetry: (() -> Unit)?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_activity),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        ProfileCompactStateSurface(
            message = message,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
            actionLabel = onRetry?.let { stringResource(co.anitrend.core.R.string.label_text_action_retry) },
            onAction = onRetry,
        )
    }
}

@Composable
private fun ReviewSpotlightCard(
    review: ProfileFeed.ReviewPreview,
    scoreFormat: ScoreFormat?,
    onReviewSelected: (Long, ScoreFormat?) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onReviewSelected(review.id, scoreFormat) },
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            review.media?.let { media ->
                Box(modifier = Modifier.fillMaxWidth().height(188.dp)) {
                    AniTrendImage(
                        image = media.image,
                        imageType = RequestImage.Media.ImageType.BANNER,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                        onClick = {},
                    )
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.22f)),
                    )
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                review.media?.displayTitleText()?.takeIf(String::isNotBlank)?.let { title ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (review.score > 0) {
                        ProfilePill(label = stringResource(R.string.label_profile_review_score, review.score))
                    }
                    if (review.ratingAmount > 0) {
                        ProfilePill(
                            label = stringResource(R.string.label_profile_review_votes, review.ratingAmount.toHumanReadableQuantity(0)),
                        )
                    }
                }
                Text(
                    text = review.summaryText(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                )
                formatEpochDate(review.updatedAt.takeIf { it > 0L } ?: review.createdAt)?.let { dateLabel ->
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewPreviewCard(
    review: ProfileFeed.ReviewPreview,
    scoreFormat: ScoreFormat?,
    onReviewSelected: (Long, ScoreFormat?) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { onReviewSelected(review.id, scoreFormat) },
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                review.media?.let { media ->
                    Box(modifier = Modifier.width(72.dp).height(104.dp)) {
                        AniTrendImage(
                            image = media.image,
                            imageType = RequestImage.Media.ImageType.POSTER,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                            onClick = {},
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    review.media?.displayTitleText()?.takeIf(String::isNotBlank)?.let { title ->
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        text = review.summaryText(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (review.score > 0) {
                            item {
                                ProfilePill(label = stringResource(R.string.label_profile_review_score, review.score))
                            }
                        }
                        if (review.ratingAmount > 0) {
                            item {
                                ProfilePill(
                                    label = stringResource(R.string.label_profile_review_votes, review.ratingAmount.toHumanReadableQuantity(0)),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
