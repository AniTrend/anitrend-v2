/*
 * Copyright (C) 2020 AniTrend
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
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.medialist.entity.MediaListEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaLocalSource : AbstractLocalSource<MediaEntity>() {
    /**
     * Count the number of entities
     */
    @Query(
        """
            select count(id) from media
        """,
    )
    abstract override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query(
        """
        delete from media
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        delete from media
        where id = :id
        """,
    )
    abstract suspend fun clearById(id: Long)

    @Query(
        """
        select * from media
        where id = :id
    """,
    )
    @Transaction
    abstract fun mediaByIdFlow(id: Long): Flow<MediaEntityView.Extended?>

    @Transaction
    @RawQuery(observedEntities = [MediaEntity::class, MediaListEntity::class, AiringScheduleEntity::class])
    abstract fun rawFlow(query: SupportSQLiteQuery): Flow<MediaEntityView.Core?>

    @Transaction
    @RawQuery(observedEntities = [MediaEntity::class, MediaListEntity::class, AiringScheduleEntity::class])
    abstract fun rawFactory(query: SupportSQLiteQuery): DataSource.Factory<Int, MediaEntityView.Core>

    @Query(
        """
        select * from media_character_connection
        where media_id = :mediaId
        order by sort_index asc
    """,
    )
    abstract fun mediaCharactersFactory(mediaId: Long): DataSource.Factory<Int, MediaCharacterConnectionEntity>

    @Query(
        """
        select * from media_character_connection
        where media_id = :mediaId
        order by sort_index asc
        limit :limit offset :offset
    """,
    )
    abstract suspend fun mediaCharactersByPage(
        mediaId: Long,
        limit: Int,
        offset: Int,
    ): List<MediaCharacterConnectionEntity>

    @Query(
        """
        select * from media_staff_connection
        where media_id = :mediaId
        order by sort_index asc
    """,
    )
    abstract fun mediaStaffFactory(mediaId: Long): DataSource.Factory<Int, MediaStaffConnectionEntity>

    @Query(
        """
        select * from media_staff_connection
        where media_id = :mediaId
        order by sort_index asc
        limit :limit offset :offset
    """,
    )
    abstract suspend fun mediaStaffByPage(
        mediaId: Long,
        limit: Int,
        offset: Int,
    ): List<MediaStaffConnectionEntity>

    @Query(
        """
        select count(id) from media_character_connection
        where media_id = :mediaId
    """,
    )
    abstract suspend fun mediaCharactersCount(mediaId: Long): Int

    @Query(
        """
        select count(id) from media_staff_connection
        where media_id = :mediaId
    """,
    )
    abstract suspend fun mediaStaffCount(mediaId: Long): Int

    @Query(
        """
        delete from media_character_connection
        where media_id = :mediaId
    """,
    )
    abstract suspend fun clearMediaCharactersByMediaId(mediaId: Long)

    @Query(
        """
        delete from media_staff_connection
        where media_id = :mediaId
    """,
    )
    abstract suspend fun clearMediaStaffByMediaId(mediaId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertMediaCharacters(attribute: List<MediaCharacterConnectionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertMediaStaff(attribute: List<MediaStaffConnectionEntity>)
}
