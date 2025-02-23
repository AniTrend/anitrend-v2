/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.core.extensions

import co.anitrend.core.koin.scope.contract.ICustomScope
import org.koin.core.scope.Scope
import org.koin.mp.KoinPlatformTools
import timber.log.Timber

/**
 * Helper to get or create a custom scope
 *
 * @return [Scope] of custom type
 */
operator fun ICustomScope.invoke(): Scope {
    val koin = KoinPlatformTools.defaultContext().get()
    val scope =
        koin.getOrCreateScope(
            toString(),
            qualifier,
        )
    Timber.v("Retrieving or creating custom scope: $scope")
    return scope
}
