/*
 * Copyright (C) 2020 AniTrend
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
import org.koin.android.scope.AndroidScopeComponent
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.newScope
import timber.log.Timber

abstract class AniTrendLifecycleService : LifecycleService(), AndroidScopeComponent, KoinScopeComponent {
    override val scope by newScope()

    override fun onCreate() {
        super.onCreate()
        runCatching {
            Timber.v("Opening service scope: $scope")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        runCatching {
            Timber.v("Closing service scope: $scope")
            closeScope()
        }
    }
}
