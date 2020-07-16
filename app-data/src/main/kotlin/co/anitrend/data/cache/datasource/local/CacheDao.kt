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

package co.anitrend.data.cache.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.cache.entity.CacheEntity
import co.anitrend.data.cache.model.CacheRequest

@Dao
internal interface CacheDao : ILocalSource<CacheEntity> {
    @Query("""
        delete from cache_log
    """)
    override suspend fun clear()

    @Query("""
        select count(id) from cache_log
    """)
    override suspend fun count(): Int

    @Query("""
        select count(id)
        from cache_log 
        where request = :request and cache_item_id = :itemId
        """)
    suspend fun countMatching(request: CacheRequest, itemId: Long): Int


    @Query("""
        select * 
        from cache_log 
        where request = :request and cache_item_id = :itemId
        """)
    suspend fun getCacheLog(request: CacheRequest, itemId: Long): CacheEntity?
}