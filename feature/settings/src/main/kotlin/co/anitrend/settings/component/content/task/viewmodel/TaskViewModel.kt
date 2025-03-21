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
package co.anitrend.settings.component.content.task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import co.anitrend.core.android.asPrettyTime
import co.anitrend.settings.component.content.task.state.TaskWorkItem
import co.anitrend.settings.component.content.task.state.mapWorkInfoState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class TaskViewModel : ViewModel() {
    @OptIn(ExperimentalTime::class)
    private fun toTaskWorkItem(info: WorkInfo): TaskWorkItem =
        TaskWorkItem(
            id = info.id.toString(),
            state = mapWorkInfoState(info.state),
            info = info.outputData.toString(),
            runAttemptCount = info.runAttemptCount,
            tags = info.tags.joinToString(),
            flexInterval =
                info.periodicityInfo?.let {
                    Duration
                        .convert(
                            value = it.flexIntervalMillis.toDouble(),
                            sourceUnit = DurationUnit.MILLISECONDS,
                            targetUnit = DurationUnit.MINUTES,
                        ).toString()
                },
            repeatInterval =
                info.periodicityInfo?.let {
                    Duration
                        .convert(
                            value = it.repeatIntervalMillis.toDouble(),
                            sourceUnit = DurationUnit.MILLISECONDS,
                            targetUnit = DurationUnit.MINUTES,
                        ).toString()
                },
            nextScheduleTime = Instant.ofEpochMilli(info.nextScheduleTimeMillis).asPrettyTime(),
        )

    operator fun invoke(workManager: WorkManager): Flow<List<TaskWorkItem>> {
        val taskWorkItems =
            workManager
                .getWorkInfosFlow(
                    WorkQuery.fromStates(WorkInfo.State.entries),
                ).map { it.map(::toTaskWorkItem) }
        return taskWorkItems.distinctUntilChanged()
    }
}
