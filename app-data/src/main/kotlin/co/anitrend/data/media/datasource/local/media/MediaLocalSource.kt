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

package co.anitrend.data.media.datasource.local.media

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.embedded.MediaEntityWithAiring
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaLocalSource : ILocalSource<MediaEntity> {

    /**
     * Count the number of entities
     */
    @Query("""
            select count(id) from media
        """)
    abstract override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query("""
        delete from media
        """)
    abstract override suspend fun clear()

    @Query("""
        select * from media
        where id = :id
    """)
    abstract suspend fun mediaById(id: Long): MediaEntity?

    @Query("""
        select * from media
        where id = :id
    """)
    @Transaction
    abstract suspend fun mediaByIdWithAiring(
        id: Long
    ): MediaEntityWithAiring?

    @Query("""
        select * from media
        where id = :id
    """)
    @Transaction
    abstract fun mediaByIdWithAiringFlow(
        id: Long
    ): Flow<MediaEntityWithAiring>

    @Query("""
        select * from media
        where id = :id
    """)
    abstract fun mediaByIdFlow(id: Long): Flow<MediaEntity>

    @Query("""
        select * from media
    """)
    abstract fun allMediaFactory(): DataSource.Factory<Int, MediaEntity>
}