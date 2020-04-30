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

package co.anitrend.data.arch.controller.strategy.contract

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState

internal abstract class ControllerStrategy<D> {

    protected val moduleTag = javaClass.simpleName

    /**
     * Execute a paging task under an implementation strategy
     *
     * @param block what will be executed
     * @param pagingRequestHelper paging event emitter
     */
    internal abstract suspend operator fun invoke(
        block: suspend () -> Unit,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    )

    /**
     * Execute a task under an implementation strategy
     *
     * @param block what will be executed
     * @param networkState network state event emitter
     */
    internal abstract suspend operator fun invoke(
        block: suspend () -> D?,
        networkState: MutableLiveData<NetworkState>
    ): D?
}