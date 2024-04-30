package co.anitrend.data.edge.navigation.datasource

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity

@Dao
abstract class EdgeNavigationLocalSource : AbstractLocalSource<EdgeNavigationEntity>() {

    @Query("""
        select count(id) from edge_config_navigation
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from edge_config_navigation
        """)
    abstract override suspend fun clear()
}
