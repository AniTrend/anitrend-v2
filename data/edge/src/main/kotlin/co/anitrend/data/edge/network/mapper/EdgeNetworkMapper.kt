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
package co.anitrend.data.edge.network.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.network.EdgeNetworkEmbedded
import co.anitrend.data.edge.network.converter.EdgeNetworkConverter
import co.anitrend.data.edge.network.datasource.EdgeNetworkLocalSource
import co.anitrend.data.edge.network.entity.EdgeNetworkEntity

internal class EdgeNetworkMapper(
    override val localSource: EdgeNetworkLocalSource,
    override val converter: EdgeNetworkConverter,
) : EmbedMapper<EdgeNetworkEmbedded, EdgeNetworkEntity>() {
    override suspend fun onResponseMapFrom(source: List<EdgeNetworkEmbedded>): List<EdgeNetworkEntity> = source.map(converter::convertFrom)
}
