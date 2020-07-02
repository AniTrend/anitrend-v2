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
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.core.util.theme.ThemeHelper
import coil.ImageLoader
import coil.ImageLoaderFactory
import org.koin.android.ext.android.get
import timber.log.Timber

abstract class AniTrendApplication : Application(), ImageLoaderFactory {

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Restarts the global koin instance
     */
    abstract fun restartDependencyInjection()

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    protected open fun plantAnalyticsTree() {
        if (!BuildConfig.DEBUG)
            Timber.plant(get<ISupportAnalytics>() as Timber.Tree)
    }

    /**
     * Applies theme on application instance
     */
    protected open fun applyNightMode() {
        // apply application theme on application instance'
        get<ThemeHelper>().applyDynamicNightModeFromTheme()
    }

    /**
     * Checks if the application needs to perform any migrations
     */
    protected open fun checkApplicationMigration() {
        //Settings(this).sharedPreferences.edit(commit = true) { clear() }
    }

    override fun onCreate() {
        super.onCreate()
        checkApplicationMigration()
        plantAnalyticsTree()
        applyNightMode()
    }

    /**
     * Return a new [ImageLoader].
     */
    override fun newImageLoader() =  get<ImageLoader>()
}