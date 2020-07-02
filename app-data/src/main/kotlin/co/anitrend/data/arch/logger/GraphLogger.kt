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

package co.anitrend.data.arch.logger

import io.github.wax911.library.logger.contract.ILogger.Level
import io.github.wax911.library.logger.core.AbstractLogger
import timber.log.Timber

/**
 * Logs to timber
 */
internal class GraphLogger(level: Level) : AbstractLogger(level) {
    /**
     * Write a log message to its destination.
     *
     * @param level Filter used to determine the verbosity level of logs.
     * @param tag Used to identify the source of a log message. It usually
     * identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @param throwable An exception to log
     */
    override fun log(level: Level, tag: String, message: String, throwable: Throwable?) {
        when (level) {
            Level.VERBOSE -> Timber.tag(tag).v(throwable, message)
            Level.DEBUG -> Timber.tag(tag).d(throwable, message)
            Level.INFO -> Timber.tag(tag).i(throwable, message)
            Level.WARNING -> Timber.tag(tag).w(throwable, message)
            Level.ERROR -> Timber.tag(tag).e(throwable, message)
            Level.NONE -> { /** no logging */ }
        }
    }
}