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
package co.anitrend.review.discover.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.common.review.ui.compose.ReviewBrowseCard
import co.anitrend.common.review.ui.compose.ReviewCardVariant
import co.anitrend.common.review.ui.compose.ReviewLoadingCard
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.extensions.toDataBuilder
import co.anitrend.review.discover.R
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReviewDiscoverRoute(
    onBackPress: () -> Unit,
    onReviewClick: (Long) -> Unit,
    viewModel: ReviewDiscoverViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val authenticationSettings: IAuthenticationSettings = koinInject()
    val workManager = remember(context) { WorkManager.getInstance(context) }
    val scope = rememberCoroutineScope()
    val pendingVotes = remember { mutableStateMapOf<Long, Boolean>() }
    val params by viewModel.params.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    val refreshState = reviews.loadState.refresh
    val authenticatedUserId = authenticationSettings.authenticatedUserId.value

    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResource(R.string.title_review_discover),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitleFor(params),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    reviews.itemCount > 0 ->
                        ReviewDiscoverList(
                            reviews = reviews,
                            authenticatedUserId = authenticatedUserId,
                            isVotePending = { reviewId -> pendingVotes[reviewId] == true },
                            onReviewClick = onReviewClick,
                            onVoteRequested = { review, rating ->
                                if (pendingVotes[review.id] != true) {
                                    scope.launch {
                                        pendingVotes[review.id] = true
                                        try {
                                            val terminalState =
                                                submitReviewVote(
                                                    workManager = workManager,
                                                    reviewId = review.id,
                                                    rating = rating,
                                                )

                                            if (terminalState == WorkInfo.State.SUCCEEDED) {
                                                reviews.refresh()
                                            }
                                        } finally {
                                            pendingVotes.remove(review.id)
                                        }
                                    }
                                }
                            },
                        )

                    refreshState is LoadState.Loading ->
                        ReviewDiscoverLoadingState()

                    refreshState is LoadState.Error ->
                        ReviewDiscoverRetryState(
                            title = stringResource(R.string.label_review_discover_error_title),
                            subtitle = stringResource(R.string.message_review_discover_error),
                            onRetry = reviews::retry,
                        )

                    else ->
                        ReviewDiscoverState(
                            title = stringResource(R.string.label_review_discover_empty_title),
                            subtitle = stringResource(R.string.message_review_discover_empty),
                        )
                }
            }
        }
    }
}

@Composable
private fun ReviewDiscoverList(
    reviews: LazyPagingItems<Review>,
    authenticatedUserId: Long,
    isVotePending: (Long) -> Boolean,
    onReviewClick: (Long) -> Unit,
    onVoteRequested: (Review, ReviewRating) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(
            count = reviews.itemCount,
            key = reviews.itemKey { review -> review.id },
            contentType = reviews.itemContentType { "review_discover_card" },
        ) { index ->
            val review = reviews[index] ?: return@items
            ReviewBrowseCard(
                review = review,
                variant = ReviewCardVariant.Discover,
                canVote = !review.isOwnedBy(authenticatedUserId),
                isVotePending = isVotePending(review.id),
                onOpen = { onReviewClick(review.id) },
                onVoteRequested = { rating -> onVoteRequested(review, rating) },
            )
        }

        when (reviews.loadState.append) {
            is LoadState.Loading -> {
                item(
                    key = "review_discover_append_loading",
                    contentType = "review_discover_append_loading",
                ) {
                    ReviewLoadingCard(
                        variant = ReviewCardVariant.Discover,
                    )
                }
            }

            is LoadState.Error -> {
                item(
                    key = "review_discover_append_error",
                    contentType = "review_discover_append_error",
                ) {
                    ReviewDiscoverRetryState(
                        title = stringResource(R.string.label_review_discover_error_title),
                        subtitle = stringResource(R.string.message_review_discover_error),
                        onRetry = reviews::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ReviewDiscoverLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        repeat(3) {
            ReviewLoadingCard(
                variant = ReviewCardVariant.Discover,
            )
        }
    }
}

@Composable
private fun ReviewDiscoverState(
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
private fun ReviewDiscoverRetryState(
    title: String,
    subtitle: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
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
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onRetry) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}

@Composable
private fun subtitleFor(param: ReviewDiscoverRouter.ReviewDiscoverParam): String =
    stringResource(
        when {
            param.userId != null -> R.string.message_review_discover_user_scope
            param.mediaId != null -> R.string.message_review_discover_media_scope
            param.mediaType == MediaType.ANIME -> R.string.message_review_discover_anime_scope
            param.mediaType == MediaType.MANGA -> R.string.message_review_discover_manga_scope
            else -> R.string.message_review_discover_default
        },
    )

private suspend fun submitReviewVote(
    workManager: WorkManager,
    reviewId: Long,
    rating: ReviewRating,
): WorkInfo.State {
    val params =
        ReviewTaskRouter.Param.RateEntry(
            id = reviewId,
            rating = rating,
        )
    val request =
        OneTimeWorkRequest
            .Builder(ReviewTaskRouter.forReviewRateWorker())
            .setInputData(params.toDataBuilder().build())
            .build()

    workManager.enqueue(request)

    return workManager
        .getWorkInfoByIdFlow(request.id)
        .filterNotNull()
        .first { it.state.isFinished }
        .state
}

private fun Review.isOwnedBy(authenticatedUserId: Long): Boolean =
    authenticatedUserId != IAuthenticationSettings.INVALID_USER_ID && authenticatedUserId == userId
