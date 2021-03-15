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

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.api.provider.RetrofitProvider
import co.anitrend.data.arch.controller.strategy.policy.OfflineStrategy
import co.anitrend.data.arch.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.tmdb.api.TmdbApi
import co.anitrend.data.trakt.api.TraktApi
import org.koin.core.scope.Scope


/**
 * Facade for supplying retrofit interface types
 */
internal inline fun <reified T> Scope.api(type: EndpointType): T =
    RetrofitProvider.provide(type, this).create(T::class.java)

internal fun Scope.trakt() = get<TraktApi>()
internal fun Scope.tmdb() = get<TmdbApi>()

/**
 * Facade for supplying database contract
 */
internal fun Scope.db() = get<IAniTrendStore>()

/**
 * Facade for supplying online strategy
 */
internal fun <T> Scope.online() =
    OnlineStrategy.create<T>(
        networkMessage = get(),
        connectivity = get()
    )

/**
 * Facade for supplying offline strategy
 */
internal fun <T> Scope.offline() =
    OfflineStrategy.create<T>(
        networkMessage = get()
    )