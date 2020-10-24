/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.arch.extension

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.mapper.DefaultMapper

/**
 * Extension to help us create a controller from a a mapper instance
 */
internal fun <S, D> DefaultMapper<S, D>.controller(
    supportDispatchers: SupportDispatchers,
    strategy: ControllerStrategy<D>
) = GraphQLController.newInstance(
    mapper = this,
    strategy = strategy,
    dispatchers = supportDispatchers
)