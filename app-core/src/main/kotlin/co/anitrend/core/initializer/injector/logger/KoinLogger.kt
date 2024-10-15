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
package co.anitrend.core.initializer.injector.logger

import co.anitrend.core.BuildConfig
import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

internal class KoinLogger(
    logLevel: Level = if (BuildConfig.DEBUG) Level.DEBUG else Level.WARNING,
) : Logger(logLevel) {
    override fun display(
        level: Level,
        msg: MESSAGE,
    ) {
        when (level) {
            Level.DEBUG -> Timber.tag(KOIN_TAG).d(msg)
            Level.INFO -> Timber.tag(KOIN_TAG).i(msg)
            Level.ERROR -> Timber.tag(KOIN_TAG).e(msg)
            Level.WARNING -> Timber.tag(KOIN_TAG).w(msg)
            Level.NONE -> { /* logging disabled */ }
        }
    }
}
