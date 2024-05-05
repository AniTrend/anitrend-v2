/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.android.extensions

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cache.datasource.ICacheStore
import co.anitrend.data.android.controller.core.DefaultController
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.android.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.android.controller.strategy.policy.OfflineStrategy
import co.anitrend.data.android.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.network.default.DefaultNetworkClient
import co.anitrend.data.android.network.graphql.GraphNetworkClient
import org.koin.core.scope.Scope

/**
 * Facade for supplying online strategy
 */
fun <T> Scope.online() =
    OnlineStrategy.create<T>(
        networkMessage = get(),
        connectivity = get(),
    )

/**
 * Facade for supplying offline strategy
 */
fun <T> Scope.offline() =
    OfflineStrategy.create<T>(
        networkMessage = get(),
    )

fun Scope.cacheLocalSource() = get<ICacheStore>().cacheDao()

/**
 * Extension to help us create a controller
 */
fun <S, D> Scope.graphQLController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get(),
) = GraphQLController(
    mapper = mapper,
    strategy = strategy,
    dispatcher = dispatcher.io,
    client =
        GraphNetworkClient(
            gson = get(),
            dispatcher = dispatcher.io,
        ),
)

/**
 * Extension to help us create a controller
 */
fun <S, D> Scope.defaultController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get(),
) = DefaultController(
    mapper = mapper,
    strategy = strategy,
    dispatcher = dispatcher.io,
    client =
        DefaultNetworkClient(
            dispatcher = dispatcher.io,
        ),
)
