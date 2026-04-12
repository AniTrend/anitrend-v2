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
package co.anitrend.review.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.review.ui.compose.ReviewReaderContent
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.navigation.extensions.toDataBuilder
import co.anitrend.review.R
import co.anitrend.review.component.viewmodel.ReviewViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun ReviewRoute(
    onBackPress: () -> Unit,
    viewModel: ReviewViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val authenticationSettings: IAuthenticationSettings = koinInject()
    val workManager = remember(context) { WorkManager.getInstance(context) }
    val scope = rememberCoroutineScope()
    var isVotePending by remember { mutableStateOf(false) }
    val review by viewModel.model.observeAsState()
    val loadState by viewModel.loadState.observeAsState()
    val authenticatedUserId = authenticationSettings.authenticatedUserId.value

    LaunchedEffect(viewModel.reviewId) {
        viewModel()
    }

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
                    text = stringResource(R.string.title_review_entry),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.subtitle_review_entry),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    review != null -> {
                        val reviewEntry = requireNotNull(review)
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            ReviewReaderContent(
                                review = reviewEntry,
                                canVote = !reviewEntry.isOwnedBy(authenticatedUserId),
                                isVotePending = isVotePending,
                                onVoteRequested = { rating ->
                                    if (!isVotePending) {
                                        scope.launch {
                                            isVotePending = true
                                            try {
                                                val terminalState =
                                                    submitReviewVote(
                                                        workManager = workManager,
                                                        reviewId = reviewEntry.id,
                                                        rating = rating,
                                                    )

                                                if (terminalState == WorkInfo.State.SUCCEEDED) {
                                                    viewModel.invoke()
                                                }
                                            } finally {
                                                isVotePending = false
                                            }
                                        }
                                    }
                                },
                            )
                        }
                    }

                    loadState is LoadState.Error -> {
                        ReviewEntryRetryState(onRetry = viewModel::invoke)
                    }

                    loadState is LoadState.Success -> {
                        ReviewEntryState(
                            title = stringResource(R.string.label_review_entry_empty_title),
                            subtitle = stringResource(R.string.message_review_entry_empty),
                        )
                    }

                    else -> {
                        ReviewEntryState(
                            title = stringResource(R.string.label_review_entry_loading_title),
                            subtitle = stringResource(R.string.message_review_entry_loading),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewEntryState(
    title: String,
    subtitle: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
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
private fun ReviewEntryRetryState(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(R.string.label_review_entry_error_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(R.string.message_review_entry_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(onClick = onRetry) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}

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
