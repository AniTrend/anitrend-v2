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
package co.anitrend.data.recommendation.datasource.local.connection

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaRecommendationConnectionLocalSource : AbstractLocalSource<MediaRecommendationConnectionEntity>() {
    @Query(
        """
        select count(id) from media_recommendation_connection
        """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from media_recommendation_connection
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        select * from media_recommendation_connection
        where media_id = :mediaId
        order by sort_index asc
        """,
    )
    abstract fun entriesByMediaIdFlow(mediaId: Long): Flow<List<MediaRecommendationConnectionEntity>>

    @Query(
        """
        select * from media_recommendation_connection
        where media_id = :mediaId
        order by sort_index asc
        """,
    )
    abstract fun entriesByMediaIdPagingSource(mediaId: Long): PagingSource<Int, MediaRecommendationConnectionEntity>

    @Query(
        """
        select count(id) from media_recommendation_connection
        where media_id = :mediaId
        """,
    )
    abstract suspend fun countByMediaId(mediaId: Long): Int

    @Query(
        """
        select max(sort_index) from media_recommendation_connection
        where media_id = :mediaId
        """,
    )
    abstract suspend fun maxSortIndexByMediaId(mediaId: Long): Int?

    @Query(
        """
        delete from media_recommendation_connection
        where media_id = :mediaId
        """,
    )
    abstract suspend fun clearByMediaId(mediaId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertConnections(attribute: List<MediaRecommendationConnectionEntity>)
}
