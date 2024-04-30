package co.anitrend.data.edge.config.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.genre.entity.EdgeGenreEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity

data class EdgeConfigViewEntity(
    @Embedded val config: EdgeConfigEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val genres: List<EdgeGenreEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val navigation: List<EdgeNavigationEntity>,
)
