package co.anitrend.task.config.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import co.anitrend.navigation.work.WorkSchedulerController
import java.util.concurrent.TimeUnit

class ConfigScheduler(
    override val worker: Class<out ListenableWorker>
) : WorkSchedulerController() {
    /**
     * Schedule a unit of work
     */
    override fun schedule(context: Context) {
        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val workRequest = PeriodicWorkRequest.Builder(
            worker, 15, TimeUnit.MINUTES
        ).setConstraints(
            constrains
        ).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                worker.simpleName,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    /**
     * Cancel a unit of work
     */
    override fun cancel(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(worker.simpleName)
    }
}
