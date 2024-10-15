package co.anitrend.data.edge.genre.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.genre.entity.EdgeGenreEntity
import co.anitrend.data.edge.genre.model.EdgeGenreModel

internal class EdgeGenreModelConverter(
    override val fromType: (EdgeGenreModel) -> EdgeGenreEntity = {
        EdgeGenreEntity(
            configId = EdgeConfigEntity.DEFAULT_ID,
            name = it.name,
            id = it.mediaId
        )
    },
    override val toType: (EdgeGenreEntity) -> EdgeGenreModel = { throw NotImplementedError() }
) : SupportConverter<EdgeGenreModel, EdgeGenreEntity>()
