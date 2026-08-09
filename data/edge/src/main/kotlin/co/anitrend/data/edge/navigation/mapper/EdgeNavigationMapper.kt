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
package co.anitrend.data.edge.navigation.mapper

import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.edge.graphql.GetConfigData
import co.anitrend.data.edge.navigation.converters.EdgeNavigationModelConverter
import co.anitrend.data.edge.navigation.datasource.EdgeNavigationLocalSource
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity

internal class EdgeNavigationMapper(
    override val localSource: EdgeNavigationLocalSource,
    override val converter: EdgeNavigationModelConverter,
) : EmbedMapper<GetConfigData.ConfigNavigation, EdgeNavigationEntity>()
