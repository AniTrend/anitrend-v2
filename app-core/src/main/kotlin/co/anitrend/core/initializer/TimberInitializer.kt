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
import android.util.Log
import co.anitrend.core.BuildConfig
import co.anitrend.core.initializer.contract.AbstractCoreInitializer
import co.anitrend.data.arch.storage.StorageController
import fr.bipi.tressence.file.FileLoggerTree
import timber.log.Timber
import java.io.IOException

class TimberInitializer : AbstractCoreInitializer<Unit>() {

    @Throws(IOException::class)
    private fun createFileLoggingTree(context: Context): Timber.Tree {
        val loggingFile = StorageController().getLogsCache(context)
        return FileLoggerTree.Builder()
            .withFileName("${context.packageName}.log")
            .withDirName(loggingFile.absolutePath)
            .withSizeLimit(FILE_SIZE_LIMIT)
            .withFileLimit(FILE_CREATION_LIMIT)
            .withMinPriority(MIN_LOG_LEVEL)
            .appendToFile(true)
            .build()
    }

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        Timber.plant(createFileLoggingTree(context))
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    companion object {
        val MIN_LOG_LEVEL = if (BuildConfig.DEBUG) Log.VERBOSE else Log.WARN
        const val FILE_SIZE_LIMIT = 2 * 1024 * 1024
        const val FILE_CREATION_LIMIT = 1
    }
}