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
package co.anitrend.data.media.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.stats.MediaScoreDistributionEntity
import co.anitrend.data.media.entity.stats.MediaStatusDistributionEntity
import co.anitrend.data.media.entity.view.MediaStatsEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class MediaStatsLocalSource : AbstractLocalSource<MediaStatsEntity>() {
    @Query(
        """
        delete from media_score_distribution
        """,
    )
    protected abstract suspend fun clearScoreDistributions()

    @Query(
        """
        delete from media_status_distribution
        """,
    )
    protected abstract suspend fun clearStatusDistributions()

    @Query(
        """
        delete from media_score_distribution
        where media_id = :mediaId
        """,
    )
    protected abstract suspend fun clearScoreDistributionsByMediaId(mediaId: Long)

    @Query(
        """
        delete from media_status_distribution
        where media_id = :mediaId
        """,
    )
    protected abstract suspend fun clearStatusDistributionsByMediaId(mediaId: Long)

    @Query(
        """
        select count(media_id) from media_stats
        """,
    )
    abstract override suspend fun count(): Int

    @Query("delete from media_stats")
    protected abstract suspend fun clearStats()

    @Transaction
    override suspend fun clear() {
        clearScoreDistributions()
        clearStatusDistributions()
        clearStats()
    }

    @Query(
        """
        select * from media_stats
        where media_id = :mediaId
        limit 1
        """,
    )
    @Transaction
    abstract fun entryByMediaIdFlow(mediaId: Long): Flow<MediaStatsEntityView?>

    @Query("delete from media_stats where media_id = :mediaId")
    protected abstract suspend fun clearStatsByMediaId(mediaId: Long)

    @Transaction
    open suspend fun clearByMediaId(mediaId: Long) {
        clearScoreDistributionsByMediaId(mediaId)
        clearStatusDistributionsByMediaId(mediaId)
        clearStatsByMediaId(mediaId)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertScoreDistributions(attribute: List<MediaScoreDistributionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsertStatusDistributions(attribute: List<MediaStatusDistributionEntity>)
}
