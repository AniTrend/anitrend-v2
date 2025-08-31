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
