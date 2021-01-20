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

package co.anitrend.task.user.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import co.anitrend.core.initializer.contract.AbstractFeatureInitializer
import co.anitrend.navigation.UserTaskRouter
import co.anitrend.navigation.UserTaskRouter.Provider.Companion.forAccountSyncWorker
import java.util.concurrent.TimeUnit

class WorkSchedulerInitializer : AbstractFeatureInitializer<Unit>() {

    private fun scheduleAccountSyncWorker(context: Context) {
        val worker = UserTaskRouter.forAccountSyncWorker()

        val workRequest = PeriodicWorkRequest.Builder(
            worker, 15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                worker.simpleName,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        scheduleAccountSyncWorker(context)
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after koin has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return super.dependencies() + listOf(FeatureInitializer::class.java)
    }
}