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
package co.anitrend.settings.component.content.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.StopCircle
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkManager
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.category.AniTrendCategoryItem
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.applyOpacity
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.content.task.state.TaskState
import co.anitrend.settings.component.content.task.state.TaskWorkItem
import co.anitrend.settings.component.content.task.viewmodel.TaskViewModel
import org.koin.compose.koinInject
import java.util.UUID

@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = koinInject<TaskViewModel>(),
) {
    val context = LocalContext.current
    val workManager = remember { WorkManager.getInstance(context = context) }
    val observable = viewModel(workManager = workManager)
    val taskWorkItems by observable.collectAsStateWithLifecycle(emptyList())
    TaskContent(modifier = modifier, taskWorkItems = taskWorkItems) {
        workManager.cancelWorkById(UUID.fromString(it))
    }
}

@Composable
private fun TaskContent(
    modifier: Modifier = Modifier,
    taskWorkItems: List<TaskWorkItem>,
    onCancelWork: (String) -> Unit,
) {
    // Define the desired order for displaying states.
    val stateOrder =
        mapOf(
            TaskState.Running to 0,
            TaskState.Enqueued to 1,
            TaskState.Blocked to 2,
            TaskState.Succeeded to 3,
            TaskState.Failed to 4,
            TaskState.Cancelled to 5,
        )
    // Group work items by their state.
    val groupedItems = taskWorkItems.groupBy { it.state }
    val orderedStates = TaskState.entries.sortedBy { stateOrder[it] ?: 99 }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize(),
    ) {
        if (taskWorkItems.isEmpty()) {
            item {
                AniTrendHintCard(
                    title = stringResource(R.string.title_settings_task_empty),
                    description = stringResource(R.string.summary_settings_task_empty),
                    icon = Icons.Outlined.Task,
                )
            }
        }

        orderedStates.forEach { state ->
            groupedItems[state]?.let { itemsForState ->
                item {
                    SettingsSectionCard(
                        title = taskStateLabel(state),
                        description = pluralStringResource(R.plurals.label_settings_task_count, itemsForState.size, itemsForState.size),
                    ) {
                        itemsForState.forEachIndexed { index, workItem ->
                            val isEnabled = workItem.state !in listOf(TaskState.Cancelled, TaskState.Failed, TaskState.Blocked, TaskState.Succeeded)
                            AniTrendCategoryItem(
                                title = workItem.tags,
                                description = stringResource(R.string.label_settings_task_item_summary, workItem.info, workItem.nextScheduleTime),
                                enabled = isEnabled,
                                trailingIcon = {
                                    if (workItem.state in listOf(TaskState.Enqueued, TaskState.Running, TaskState.Blocked)) {
                                        IconButton(onClick = { onCancelWork(workItem.id) }) {
                                            Icon(
                                                imageVector =
                                                    if (workItem.state ==
                                                        TaskState.Running
                                                    ) {
                                                        Icons.Outlined.StopCircle
                                                    } else {
                                                        Icons.Outlined.Cancel
                                                    },
                                                contentDescription = stringResource(R.string.description_settings_task_cancel_work),
                                            )
                                        }
                                    }
                                },
                                leadingIcon = {
                                    if (workItem.state == TaskState.Running) {
                                        CircularProgressIndicator(
                                            modifier =
                                                Modifier
                                                    .padding(start = 8.dp, end = 16.dp)
                                                    .size(24.dp),
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.Task,
                                            contentDescription = null,
                                            modifier =
                                                Modifier
                                                    .padding(start = 8.dp, end = 16.dp)
                                                    .size(24.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(isEnabled),
                                        )
                                    }
                                },
                            )
                            if (index != itemsForState.lastIndex) {
                                androidx.compose.material3.HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 20.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun taskStateLabel(state: TaskState): String =
    when (state) {
        TaskState.Enqueued -> stringResource(R.string.label_settings_task_state_enqueued)
        TaskState.Running -> stringResource(R.string.label_settings_task_state_running)
        TaskState.Succeeded -> stringResource(R.string.label_settings_task_state_succeeded)
        TaskState.Failed -> stringResource(R.string.label_settings_task_state_failed)
        TaskState.Blocked -> stringResource(R.string.label_settings_task_state_blocked)
        TaskState.Cancelled -> stringResource(R.string.label_settings_task_state_cancelled)
    }

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
fun TaskContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    val dummyTaskWorkItems =
        listOf(
            TaskWorkItem(
                id = "Task-1",
                state = TaskState.Enqueued,
                info = "Waiting in queue",
                runAttemptCount = 0,
                tags = "Task-1-tag",
                flexInterval = "10 minutes",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            TaskWorkItem(
                id = "Task-2",
                state = TaskState.Running,
                info = "Processing data...",
                runAttemptCount = 1,
                tags = "Task-2-tag",
                flexInterval = "10 minutes",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            TaskWorkItem(
                id = "Task-3",
                state = TaskState.Blocked,
                info = "Waiting for prerequisites",
                runAttemptCount = 2,
                tags = "Task-3-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            TaskWorkItem(
                id = "Task-4",
                state = TaskState.Succeeded,
                info = "Completed successfully",
                runAttemptCount = 1,
                tags = "Task-4-tag",
                flexInterval = "5",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            TaskWorkItem(
                id = "Task-5",
                state = TaskState.Failed,
                info = "Error: network issue",
                runAttemptCount = 3,
                tags = "Task-5-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
            TaskWorkItem(
                id = "Task-6",
                state = TaskState.Cancelled,
                info = "Cancelled by user",
                runAttemptCount = 1,
                tags = "Task-6-tag",
                flexInterval = "10",
                repeatInterval = "20",
                nextScheduleTime = "Fri 21 Feb",
            ),
        )
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        TaskContent(
            taskWorkItems = dummyTaskWorkItems,
            onCancelWork = {},
        )
    }
}
