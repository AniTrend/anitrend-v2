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
package co.anitrend.data.edge.season.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.season.EdgeSeasonEmbedded
import co.anitrend.data.edge.season.entity.EdgeSeasonEntity
import co.anitrend.data.edge.season.model.EdgeSeasonModel

/**
 * Converts a (mediaId, SeasonModel) pair into [EdgeSeasonEntity].
 */
internal class EdgeSeasonConverter : SupportConverter<EdgeSeasonEmbedded, EdgeSeasonEntity>() {
    override val fromType: (EdgeSeasonEmbedded) -> EdgeSeasonEntity = { pair ->
        val (mediaId, model) = pair
        EdgeSeasonEntity(
            mediaId = mediaId,
            number = model.number,
            name = model.name,
            tmdbId = model.tmdbId,
            overview = model.overview,
            episodeCount = model.episodeCount,
            cover = model.cover,
            airDate = model.airDate,
        )
    }
    override val toType: (EdgeSeasonEntity) -> EdgeSeasonEmbedded = { throw NotImplementedError() }
}
