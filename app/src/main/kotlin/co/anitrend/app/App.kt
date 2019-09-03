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

package co.anitrend.app

import android.app.Application
import androidx.work.Configuration
import co.anitrend.app.koin.appModules
import co.anitrend.app.analytics.AnalyticsLogging
import co.anitrend.core.koin.coreModules
import co.anitrend.data.koin.dataModules
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.data.util.Settings
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App: Application(), Configuration.Provider {

    val analyticsUtil by inject<ISupportAnalytics>()
    private val settings by inject<Settings>()

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     * Initializes Koin dependency injection
     */
    private fun initializeKoin() {
        startKoin{
            androidLogger()
            androidContext(
                applicationContext
            )
            modules(
                appModules +
                coreModules +
                dataModules
            )
        }
    }

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    private fun plantLoggingTree() {
        when (BuildConfig.DEBUG) {
            true -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(analyticsUtil as AnalyticsLogging)
        }
    }

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     *
     * Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
     *
     *
     * If you override this method, be sure to call `super.onCreate()`.
     *
     *
     * Be aware that direct boot may also affect callback order on
     * Android [android.os.Build.VERSION_CODES.N] and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such [android.content.ContentProvider], are
     * disabled until user unlock happens, especially when component callback
     * order matters.
     */
    override fun onCreate() {
        super.onCreate()
        initializeKoin()
        plantLoggingTree()
    }

    /**
     * @return The [Configuration] used to initialize WorkManager
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .build()
    }

    /**
     * Applies the correct application theme base on preferences
     */
    fun applyApplicationTheme() {
        /*if (settings.isLightTheme())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)*/
    }
}