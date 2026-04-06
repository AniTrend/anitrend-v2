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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.android.core.asPrettyTime
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.entity.Review
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.review.discover.R
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import org.koin.androidx.compose.koinViewModel
import org.threeten.bp.Instant

@Composable
fun ReviewDiscoverRoute(
    onBackPress: () -> Unit,
    viewModel: ReviewDiscoverViewModel = koinViewModel(),
) {
    val params by viewModel.params.collectAsStateWithLifecycle()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    val refreshState = reviews.loadState.refresh

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
                    reviews.itemCount > 0 -> ReviewDiscoverList(reviews = reviews)

                    refreshState is LoadState.Loading ->
                        ReviewDiscoverState(
                            title = stringResource(R.string.label_review_discover_loading_title),
                            subtitle = stringResource(R.string.message_review_discover_loading),
                        )

                    refreshState is LoadState.Error ->
                        ReviewDiscoverRetryState(
                            title = stringResource(R.string.label_review_discover_error_title),
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
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(count = reviews.itemCount) { index ->
            val review = reviews[index] ?: return@items
            ReviewDiscoverCard(review = review)
        }

        when (reviews.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Text(
                        text = stringResource(R.string.message_review_discover_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    ReviewDiscoverRetryState(
                        title = stringResource(R.string.label_review_discover_error_title),
                        onRetry = reviews::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun ReviewDiscoverCard(
    review: Review,
    modifier: Modifier = Modifier,
) {
    val mediaTitle = (review as? Review.Extended)?.media?.displayTitle()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.16f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = review.user.name.toString().ifBlank { stringResource(R.string.label_review_discover_unknown_author) },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(R.string.label_review_discover_score, review.score),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (!mediaTitle.isNullOrBlank()) {
                Text(
                    text = mediaTitle,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Text(
                text = review.summary,
                maxLines = 5,
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

private fun Media.displayTitle(): String? =
    title.userPreferred
        ?.toString()
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?: listOf(title.english, title.romaji, title.native)
            .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
            .firstOrNull()
