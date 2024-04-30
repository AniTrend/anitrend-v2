package co.anitrend.data.edge.genre.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.genre.converters.EdgeGenreModelConverter
import co.anitrend.data.edge.genre.datasource.EdgeGenreLocalSource
import co.anitrend.data.edge.genre.entity.EdgeGenreEntity
import co.anitrend.data.edge.genre.model.EdgeGenreModel

internal class EdgeGenreMapper(
    override val localSource: EdgeGenreLocalSource,
    override val converter: EdgeGenreModelConverter,
) : EmbedMapper<EdgeGenreModel, EdgeGenreEntity>()
