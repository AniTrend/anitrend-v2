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

package co.anitrend.data.medialist.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.entity.view.MediaListEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaListLocalSource : ILocalSource<MediaListEntity> {

    /**
     * Count the number of entities
     */
    @Query("""
            select count(id) from media_list
        """)
    abstract override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query("""
        delete from media_list
        """)
    abstract override suspend fun clear()

    @Query("""
        delete from media_list
        where user_id = :userId
        """)
    abstract suspend fun clearByUserId(userId: Long)

    @Transaction
    @Query("""
        select ml.* from media_list ml
        join user u on u.id = ml.user_id
        where ml.id = :id and u.id = :userId
    """)
    abstract fun byIdFlow(id: Long, userId: Long): Flow<MediaListEntityView.Core?>

    @Transaction
    @Query("""
        select ml.* from media_list ml
        join user u on u.id = ml.user_id
        where ml.media_id = :mediaId and u.id = :userId
    """)
    abstract fun byMediaIdFlow(mediaId: Long, userId: Long): Flow<MediaListEntityView.WithMedia?>


    @Query("""
        select ml.* from media_list ml
        join user u on u.id = ml.user_id
        where ml.id = :id and u.id = :userId
    """)
    @Transaction
    abstract fun byIdCoreFlow(id: Long, userId: Long): Flow<MediaListEntityView.Core?>

    @Query("""
        select ml.* from media_list ml
        join user u on u.id = ml.user_id
        where u.id = :userId
    """)
    @Transaction
    abstract fun entryFactory(userId: Long): DataSource.Factory<Int, MediaListEntityView.WithMedia>
}