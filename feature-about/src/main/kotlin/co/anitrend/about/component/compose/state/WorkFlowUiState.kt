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
package co.anitrend.about.component.compose.state

import androidx.compose.runtime.Immutable
import androidx.work.WorkInfo

enum class WorkState {
    Enqueued,
    Running,
    Succeeded,
    Failed,
    Blocked,
    Cancelled,
}

@Immutable
data class WorkItem(
    val id: String,
    val state: WorkState,
    val info: String = "",
    val runAttemptCount: Int = 0,
    val tags: String,
    val flexInterval: String?,
    val repeatInterval: String?,
    val nextScheduleTime: String,
)

// Mapping function to convert WorkInfo.State into our UI model.
fun mapWorkInfoState(state: WorkInfo.State): WorkState =
    when (state) {
        WorkInfo.State.ENQUEUED -> WorkState.Enqueued
        WorkInfo.State.RUNNING -> WorkState.Running
        WorkInfo.State.SUCCEEDED -> WorkState.Succeeded
        WorkInfo.State.FAILED -> WorkState.Failed
        WorkInfo.State.BLOCKED -> WorkState.Blocked
        WorkInfo.State.CANCELLED -> WorkState.Cancelled
    }
