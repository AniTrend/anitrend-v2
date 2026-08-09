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
package co.anitrend.data.edge.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.edge.episode.mapper.EdgeEpisodeMapper
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.image.mapper.EdgeImageMapper
import co.anitrend.data.edge.media.converters.EdgeMediaModelConverter
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.network.mapper.EdgeNetworkMapper
import co.anitrend.data.edge.theme.mapper.EdgeThemeMapper
import co.anitrend.data.edge.theme.model.isPersistable
import co.anitrend.data.edge.trailer.mapper.EdgeTrailerMapper

internal class EdgeMediaMapper(
    private val localSource: EdgeMediaLocalSource,
    private val converter: EdgeMediaModelConverter,
    private val imageMapper: EdgeImageMapper,
    private val networkMapper: EdgeNetworkMapper,
    private val trailerMapper: EdgeTrailerMapper,
    private val themeMapper: EdgeThemeMapper,
    private val episodeMapper: EdgeEpisodeMapper,
) : DefaultMapper<GetMediaByIdData, EdgeMediaEntity>() {
    override suspend fun persist(data: EdgeMediaEntity) {
        localSource.upsert(data)
        imageMapper.persistEmbedded()
        networkMapper.persistEmbedded()
        trailerMapper.persistEmbedded()
        themeMapper.persistEmbedded()
        episodeMapper.persistEmbedded()
    }

    override suspend fun onResponseMapFrom(source: GetMediaByIdData): EdgeMediaEntity {
        val model = source.series ?: throw NullPointerException("Series payload was null")
        val entity = converter.convertFrom(model)
        imageMapper.onEmbedded(mediaId = entity.id, sources = model.images.orEmpty().filterNotNull())
        networkMapper.onEmbedded(
            source =
                model.networks.orEmpty().mapNotNull { network ->
                    if (network != null) entity.id to network else null
                },
        )
        trailerMapper.onEmbedded(mediaId = entity.id, sources = model.trailers.orEmpty().filterNotNull())
        themeMapper.onEmbedded(
            source =
                model.animethemes
                    .orEmpty()
                    .filterNotNull()
                    .filter(GetMediaByIdData.SeriesAnimethemes::isPersistable)
                    .map { entity.id to it },
        )
        return entity
    }
}
