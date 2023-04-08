package co.anitrend.task.config.component

import android.content.Context
import androidx.work.WorkerParameters
import co.anitrend.arch.core.worker.SupportCoroutineWorker
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.data.edge.config.GetConfigInteractor
import kotlinx.coroutines.flow.first

class ConfigWorker(
    context: Context,
    parameters: WorkerParameters,
    private val interactor: GetConfigInteractor
) : SupportCoroutineWorker(context, parameters) {

    /**
     * A suspending method to do your work.  This function runs on the coroutine context specified
     * by [coroutineContext].
     *
     * A CoroutineWorker is given a maximum of ten minutes to finish its execution and return a
     * [androidx.work.ListenableWorker.Result].  After this time has expired, the worker will be signalled to
     * stop.
     *
     * @return The [androidx.work.ListenableWorker.Result] of the result of the background work; note that
     * dependent work will not execute if you return [androidx.work.ListenableWorker.Result.failure]
     */
    override suspend fun doWork(): Result {
        val dataState = interactor()

        val loadState = dataState.loadState.first { state ->
            state is LoadState.Success || state is LoadState.Error
        }

        return if (loadState is LoadState.Success)
            Result.success()
        else Result.failure()
    }
}
