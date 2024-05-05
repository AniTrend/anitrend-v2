/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.core.initializer.injector.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import timber.log.Timber

/**
 * Custom application worker factory
 */
internal class WorkManagerFactory : WorkerFactory(), KoinComponent {
    private fun resolveDependency(
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker =
        get(
            qualifier = named(workerClassName),
        ) { parametersOf(workerParameters) }

    /**
     * Override this method to implement your custom worker-creation logic.  Use
     * [Configuration.Builder.setWorkerFactory] to use your custom class.
     *
     *
     * Throwing an [Exception] here will crash the application. If a [WorkerFactory]
     * is unable to create an instance of the [ListenableWorker], it should return `null` so it can delegate to the default [WorkerFactory].
     *
     *
     * Returns a new instance of the specified `workerClassName` given the arguments.  The
     * returned worker must be a newly-created instance and must not have been previously returned
     * or invoked by WorkManager. Otherwise, WorkManager will throw an
     * [IllegalStateException].
     *
     * @param appContext The application context
     * @param workerClassName The class name of the worker to create
     * @param workerParameters Parameters for worker initialization
     * @return A new [ListenableWorker] instance of type `workerClassName`, or
     * `null` if the worker could not be created
     */
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return runCatching {
            Timber.d("Resolving requested worker: $workerClassName")
            resolveDependency(workerClassName, workerParameters)
        }.onFailure {
            Timber.w(it, "Unable to resolve worker: $workerClassName")
        }.getOrNull()
    }
}
