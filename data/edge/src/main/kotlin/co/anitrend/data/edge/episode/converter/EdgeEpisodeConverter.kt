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
package co.anitrend.data.edge.episode.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.episode.EdgeEpisodeEmbedded
import co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity
import co.anitrend.data.edge.episode.model.EdgeEpisodeModel

/**
 * Converts a (mediaId, EpisodeModel) pair into [EdgeEpisodeEntity].
 */
internal class EdgeEpisodeConverter : SupportConverter<EdgeEpisodeEmbedded, EdgeEpisodeEntity>() {
    fun convertFromOrNull(pair: EdgeEpisodeEmbedded): EdgeEpisodeEntity? {
        val (mediaId, model) = pair
        val seasonNumber = model.seasonNumber ?: return null
        val episodeNumber = model.episodeNumber ?: return null
        val airDate = model.aired ?: return null

        return EdgeEpisodeEntity(
            mediaId = mediaId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            name = model.title?.english ?: model.title?.romanji ?: model.title?.native,
            overview = model.synopsis,
            image = model.image,
            poster = model.poster,
            runtime = model.duration,
            absoluteEpisodeNumber = model.absoluteEpisodeNumber,
            airDate = airDate,
        )
    }

    override val fromType: (EdgeEpisodeEmbedded) -> EdgeEpisodeEntity = { pair ->
        convertFromOrNull(pair) ?: throw IllegalArgumentException("Episode payload was missing required fields")
    }
    override val toType: (EdgeEpisodeEntity) -> EdgeEpisodeEmbedded = { throw NotImplementedError() }
}
