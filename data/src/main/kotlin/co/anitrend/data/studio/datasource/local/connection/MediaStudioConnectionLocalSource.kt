/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.studio.datasource.local.connection

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaStudioConnectionLocalSource : AbstractLocalSource<MediaStudioConnectionEntity>() {
    @Query(
        """
        select count(id) from media_studio_connection
        """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from media_studio_connection
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        select * from media_studio_connection
        where media_id = :mediaId
        order by sort_index asc
        """,
    )
    abstract fun entriesByMediaIdFlow(mediaId: Long): Flow<List<MediaStudioConnectionEntity>>

    @Query(
        """
        delete from media_studio_connection
        where media_id = :mediaId
        """,
    )
    abstract suspend fun clearByMediaId(mediaId: Long)
}
