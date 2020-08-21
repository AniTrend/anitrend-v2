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

package co.anitrend.data.media.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MediaLocalSource : ILocalSource<MediaEntity> {

    /**
     * Count the number of entities
     */
    @Query("""
            select count(id) from media_entity
        """)
    override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query("""
        delete from media_entity
        """)
    override suspend fun clear()

    @Query("""
        select * from media_entity
        where id = :id
    """)
    fun getMediaByIdFlow(id: Long): Flow<MediaEntity>

    @Query("""
        select * from media_entity
    """)
    fun getAllMediaFactory(): DataSource.Factory<Int, MediaEntity>
}