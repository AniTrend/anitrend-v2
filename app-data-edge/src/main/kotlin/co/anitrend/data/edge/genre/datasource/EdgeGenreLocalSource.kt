package co.anitrend.data.edge.genre.datasource

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.edge.genre.entity.EdgeGenreEntity

@Dao
abstract class EdgeGenreLocalSource : AbstractLocalSource<EdgeGenreEntity>() {

    @Query("""
        select count(id) from edge_config_genre
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from edge_config_genre
        """)
    abstract override suspend fun clear()
}
