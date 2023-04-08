package co.anitrend.data.edge.config.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
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
        select * from edge_config
        where id = :id
    """)
    abstract fun edgeConfig(
        id: Long
    ): Flow<EdgeConfigEntity?>
}
