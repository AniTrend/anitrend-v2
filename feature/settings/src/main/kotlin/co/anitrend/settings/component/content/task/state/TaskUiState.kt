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
package co.anitrend.settings.component.content.task.state

import androidx.compose.runtime.Immutable
import androidx.work.WorkInfo

enum class TaskState {
    Enqueued,
    Running,
    Succeeded,
    Failed,
    Blocked,
    Cancelled,
}

@Immutable
data class TaskWorkItem(
    val id: String,
    val state: TaskState,
    val info: String = "",
    val runAttemptCount: Int = 0,
    val tags: String,
    val flexInterval: String?,
    val repeatInterval: String?,
    val nextScheduleTime: String,
)

// Mapping function to convert WorkInfo.State into our UI model.
fun mapWorkInfoState(state: WorkInfo.State): TaskState =
    when (state) {
        WorkInfo.State.ENQUEUED -> TaskState.Enqueued
        WorkInfo.State.RUNNING -> TaskState.Running
        WorkInfo.State.SUCCEEDED -> TaskState.Succeeded
        WorkInfo.State.FAILED -> TaskState.Failed
        WorkInfo.State.BLOCKED -> TaskState.Blocked
        WorkInfo.State.CANCELLED -> TaskState.Cancelled
    }
