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
package co.anitrend.about.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.about.component.compose.state.WorkItem
import co.anitrend.about.component.compose.state.WorkState
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

@Composable
fun WorkManagerStatusScreen(
    modifier: Modifier = Modifier,
    workItems: List<WorkItem>,
    onCancelWork: (String) -> Unit,
) {
    // Define the desired order for displaying states.
    val stateOrder =
        mapOf(
            WorkState.Running to 0,
            WorkState.Enqueued to 1,
            WorkState.Blocked to 2,
            WorkState.Succeeded to 3,
            WorkState.Failed to 4,
            WorkState.Cancelled to 5,
        )
    // Group work items by their state.
    val groupedItems = workItems.groupBy { it.state }
    val orderedStates = WorkState.entries.sortedBy { stateOrder[it] ?: 99 }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        // For each state, if there are any work items, show a section header and its items.
        orderedStates.forEach { state ->
            groupedItems[state]?.let { itemsForState ->
                item { SectionHeader(title = state.name) }
                items(itemsForState) { workItem ->
                    WorkItemCard(workItem = workItem, onCancelWork = onCancelWork)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp),
        )
        HorizontalDivider()
    }
}

// A detailed card representing a single work item.
@Composable
fun WorkItemCard(
    workItem: WorkItem,
    onCancelWork: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Left section: task details.
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workItem.tags,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (workItem.nextScheduleTime.isNotEmpty() && workItem.state == WorkState.Enqueued) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = workItem.nextScheduleTime,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Text(
                    text = "Attempts: ${workItem.runAttemptCount} | Interval: ${workItem.flexInterval ?: "TBC"}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            // Right section: state chip and cancel button (if cancellation is applicable).
            Column(horizontalAlignment = Alignment.End) {
                if (workItem.state in listOf(WorkState.Enqueued, WorkState.Running, WorkState.Blocked)) {
                    IconButton(onClick = { onCancelWork(workItem.id) }) {
                        Icon(
                            imageVector = if (workItem.state == WorkState.Running) Icons.Default.StopCircle else Icons.Default.Delete,
                            contentDescription = "Cancel Work",
                        )
                    }
                }
            }
        }
        // If the work is currently running, display a progress indicator.
        if (workItem.state == WorkState.Running) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@AniTrendPreview.Default
@Composable
fun PreviewWorkManagerStatusScreen(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    val dummyWorkItems =
        listOf(
            WorkItem(
                id = "Task-1",
                state = WorkState.Enqueued,
                info = "Waiting in queue",
                runAttemptCount = 0,
                tags = "Task-1-tag",
                flexInterval = "10 minutes",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            WorkItem(
                id = "Task-2",
                state = WorkState.Running,
                info = "Processing data...",
                runAttemptCount = 1,
                tags = "Task-2-tag",
                flexInterval = "10 minutes",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            WorkItem(
                id = "Task-3",
                state = WorkState.Blocked,
                info = "Waiting for prerequisites",
                runAttemptCount = 2,
                tags = "Task-3-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            WorkItem(
                id = "Task-4",
                state = WorkState.Succeeded,
                info = "Completed successfully",
                runAttemptCount = 1,
                tags = "Task-4-tag",
                flexInterval = "5",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            WorkItem(
                id = "Task-5",
                state = WorkState.Failed,
                info = "Error: network issue",
                runAttemptCount = 3,
                tags = "Task-5-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            WorkItem(
                id = "Task-6",
                state = WorkState.Cancelled,
                info = "Cancelled by user",
                runAttemptCount = 1,
                tags = "Task-6-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
        )
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        WorkManagerStatusScreen(
            workItems = dummyWorkItems,
            onCancelWork = {},
        )
    }
}
