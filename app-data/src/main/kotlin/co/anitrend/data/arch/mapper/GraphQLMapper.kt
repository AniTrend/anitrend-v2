/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.arch.mapper

import co.anitrend.arch.data.mapper.SupportResponseMapper
import io.github.wax911.library.model.body.GraphContainer

/**
 * GraphQLMapper specific mapper, assures that all requests respond with [GraphContainer] as the root tree object.
 * Making it easier for us to implement error logging and provide better error messages
 */
internal abstract class GraphQLMapper<S, D> : SupportResponseMapper<GraphContainer<S>, D>()