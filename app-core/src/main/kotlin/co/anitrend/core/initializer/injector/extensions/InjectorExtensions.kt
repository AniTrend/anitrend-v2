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

package co.anitrend.core.initializer.injector.extensions

import android.util.Log
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import co.anitrend.core.BuildConfig
import co.anitrend.core.initializer.injector.InjectorInitializer
import co.anitrend.core.initializer.injector.factory.WorkManagerFactory
import co.anitrend.core.initializer.injector.logger.KoinLogger
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.KoinApplication
import org.koin.core.KoinExperimentalAPI


private fun KoinApplication.createWorkManagerFactory() {
    val factory = DelegatingWorkerFactory()
        .apply {
            addFactory(WorkManagerFactory())
        }

    val configuration = Configuration.Builder()
        .setDefaultProcessName("co.anitrend:worker")
        .setWorkerFactory(factory)

    if (BuildConfig.DEBUG)
        configuration.setMinimumLoggingLevel(Log.VERBOSE)
    else configuration.setMinimumLoggingLevel(Log.WARN)

    WorkManager.initialize(koin.get(), configuration.build())
}

/**
 * Setup the KoinWorkerFactory instance
 *
 * @see org.koin.androidx.workmanager.koin.workManagerFactory
 */
@KoinExperimentalAPI
internal fun KoinApplication.workManagerFactory() {
    createWorkManagerFactory()
}

internal fun KoinApplication.koinTimberLogger() {
    logger(KoinLogger())
}

internal fun KoinApplication.defaultProperties() {
    val properties = "koin.properties"
    fileProperties(properties)
}