/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.core.initializer

import android.content.Context
import androidx.startup.Initializer
import co.anitrend.core.BuildConfig
import co.anitrend.core.initializer.contract.AbstractInitializer
import co.anitrend.core.koin.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class InjectorInitializer : AbstractInitializer<Unit>() {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val logLevel = if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR
        startKoin {
            androidContext(context)
            logger(KoinLogger(logLevel))
            workManagerFactory()
            fragmentFactory()
            modules(coreModules)
        }
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another
     * [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> =
            listOf(TimberInitializer::class.java)

    internal class KoinLogger(
        logLevel: Level
    ) : Logger(logLevel) {
        override fun log(level: Level, msg: MESSAGE) {
            when (level) {
                Level.DEBUG -> Timber.tag(KOIN_TAG).v(msg)
                Level.INFO -> Timber.tag(KOIN_TAG).i(msg)
                Level.ERROR -> Timber.tag(KOIN_TAG).e(msg)
                Level.NONE -> { /* logging disabled */ }
            }
        }
    }
}