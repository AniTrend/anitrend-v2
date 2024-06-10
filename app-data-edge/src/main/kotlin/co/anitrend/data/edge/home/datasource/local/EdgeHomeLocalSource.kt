package co.anitrend.data.edge.home.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.home.entity.EdgeHomeEntity

@Dao
abstract class EdgeHomeLocalSource : AbstractLocalSource<EdgeHomeEntity>() {

    @Query("""
        select count(id) from edge_home
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from edge_home
        """)
    abstract override suspend fun clear()
}
