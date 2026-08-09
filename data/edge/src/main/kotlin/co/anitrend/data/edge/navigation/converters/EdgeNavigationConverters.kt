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
package co.anitrend.data.edge.navigation.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationGroupEntity

internal class EdgeNavigationModelConverter(
    override val fromType: (GetConfigData.ConfigNavigation) -> EdgeNavigationEntity = {
        EdgeNavigationEntity(
            criteria = it.criteria,
            destination = it.destination,
            i18n = it.i18n,
            icon = it.icon,
            group =
                EdgeNavigationGroupEntity(
                    authenticated = it.group.authenticated,
                    i18n = it.group.i18n,
                ),
            configId = EdgeConfigEntity.DEFAULT_ID,
        )
    },
    override val toType: (EdgeNavigationEntity) -> GetConfigData.ConfigNavigation = { throw NotImplementedError() },
) : SupportConverter<GetConfigData.ConfigNavigation, EdgeNavigationEntity>()
