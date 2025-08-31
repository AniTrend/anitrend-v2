package co.anitrend.data.edge.media.entity.view

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.network.entity.EdgeNetworkEntity
import co.anitrend.data.edge.season.entity.EdgeSeasonEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity

data class EdgeMediaEntityView(
    @Embedded val media: EdgeMediaEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val networks: List<EdgeNetworkEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val trailers: List<EdgeTrailerEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val episodes: List<EdgeEpisodeEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val seasons: List<EdgeSeasonEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "media_id",
    )
    val themes: List<EdgeThemeEntity>,
)
