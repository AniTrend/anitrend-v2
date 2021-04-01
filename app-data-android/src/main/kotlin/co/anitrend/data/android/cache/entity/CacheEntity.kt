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

package co.anitrend.data.android.cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.android.cache.model.CacheRequest
import org.threeten.bp.Instant

@Entity(
    tableName = "cache_log",
    indices = [
        Index(
            value = ["cache_item_id"]
        )
    ]
)
data class CacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "request") val request: CacheRequest,
    @ColumnInfo(name = "cache_item_id") val cacheItemId: Long,
    @ColumnInfo(name = "timestamp") val timestamp: Instant
) {
    companion object {
        fun new(
            request: CacheRequest,
            cacheItemId: Long
        ): CacheEntity = CacheEntity(
            request = request,
            cacheItemId = cacheItemId,
            timestamp = Instant.now()
        )
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString() =
        "CacheEntity(id: $id, request: $request, cacheItemId: $cacheItemId, timestamp: $timestamp)"
}