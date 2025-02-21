package co.anitrend.about.component.compose.state

import androidx.compose.runtime.Immutable
import androidx.work.WorkInfo

enum class WorkState {
    Enqueued, Running, Succeeded, Failed, Blocked, Cancelled
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
fun mapWorkInfoState(state: WorkInfo.State): WorkState = when (state) {
    WorkInfo.State.ENQUEUED -> WorkState.Enqueued
    WorkInfo.State.RUNNING -> WorkState.Running
    WorkInfo.State.SUCCEEDED -> WorkState.Succeeded
    WorkInfo.State.FAILED -> WorkState.Failed
    WorkInfo.State.BLOCKED -> WorkState.Blocked
    WorkInfo.State.CANCELLED -> WorkState.Cancelled
}
