/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.search.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

@Composable
internal fun UserSearchSection(
    title: String,
    items: LazyPagingItems<User>,
    onUserClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = title, onSeeAllClick = onSeeAllClick)

        when {
            items.itemCount > 0 -> {
                val previewItems = remember(items.itemCount) { List(items.itemCount) { index -> items[index] }.filterNotNull().take(8) }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { user -> user.id }) { user ->
                        UserCompactRow(
                            user = user,
                            onClick = {
                                onUserClick(ProfileRouter.ProfileParam(userId = user.id, userName = user.name.toString()))
                            },
                        )
                    }
                }
            }

            refreshState is LoadState.Loading ->
                SearchState(
                    title = stringResource(R.string.label_search_loading_title),
                    subtitle = stringResource(R.string.message_search_loading),
                )

            refreshState is LoadState.Error ->
                SearchState(
                    title = stringResource(R.string.label_search_error_title),
                    subtitle = stringResource(R.string.message_search_error),
                    onRetry = items::retry,
                )

            else ->
                SearchState(
                    title = stringResource(R.string.label_search_empty_title),
                    subtitle = stringResource(R.string.message_search_empty),
                )
        }
    }
}

@Composable
internal fun UserDrillDown(
    items: LazyPagingItems<User>,
    onUserClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = items.loadState.refresh
    when {
        items.itemCount > 0 -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { user -> user.id },
                    contentType = items.itemContentType { "search_user_row" },
                ) { index ->
                    val user = items[index] ?: return@items
                    UserListItem(
                        user = user,
                        onClick = {
                            onUserClick(ProfileRouter.ProfileParam(userId = user.id, userName = user.name.toString()))
                        },
                    )
                }
            }
        }

        refreshState is LoadState.Loading ->
            SearchState(
                title = stringResource(R.string.label_search_loading_title),
                subtitle = stringResource(R.string.message_search_loading),
            )

        refreshState is LoadState.Error ->
            SearchState(
                title = stringResource(R.string.label_search_error_title),
                subtitle = stringResource(R.string.message_search_error),
                onRetry = items::retry,
            )

        else ->
            SearchState(
                title = stringResource(R.string.label_search_empty_title),
                subtitle = stringResource(R.string.message_search_empty),
            )
    }
}

@Composable
private fun UserCompactRow(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = user.name.toString()
    Surface(
        modifier =
            modifier
                .width(138.dp)
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(24.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun UserListItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = user.name.toString()
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(22.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
