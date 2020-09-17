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

package co.anitrend.core.service

import androidx.lifecycle.LifecycleService
import co.anitrend.arch.extension.ext.UNSAFE
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.ScopeID

abstract class AniTrendLifecycleService : LifecycleService(), KoinScopeComponent {

    private val scopeID: ScopeID by lazy(UNSAFE) { getScopeId() }

    override val koin by lazy(UNSAFE) {
        getKoin()
    }

    override val scope by lazy(UNSAFE) {
        createScope(scopeID, getScopeName(), this)
    }

    override fun onCreate() {
        super.onCreate()
        runCatching {
            koin._logger.debug("Open activity scope: $scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching {
            koin._logger.debug("Close service scope: $scope")
            scope.close()
        }
    }
}