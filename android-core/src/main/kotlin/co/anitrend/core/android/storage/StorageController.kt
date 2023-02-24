/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.android.storage

import android.content.Context
import co.anitrend.core.android.storage.contract.IStorageController
import co.anitrend.core.android.storage.enums.StorageType
import co.anitrend.core.android.storage.extensions.toHumanReadableByteValue
import co.anitrend.data.settings.cache.ICacheSettings
import timber.log.Timber
import java.io.File

class StorageController : IStorageController {

    private fun Context.directoryOf(
        storageType: StorageType
    ) = when (storageType) {
        StorageType.CACHE -> externalCacheDir ?: cacheDir
        StorageType.FILES -> getExternalFilesDir(storageType.type) ?: filesDir
        StorageType.MUSIC -> requireNotNull(getExternalFilesDir(storageType.type))
        StorageType.PICTURES -> requireNotNull(getExternalFilesDir(storageType.type))
        StorageType.MOVIES -> requireNotNull(getExternalFilesDir(storageType.type))
    }

    override fun getLogsCache(context: Context): File {
        val external = context.directoryOf(StorageType.FILES)
        val logs = File(external, logsName)
        if (!logs.exists()) logs.mkdirs()
        return logs
    }

    override fun getImageCache(context: Context): File {
        val cache = context.directoryOf(StorageType.CACHE)
        val imageCache = File(cache, imageCacheName)
        if (!imageCache.exists()) imageCache.mkdirs()
        Timber.v("Cache directory for images: ${imageCache.canonicalPath}")
        return imageCache
    }

    override fun getVideoCache(context: Context): File {
        val cache = context.directoryOf(StorageType.CACHE)
        val videoCache = File(cache, videoCacheName)
        if (!videoCache.exists()) videoCache.mkdirs()
        Timber.v("Cache directory for exo player cache: ${videoCache.canonicalPath}")
        return videoCache
    }

    override fun getVideoOfflineCache(context: Context): File {
        val cache = context.directoryOf(StorageType.CACHE)
        val videoOfflineCache = File(cache, videoOfflineCacheName)
        if (!videoOfflineCache.exists()) videoOfflineCache.mkdirs()
        Timber.v("Cache directory for exo player offline sync cache: ${videoOfflineCache.canonicalPath}")
        return videoOfflineCache
    }

    override fun getFreeSpace(context: Context, type: StorageType): Long {
        val cache = context.directoryOf(type)
        return cache.freeSpace
    }

    override fun getStorageUsageLimit(
        context: Context,
        type: StorageType,
        settings: ICacheSettings
    ): Long {
        val freeSpace = getFreeSpace(context, type)
        val ratio = settings.cacheUsageRatio.value
        val limit = (freeSpace * ratio).toLong()
        Timber.v("Storage usage limit -> ratio: $ratio | limit: ${limit.toHumanReadableByteValue()}")
        return limit
    }

    companion object {
        private const val logsName = "logs"
        private const val imageCacheName = "coil_image_cache"
        private const val videoCacheName = "exo_video_cache"
        private const val videoOfflineCacheName = "exo_video_offline_cache"
    }
}