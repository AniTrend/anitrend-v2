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
package co.anitrend.airing.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.compose.material3.rememberDatePickerState
import co.anitrend.airing.R
import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.model.common.IParam
import org.koin.androidx.compose.koinViewModel
import org.threeten.bp.Instant

@Composable
fun AiringRoute(
    settings: ICustomizationSettings,
    userSettings: IUserSettings,
    dateHelper: AniTrendDateHelper,
    onBackPress: () -> Unit,
    onMediaItemClick: (IParam) -> Unit,
    viewModel: AiringViewModel = koinViewModel(),
) {
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val preferredViewMode by settings.preferredViewMode.flow.collectAsStateWithLifecycle(
        initialValue = settings.preferredViewMode.value,
    )
    val scoreFormat by userSettings.scoreFormat.flow.collectAsStateWithLifecycle(
        initialValue = IUserSettings.DEFAULT_SCORE_FORMAT,
    )
    val airings = viewModel.schedule.collectAsLazyPagingItems()
    val refreshState = airings.loadState.refresh
    val context = LocalContext.current
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val selectedDateLabel = remember(filter.airingAt_greater) { formatAiringDate(filter.airingAt_greater, dateHelper) }
    val selectedDateMillis =
        remember(filter.airingAt_greater) {
            filter.airingAt_greater?.toDatePickerInitialMillis(dateHelper)
        }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis
                            ?.toAiringFilterEpochSecond(dateHelper)
                            ?.let { epochSecond ->
                                viewModel.setFilter(
                                    filter.copy(
                                        airingAt_greater = epochSecond,
                                    ),
                                )
                            }
                        showDatePicker = false
                    },
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.title_airing_schedule),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text =
                        selectedDateLabel?.let {
                            context.getString(R.string.message_airing_filter_active, it)
                        } ?: stringResource(R.string.message_airing_filter_default),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                    ) {
                        Text(text = stringResource(R.string.action_airing_pick_date))
                    }
                    if (filter != viewModel.initialParam) {
                        TextButton(onClick = viewModel::resetFilter) {
                            Text(text = stringResource(R.string.action_airing_reset_date))
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    airings.itemCount > 0 ->
                        AiringGrid(
                            airings = airings,
                            preferredViewMode = preferredViewMode,
                            scoreFormat = scoreFormat,
                            onMediaItemClick = onMediaItemClick,
                        )

                    refreshState is LoadState.Loading ->
                        AiringState(
                            title = stringResource(R.string.label_airing_loading_title),
                            subtitle = stringResource(R.string.message_airing_loading),
                        )

                    refreshState is LoadState.Error ->
                        RetryAiringState(
                            title = stringResource(R.string.label_airing_error_title),
                            onRetry = airings::retry,
                        )

                    else ->
                        AiringState(
                            title = stringResource(R.string.label_airing_empty_title),
                            subtitle = stringResource(R.string.message_airing_empty),
                        )
                }
            }
        }
    }
}

@Composable
private fun AiringGrid(
    airings: LazyPagingItems<Media>,
    preferredViewMode: PreferredViewMode,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = preferredViewMode.gridColumns()
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat = scoreFormat) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            count = airings.itemCount,
            key = airings.itemKey { media -> media.id },
            contentType = airings.itemContentType { "airing_grid_item" },
        ) { index ->
            val media = airings[index] ?: return@items

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = if (columns == 1) Alignment.CenterStart else Alignment.Center,
            ) {
                MediaCompactItem(
                    media = media,
                    mediaPreferenceData = mediaPreferenceData,
                    mediaItemClick = onMediaItemClick,
                    modifier =
                        if (columns == 1) {
                            Modifier
                                .width(168.dp)
                                .height(285.dp)
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.55f)
                        },
                )
            }
        }

        when (airings.loadState.append) {
            is LoadState.Loading -> {
                item(
                    key = "airing_append_loading",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "airing_append_loading",
                ) {
                    Text(
                        text = stringResource(R.string.message_airing_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item(
                    key = "airing_append_error",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "airing_append_error",
                ) {
                    RetryAiringState(
                        title = stringResource(R.string.label_airing_error_title),
                        onRetry = airings::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun AiringState(
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
private fun RetryAiringState(
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

private fun PreferredViewMode.gridColumns(): Int =
    when (this) {
        PreferredViewMode.COMPACT -> 3
        PreferredViewMode.COMFORTABLE -> 2
        PreferredViewMode.SUMMARY,
        PreferredViewMode.DETAILED,
        -> 1
    }

private fun formatAiringDate(
    epochSecond: Int?,
    dateHelper: AniTrendDateHelper,
): String? {
    if (epochSecond == null) {
        return null
    }

    return dateHelper
        .convertToTextDate(
            dateHelper.convertToFuzzyDate(
                unixTimeStamp = Instant.ofEpochSecond(epochSecond.toLong()).toEpochMilli(),
            ),
        )?.toString()
}

private fun Int.toDatePickerInitialMillis(dateHelper: AniTrendDateHelper): Long =
    dateHelper.convertToUnixTimeStamp(
        dateHelper.convertToFuzzyDate(
            unixTimeStamp = Instant.ofEpochSecond(toLong()).toEpochMilli(),
        ),
    )

private fun Long.toAiringFilterEpochSecond(dateHelper: AniTrendDateHelper): Int =
    dateHelper
        .convertToUnixTimeStamp(
            dateHelper.convertToFuzzyDate(unixTimeStamp = this),
        ).div(1000)
        .toInt()
