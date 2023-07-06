package co.anitrend.data.edge.navigation.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.navigation.converters.EdgeNavigationModelConverter
import co.anitrend.data.edge.navigation.datasource.local.EdgeNavigationLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.model.EdgeNavigationModel

internal class EdgeNavigationMapper(
    override val localSource: EdgeNavigationLocalSource,
    override val converter: EdgeNavigationModelConverter
) : EmbedMapper<EdgeNavigationModel, EdgeNavigationEntity>()
