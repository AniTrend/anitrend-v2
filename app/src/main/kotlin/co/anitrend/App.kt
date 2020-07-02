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

package co.anitrend

import co.anitrend.core.AniTrendApplication
import co.anitrend.core.helper.runtime.UncaughtExceptionHandler
import org.koin.core.context.stopKoin

class App : AniTrendApplication() {

    private fun createUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(
            UncaughtExceptionHandler()
        )
    }

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Restarts the global koin instance
     */
    override fun restartDependencyInjection() {
        stopKoin()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            createUncaughtExceptionHandler()
    }
}