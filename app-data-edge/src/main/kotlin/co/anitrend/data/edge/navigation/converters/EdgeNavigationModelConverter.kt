/*
 * Copyright (C) 2024 AniTrend
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
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.navigation.model.EdgeNavigationModel

internal class EdgeNavigationModelConverter(
    override val fromType: (EdgeNavigationModel) -> EdgeNavigationEntity = {
        EdgeNavigationEntity(
            id = it.id,
            destination = it.destination,
            label = it.label,
            icon = it.icon,
            group =
                EdgeNavigationEntity.Group(
                    authenticated = it.group.authenticated,
                    label = it.group.label,
                    id = it.group.id,
                ),
        )
    },
    override val toType: (EdgeNavigationEntity) -> EdgeNavigationModel = { throw NotImplementedError() },
) : SupportConverter<EdgeNavigationModel, EdgeNavigationEntity>()
