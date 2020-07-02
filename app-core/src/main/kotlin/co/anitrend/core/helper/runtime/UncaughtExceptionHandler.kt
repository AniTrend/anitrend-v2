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

package co.anitrend.core.helper.runtime

import timber.log.Timber

/**
 * An uncaught exception handler for the application
 */
class UncaughtExceptionHandler(
    private val exceptionHandler: Thread.UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()
) : Thread.UncaughtExceptionHandler {

    /**
     * Method invoked when the given thread terminates due to the given uncaught exception.
     *
     * Any exception thrown by this method will be ignored by the Java Virtual Machine.
     *
     * @param thread the thread
     * @param throwable the exception
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        Timber.tag(TAG).e(throwable, thread.name)
        exceptionHandler?.uncaughtException(thread, throwable)
    }

    companion object {
        private val TAG =
            UncaughtExceptionHandler::class.java.simpleName
    }
}