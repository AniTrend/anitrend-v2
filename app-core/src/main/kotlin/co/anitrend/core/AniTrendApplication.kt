/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.core

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import co.anitrend.core.analytics.AnalyticsLogger
import co.anitrend.core.extensions.analytics
import co.anitrend.core.extensions.koinOf
import co.anitrend.core.settings.Settings
import co.anitrend.core.util.theme.ThemeUtil
import timber.log.Timber

abstract class AniTrendApplication : Application(), Configuration.Provider {

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    protected abstract fun initializeDependencyInjection()

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Restarts the global koin instance
     */
    abstract fun restartDependencyInjection()

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    protected open fun plantLoggingTree() {
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(analytics as AnalyticsLogger)
        }
    }

    override fun onCreate() {
        super.onCreate()
        // clearing preference for migration purposes
        //Settings(this).sharedPreferences.edit(commit = true) { clear() }
        initializeDependencyInjection()
        plantLoggingTree()
        // apply application theme on application instance
        ThemeUtil(
            settings = koinOf<Settings>()
        ).applyNightMode()
    }

    /**
     * @return The [Configuration] used to initialize WorkManager
     */
    override fun getWorkManagerConfiguration(): Configuration {
        val logLevel = when (BuildConfig.DEBUG) {
            true -> Log.VERBOSE
            else -> Log.ERROR
        }
        return Configuration.Builder()
            .setMinimumLoggingLevel(logLevel)
            .build()
    }
}