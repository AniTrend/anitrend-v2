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

package co.anitrend.data.android.cache.datasource

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.cache.entity.CacheEntity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.source.AbstractLocalSource
import org.threeten.bp.Instant

@Dao
abstract class CacheLocalSource : AbstractLocalSource<CacheEntity>() {

    @Query("""
        delete from cache_log
    """)
    abstract override suspend fun clear()

    @Query("""
        select count(id) from cache_log
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from cache_log
        where request = :request
    """)
    abstract suspend fun clearByType(request: CacheRequest)

    @Query("""
        select count(id)
        from cache_log 
        where request = :request and cache_item_id = :itemId
        """)
    abstract suspend fun countMatching(request: CacheRequest, itemId: Long): Int

    @Query("""
        select * 
        from cache_log 
        where request = :request and cache_item_id = :itemId
        """)
    abstract suspend fun getCacheLog(request: CacheRequest, itemId: Long): CacheEntity?

    @Query("""
        select id
        from cache_log 
        where request = :request and cache_item_id = :itemId
        """)
    abstract suspend fun getCacheLogId(request: CacheRequest, itemId: Long): Long?


    @Query("""
        update cache_log
        set timestamp = :timeStamp
        where id = :id
        """)
    abstract suspend fun partialUpdate(id: Long, timeStamp: Instant)

    @Transaction
    open suspend fun insertOrUpdate(entity: CacheEntity) {
        val id = getCacheLogId(entity.request, entity.cacheItemId)
        if (id == null)
            insert(entity)
        else partialUpdate(id, entity.timestamp)
    }
}