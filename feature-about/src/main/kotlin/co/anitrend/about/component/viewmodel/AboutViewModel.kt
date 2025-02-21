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
package co.anitrend.about.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import co.anitrend.about.component.compose.state.WorkItem
import co.anitrend.about.component.compose.state.mapWorkInfoState
import co.anitrend.core.android.asPrettyTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.threeten.bp.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

internal class AboutViewModel : ViewModel() {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(workManager: WorkManager): Flow<List<WorkItem>> {
        val workItems = workManager.getWorkInfosFlow(
            WorkQuery.fromStates(WorkInfo.State.entries)
        ).map { workInfos ->
            workInfos.map { info ->
                WorkItem(
                    id = info.id.toString(),
                    state = mapWorkInfoState(info.state),
                    info = info.outputData.toString(),
                    runAttemptCount = info.runAttemptCount,
                    tags = info.tags.joinToString(),
                    flexInterval = info.periodicityInfo?.let {
                        Duration.convert(
                            value = it.flexIntervalMillis.toDouble(),
                            sourceUnit = DurationUnit.MILLISECONDS,
                            targetUnit = DurationUnit.MINUTES
                        ).toString()
                                                                   },
                    repeatInterval = info.periodicityInfo?.let {
                        Duration.convert(
                            value = it.repeatIntervalMillis.toDouble(),
                            sourceUnit = DurationUnit.MILLISECONDS,
                            targetUnit = DurationUnit.MINUTES
                        ).toString()
                                                                     },
                    nextScheduleTime = Instant.ofEpochMilli(info.nextScheduleTimeMillis).asPrettyTime(),
                )
            }
        }.distinctUntilChanged()
        return workItems
    }
}
