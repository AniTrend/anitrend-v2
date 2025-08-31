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
import co.anitrend.data.edge.episode.EdgeEpisodeEmbedded
import co.anitrend.data.edge.episode.mapper.EdgeEpisodeMapper
import co.anitrend.data.edge.image.mapper.EdgeImageMapper
import co.anitrend.data.edge.media.converters.EdgeMediaModelConverter
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.media.model.remote.EdgeMediaModel
import co.anitrend.data.edge.network.mapper.EdgeNetworkMapper
import co.anitrend.data.edge.season.mapper.EdgeSeasonMapper
import co.anitrend.data.edge.theme.mapper.EdgeThemeMapper
import co.anitrend.data.edge.trailer.mapper.EdgeTrailerMapper

internal class EdgeMediaMapper(
    private val localSource: EdgeMediaLocalSource,
    private val converter: EdgeMediaModelConverter,
    private val imageMapper: EdgeImageMapper,
    private val networkMapper: EdgeNetworkMapper,
    private val trailerMapper: EdgeTrailerMapper,
    private val themeMapper: EdgeThemeMapper,
    private val seasonMapper: EdgeSeasonMapper,
    private val episodeMapper: EdgeEpisodeMapper,
) : DefaultMapper<EdgeMediaModel, EdgeMediaEntity>() {
    override suspend fun persist(data: EdgeMediaEntity) {
        localSource.upsert(data)
        imageMapper.persistEmbedded()
        networkMapper.persistEmbedded()
        trailerMapper.persistEmbedded()
        themeMapper.persistEmbedded()
        seasonMapper.persistEmbedded()
        episodeMapper.persistEmbedded()
    }

    override suspend fun onResponseMapFrom(source: EdgeMediaModel): EdgeMediaEntity {
        val model = source.media
        if (model == null) throw NullPointerException("Media was not present be null")
        val entity = converter.convertFrom(source.media)
        imageMapper.onEmbedded(source = entity.id to source.media.image)
        networkMapper.onEmbedded(source = model.networks.map { entity.id to it })
        trailerMapper.onEmbedded(source = model.trailers.map { entity.id to it })
        themeMapper.onEmbedded(source = model.themeSongs.map { entity.id to it })
        val seasons = model.seasons?.map { entity.id to it }.orEmpty()
        seasonMapper.onEmbedded(source = seasons)
        // Flatten all episodes across seasons
        val episodes: List<EdgeEpisodeEmbedded> =
            seasons.flatMap { (media, season) ->
                season.episodes.map { media to it }
            }
        episodeMapper.onEmbedded(source = episodes)
        return entity
    }
}
