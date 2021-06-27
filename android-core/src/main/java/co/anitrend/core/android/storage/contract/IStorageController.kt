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

package co.anitrend.core.android.storage.contract

import android.content.Context
import co.anitrend.data.settings.cache.ICacheSettings
import co.anitrend.core.android.storage.enums.StorageType
import java.io.File

interface IStorageController {
    fun getLogsCache(context: Context): File
    fun getImageCache(context: Context): File
    fun getVideoCache(context: Context): File
    fun getVideoOfflineCache(context: Context): File
    fun getFreeSpace(context: Context, type: StorageType): Long
    fun getStorageUsageLimit(context: Context, type: StorageType, settings: ICacheSettings): Long
}