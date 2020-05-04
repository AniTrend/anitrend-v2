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

package co.anitrend.core.helper

import android.content.Context
import co.anitrend.core.settings.common.cache.ICacheSettings
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

/**
 * Storage utility cache
 */
object StorageHelper {

    private val moduleTag = StorageHelper::class.java.simpleName
    private const val logsName = "logs"
    private const val imageCacheName = "coil_image_cache"
    private const val videoCacheName = "exo_video_cache"
    private const val videoOfflineCacheName = "exo_video_offline_cache"

    private fun cacheDirectory(context: Context) =
        context.externalCacheDir ?: context.cacheDir


    fun getLogsCache(context: Context): File {
        val cache = cacheDirectory(context)
        val logs = File(cache, logsName)
        if (!logs.exists()) logs.mkdirs()
        Timber.tag(moduleTag).v(
            "Directory that will be used for logs: ${logs.canonicalPath}"
        )
        return logs
    }

    fun getImageCache(context: Context): File {
        val cache = cacheDirectory(context)
        val imageCache = File(cache, imageCacheName)
        if (!imageCache.exists()) imageCache.mkdirs()
        Timber.tag(moduleTag).v(
            "Cache that will be used for images: ${imageCache.canonicalPath}"
        )
        return imageCache
    }

    fun getVideoCache(context: Context): File {
        val cache = cacheDirectory(context)
        val videoCache = File(cache, videoCacheName)
        if (!videoCache.exists()) videoCache.mkdirs()
        Timber.tag(moduleTag).v(
            "Cache that will be used for videos: ${videoCache.canonicalPath}"
        )
        return videoCache
    }

    fun getVideoOfflineCache(context: Context): File {
        val cache = cacheDirectory(context)
        val videoOfflineCache = File(cache, videoOfflineCacheName)
        if (!videoOfflineCache.exists()) videoOfflineCache.mkdirs()
        Timber.tag(moduleTag).v(
            "Cache that will be used for offline videos: ${videoOfflineCache.canonicalPath}"
        )
        return videoOfflineCache
    }

    fun getFreeSpace(context: Context): Long {
        val cache = cacheDirectory(context)
        return cache.freeSpace
    }

    fun getStorageUsageLimit(context: Context, settings: ICacheSettings): Long {
        val freeSpace = getFreeSpace(context)
        val ratio = settings.usageRatio
        val limit = (freeSpace * ratio).toLong()
        Timber.tag(moduleTag).v(
            "Storage usage limit -> ratio: $ratio | limit: ${limit.toHumanReadableByteValue()}"
        )
        return limit
    }

    fun Float.toHumanReadableByteValue(si: Boolean = false): String {
        val bytes = this
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return "$bytes B"
        val exp =
            (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre =
            (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
        return String.format(
            Locale.getDefault(), "%.1f %sB",
            bytes / unit.toDouble().pow(exp.toDouble()), pre
        )
    }

    fun Long.toHumanReadableByteValue(si: Boolean = false): String {
        val bytes = this
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return "$bytes B"
        val exp =
            (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
        val pre =
            (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
        return String.format(
            Locale.getDefault(), "%.1f %sB",
            bytes / unit.toDouble().pow(exp.toDouble()), pre
        )
    }
}