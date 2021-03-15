/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.task.user.scheduler

import android.content.Context
import androidx.work.*
import co.anitrend.navigation.UserTaskRouter
import co.anitrend.navigation.work.WorkSchedulerController
import co.anitrend.task.user.initializer.WorkSchedulerInitializer
import java.util.concurrent.TimeUnit

class UserAccountScheduler(
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
            worker, 30, TimeUnit.MINUTES
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