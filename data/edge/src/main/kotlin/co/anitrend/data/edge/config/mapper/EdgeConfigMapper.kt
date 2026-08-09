/*
 * Copyright (C) 2025 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.data.edge.config.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.config.converters.EdgeConfigModelConverter
import co.anitrend.data.edge.config.datasource.local.EdgeConfigLocalSource
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.genre.mapper.EdgeGenreMapper
import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.data.edge.navigation.mapper.EdgeNavigationMapper

internal class EdgeConfigMapper(
    private val localSource: EdgeConfigLocalSource,
    private val converter: EdgeConfigModelConverter,
    private val genreMapper: EdgeGenreMapper,
    private val navigationMapper: EdgeNavigationMapper,
) : DefaultMapper<GetConfigData, EdgeConfigEntity>() {
    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: GetConfigData): EdgeConfigEntity {
        val config =
            source.config
                ?: throw IllegalStateException("Config payload did not contain a config root")
        genreMapper.onEmbedded(config.genres.orEmpty().filterNotNull())
        navigationMapper.onEmbedded(config.navigation.orEmpty().filterNotNull())
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
