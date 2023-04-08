package co.anitrend.data.edge.navigation.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity

@Dao
abstract class EdgeNavigationLocalSource : AbstractLocalSource<EdgeNavigationEntity>() {

    @Query("""
        select count(id) from edge_navigation
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from edge_navigation
        """)
    abstract override suspend fun clear()
}
