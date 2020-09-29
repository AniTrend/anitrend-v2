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

package co.anitrend.core.ui.fragment.list

import android.os.Bundle
import android.view.View
import co.anitrend.arch.core.model.ISupportViewModelState
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.ui.fragment.list.SupportFragmentList
import org.koin.android.ext.android.getKoin
import org.koin.core.scope.KoinScopeComponent
import org.koin.core.scope.ScopeID

abstract class AniTrendListFragment<M> : SupportFragmentList<M>(), KoinScopeComponent {

    private val scopeID: ScopeID by lazy(UNSAFE) { getScopeId() }

    override val koin by lazy(UNSAFE) {
        getKoin()
    }

    override val scope by lazy(UNSAFE) {
        createScope(scopeID, getScopeName(), this)
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): ISupportViewModelState<*>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runCatching {
            koin.logger.debug("Open fragment scope: $scope")
        }
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after [.onStop] and before [.onDetach].
     */
    override fun onDestroy() {
        super.onDestroy()
        runCatching {
            koin.logger.debug("Close fragment scope: $scope")
            scope.close()
        }
    }
}