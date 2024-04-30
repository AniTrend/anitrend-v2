package co.anitrend.data.edge.config.datasource.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.entity.view.EdgeConfigViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class EdgeConfigLocalSource : AbstractLocalSource<EdgeConfigEntity>() {

    @Query("""
        select count(id) from edge_config
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from edge_config
        """)
    abstract override suspend fun clear()

    @Query("""
        delete from edge_config
        where id = :id
        """)
    abstract suspend fun clearById(id: Long)

    @Query("""
        select * from edge_config
        where id = :id
    """)
    @Transaction
    abstract fun edgeConfigById(
        id: Long
    ): Flow<EdgeConfigViewEntity?>

    @Transaction
    @RawQuery(observedEntities = [EdgeConfigEntity::class])
    abstract fun rawFlow(query: SupportSQLiteQuery): Flow<EdgeConfigViewEntity?>
}
