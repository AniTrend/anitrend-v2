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

package co.anitrend.task.medialist.scheduler

import android.content.Context
import androidx.work.*
import androidx.work.multiprocess.RemoteWorkManager
import co.anitrend.navigation.work.WorkSchedulerController
import java.util.concurrent.TimeUnit

class MangaSyncScheduler(
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
            worker, 6, TimeUnit.HOURS
        ).setConstraints(
            constrains
        ).build()

        RemoteWorkManager.getInstance(context)
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
        RemoteWorkManager.getInstance(context)
            .cancelUniqueWork(worker.simpleName)
    }
}