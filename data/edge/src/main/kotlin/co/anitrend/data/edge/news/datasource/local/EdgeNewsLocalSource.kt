/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.news.datasource.local

import androidx.paging.PagingSource
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.news.entity.EdgeNewsEntity

@Dao
abstract class EdgeNewsLocalSource : AbstractLocalSource<EdgeNewsEntity>() {
    @Query("select count(id) from edge_news")
    abstract override suspend fun count(): Int

    @Query("delete from edge_news")
    abstract override suspend fun clear()

    @RawQuery(observedEntities = [EdgeNewsEntity::class])
    @Transaction
    abstract fun rawFactory(query: SupportSQLiteQuery): DataSource.Factory<Int, EdgeNewsEntity>

    @Query(
        """
        select * from edge_news
        order by published_at desc
        """,
    )
    abstract fun entryFactory(): DataSource.Factory<Int, EdgeNewsEntity>
}
