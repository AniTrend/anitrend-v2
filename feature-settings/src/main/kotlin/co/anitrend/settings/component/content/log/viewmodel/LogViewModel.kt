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
package co.anitrend.settings.component.content.log.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.core.android.storage.contract.IStorageController
import co.anitrend.settings.component.content.log.state.LogUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LogViewModel(
    private val controller: IStorageController,
    private val dispatchers: ISupportDispatcher,
) : ViewModel() {
    private val logsContentStateFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val mutableLogsStateFlow: MutableStateFlow<LogUiState> = MutableStateFlow(LogUiState.Loading)
    val logState: Flow<LogUiState> = mutableLogsStateFlow

    init {
        viewModelScope.launch(dispatchers.computation) {
            logsContentStateFlow
                .onEach { lines ->
                    val accumulatedLogs = accumulateLogLines(lines)
                    mutableLogsStateFlow.value =
                        LogUiState.Success(
                            accumulatedLogs
                                .map(::createLogItem)
                                .reversed(),
                        )
                }.collect()
        }
    }

    private fun accumulateLogLines(lines: List<String>): List<String> {
        val accumulatedLogs = mutableListOf<String>()
        var currentLog: String? = null

        // Regex to match the expected log entry pattern (e.g., "03-16 20:57:35:465")
        val logEntryRegex = Regex("""^\d{2}-\d{2}\s+\d{2}:\d{2}:\d{2}:\d{3}""")

        lines.forEach { line ->
            if (logEntryRegex.containsMatchIn(line)) {
                // New log entry found
                currentLog?.let { accumulatedLogs.add(it) }
                currentLog = line
            } else {
                // Continuation of the previous log entry
                currentLog = (currentLog ?: "") + "\n" + line
            }
        }
        currentLog?.let { accumulatedLogs.add(it) }
        return accumulatedLogs
    }

    /**
     * Extracts the log from a log line based on the expected format:
     * "03-07 21:12:01:802 D/[Koin](2) : ..."
     */
    private fun createLogItem(log: String): LogUiState.LogItem {
        val tokens = log.split(" ").take(3)
        val levelEntries = LogUiState.LogItem.Level.entries
        return LogUiState.LogItem(
            date = tokens[DATE_INDEX],
            time = tokens[TIME_INDEX],
            level =
                levelEntries.find {
                    it.identifier == tokens[MESSAGE_INDEX].firstOrNull()
                } ?: LogUiState.LogItem.Level.VERBOSE,
            message = log,
        )
    }

    fun getLogs(context: Context) {
        val fileName = "${context.packageName}.log"
        val logsDirectory = controller.getLogsCache(context)
        viewModelScope.launch(dispatchers.io) {
            val logs = logsDirectory.resolve(fileName)
            if (logs.exists()) {
                logs.useLines { lines ->
                    logsContentStateFlow.value = lines.toList()
                }
            } else {
                mutableLogsStateFlow.value = LogUiState.Error("No logs found")
            }
        }
    }

    private companion object {
        private const val DATE_INDEX = 0
        private const val TIME_INDEX = 1
        private const val MESSAGE_INDEX = 2
    }
}
