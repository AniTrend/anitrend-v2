/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.review.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.entity.view.ReviewEntityView
import co.anitrend.data.user.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class ReviewLocalSource : AbstractLocalSource<ReviewEntity>() {
    @Query(
        """
        select count(id) from review
    """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from review
    """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        delete from review
        where id = :id
        """,
    )
    abstract suspend fun clearById(id: Long)

    @Transaction
    @RawQuery(observedEntities = [MediaEntity::class, UserEntity::class, ReviewEntity::class])
    abstract fun rawFlow(query: SupportSQLiteQuery): Flow<ReviewEntityView.Core?>

    @RawQuery(observedEntities = [MediaEntity::class, UserEntity::class, ReviewEntity::class])
    abstract fun rawPagingSource(query: SupportSQLiteQuery): PagingSource<Int, ReviewEntityView.Core>

    @Query(
        """
        SELECT * FROM review
        WHERE user_id = :userId
        ORDER BY created_at DESC
        """,
    )
    abstract fun entryByUserIdFlow(userId: Long): Flow<List<ReviewEntity>>
}
