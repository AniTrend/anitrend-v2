package co.anitrend.data.edge.config.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.config.converters.EdgeConfigModelConverter
import co.anitrend.data.edge.config.datasource.local.EdgeConfigLocalSource
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.config.model.remote.EdgeConfigModel
import co.anitrend.data.edge.genre.mapper.EdgeGenreMapper
import co.anitrend.data.edge.navigation.mapper.EdgeNavigationMapper

internal class EdgeConfigMapper(
    private val localSource: EdgeConfigLocalSource,
    private val converter: EdgeConfigModelConverter,
    private val genreMapper: EdgeGenreMapper,
    private val navigationMapper: EdgeNavigationMapper,
) : DefaultMapper<EdgeConfigModel, EdgeConfigEntity>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: EdgeConfigModel): EdgeConfigEntity {
        genreMapper.onEmbedded(source.genres)
        navigationMapper.onEmbedded(source.navigation)
        return converter.convertFrom(source)
    }

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: EdgeConfigEntity) {
        localSource.upsert(data)
        genreMapper.persistEmbedded()
        navigationMapper.persistEmbedded()
    }
}
