package co.anitrend.settings.component.content.log.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

sealed interface LogUiState {

    @Immutable
    @Stable
    data class LogItem(
        val date: String,
        val time: String,
        val level: Level,
        val message: String,
    ) {
        enum class Level(val identifier: Char) {
            ERROR('E'),
            WARNING('W'),
            INFO('I'),
            DEBUG('D'),
            VERBOSE('V'),
        }
    }

    data object Loading : LogUiState
    data class Error(val message: String) : LogUiState
    data class Success(val logs: List<LogItem>) : LogUiState
}
