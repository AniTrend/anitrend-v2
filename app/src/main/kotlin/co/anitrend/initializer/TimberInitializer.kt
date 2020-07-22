package co.anitrend.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import co.anitrend.BuildConfig
import co.anitrend.core.helper.StorageHelper
import co.anitrend.core.initializer.AbstractInitializer
import fr.bipi.tressence.file.FileLoggerTree
import timber.log.Timber
import java.io.IOException

class TimberInitializer : AbstractInitializer<Unit>() {

    @Throws(IOException::class)
    private fun createFileLoggingTree(context: Context): Timber.Tree {
        val loggingFile = StorageHelper.getLogsCache(context)
        return FileLoggerTree.Builder()
            .withFileName("${context.packageName}.log")
            .withDirName(loggingFile.absolutePath)
            .withSizeLimit(logSizeLimit)
            .withFileLimit(1)
            .withMinPriority(logLevel)
            .appendToFile(true)
            .build()
    }

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val fileLoggerTree = createFileLoggingTree(context)
        Timber.plant(fileLoggerTree)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    companion object {
        val logLevel = if (BuildConfig.DEBUG) Log.VERBOSE else Log.WARN
        const val logSizeLimit = 850 * 1024
    }
}