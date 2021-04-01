/*
 * Copyright (C) 2021  AniTrend
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

import co.anitrend.data.android.cache.datasource.ICacheStore
import co.anitrend.data.android.controller.strategy.policy.OfflineStrategy
import co.anitrend.data.android.controller.strategy.policy.OnlineStrategy
import org.koin.core.scope.Scope

/**
 * Facade for supplying online strategy
 */
fun <T> Scope.online() =
    OnlineStrategy.create<T>(
        networkMessage = get(),
        connectivity = get()
    )

/**
 * Facade for supplying offline strategy
 */
fun <T> Scope.offline() =
    OfflineStrategy.create<T>(
        networkMessage = get()
    )

fun Scope.cacheLocalSource() =
    get<ICacheStore>().cacheDao()