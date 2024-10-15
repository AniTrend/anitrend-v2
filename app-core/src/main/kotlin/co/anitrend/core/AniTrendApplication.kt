/*
 * Copyright (C) 2019 AniTrend
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
import co.anitrend.core.crash.runtime.UncaughtExceptionHandler
import co.anitrend.core.extensions.analyticsTree
import co.anitrend.core.extensions.imageLoader
import co.anitrend.core.extensions.themeHelper
import coil.ImageLoader
import coil.ImageLoaderFactory
import timber.log.Timber

abstract class AniTrendApplication : Application(), ImageLoaderFactory {
    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    protected abstract fun initializeKoin()

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Restarts the global koin instance
     */
    abstract fun restartDependencyInjection()

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    protected open fun plantAnalyticsTree() {
        if (!BuildConfig.DEBUG) {
            Timber.plant(analyticsTree())
        }
    }

    /**
     * Applies theme on application instance
     */
    protected open fun applyNightMode() {
        // apply application theme on application instance
        themeHelper().applyDynamicNightModeFromTheme()
    }

    private fun createUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(
            UncaughtExceptionHandler(),
        )
    }

    override fun onCreate() {
        super.onCreate()
        createUncaughtExceptionHandler()
        plantAnalyticsTree()
        applyNightMode()
    }

    /**
     * Return a new [ImageLoader].
     */
    override fun newImageLoader() = imageLoader()
}
