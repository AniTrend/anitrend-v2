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
package co.anitrend.data.edge.network.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.core.extensions.requireIntegralLong
import co.anitrend.data.edge.graphql.GetMediaByIdData
import co.anitrend.data.edge.network.EdgeNetworkEmbedded
import co.anitrend.data.edge.network.entity.EdgeNetworkEntity

/**
 * Converts a (mediaId, NetworkModel) pair into a persisted [EdgeNetworkEntity].
 * Reverse conversion is intentionally not implemented.
 */
internal class EdgeNetworkConverter : SupportConverter<EdgeNetworkEmbedded, EdgeNetworkEntity>() {
    override val fromType: (EdgeNetworkEmbedded) -> EdgeNetworkEntity = { pair ->
        val (mediaId, model) = pair
        EdgeNetworkEntity(
            mediaId = mediaId,
            networkId = model.id.requireIntegralLong("network id"),
            name = model.name,
            category = model.category.name,
            isPrimary = model.isPrimary,
            logoPath = model.logoPath,
            originCountry = model.originCountry,
        )
    }
    override val toType: (EdgeNetworkEntity) -> EdgeNetworkEmbedded = { throw NotImplementedError() }
}
