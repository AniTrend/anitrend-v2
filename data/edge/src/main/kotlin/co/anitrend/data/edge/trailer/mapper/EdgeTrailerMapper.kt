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
package co.anitrend.data.edge.trailer.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.trailer.EdgeTrailerEmbedded
import co.anitrend.data.edge.trailer.converter.EdgeTrailerConverter
import co.anitrend.data.edge.trailer.datasource.EdgeTrailerLocalSource
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity

internal class EdgeTrailerMapper(
    override val localSource: EdgeTrailerLocalSource,
    override val converter: EdgeTrailerConverter,
) : EmbedMapper<EdgeTrailerEmbedded, EdgeTrailerEntity>() {
    override suspend fun onResponseMapFrom(source: List<EdgeTrailerEmbedded>): List<EdgeTrailerEntity> = source.map(converter::convertFrom)
}
