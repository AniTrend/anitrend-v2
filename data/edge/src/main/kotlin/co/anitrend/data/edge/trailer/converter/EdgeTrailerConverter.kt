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
package co.anitrend.data.edge.trailer.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.trailer.EdgeTrailerEmbedded
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity
import co.anitrend.data.edge.trailer.model.EdgeTrailerModel

/**
 * Converts a (mediaId, TrailerModel) pair into [EdgeTrailerEntity].
 */
internal class EdgeTrailerConverter : SupportConverter<EdgeTrailerEmbedded, EdgeTrailerEntity>() {
    override val fromType: (EdgeTrailerEmbedded) -> EdgeTrailerEntity = { pair ->
        val (mediaId, model) = pair
        EdgeTrailerEntity(
            mediaId = mediaId,
            trailerId = model.id,
            site = model.site,
            thumbnail = model.thumbnail,
        )
    }
    override val toType: (EdgeTrailerEntity) -> EdgeTrailerEmbedded = { throw NotImplementedError() }
}
