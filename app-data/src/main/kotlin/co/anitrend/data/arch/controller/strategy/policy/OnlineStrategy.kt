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

package co.anitrend.data.arch.controller.strategy.policy

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import timber.log.Timber

/**
 * Runs connectivity check before prior to execution
 */
internal class OnlineStrategy<D> private constructor(
    private val connectivity: SupportConnectivity
) : ControllerStrategy<D>() {

    /**
     * Execute a paging task under an implementation strategy
     *
     * @param block what will be executed
     * @param pagingRequestHelper paging event emitter
     */
    override suspend fun invoke(
        block: suspend () -> Unit,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {
        if (connectivity.isConnected) {
            runCatching {
                block()
                pagingRequestHelper.recordSuccess()
            }.exceptionOrNull()?.also { e ->
                Timber.tag(moduleTag).e(e)
                pagingRequestHelper.recordFailure(e)
            }
        }
        else {
            pagingRequestHelper.recordFailure(
                Throwable("Please check your internet connection")
            )
        }
    }

    /**
     * Execute a task under an implementation strategy
     *
     * @param block what will be executed
     * @param networkState network state event emitter
     */
    override suspend fun invoke(
        block: suspend () -> D?,
        networkState: MutableLiveData<NetworkState>
    ): D? {
        if (connectivity.isConnected) {
            return runCatching{
                networkState.postValue(NetworkState.Loading)
                block()
            }.getOrElse {
                Timber.tag(moduleTag).e(it)
                networkState.postValue(
                    NetworkState.Error(
                        heading = it.cause?.message ?: "Unexpected error encountered \uD83E\uDD2D",
                        message = it.message
                    )
                )
                null
            }
        } else {
            networkState.postValue(
                NetworkState.Error(
                    heading = "No internet connection detected \uD83E\uDD2D",
                    message = "Please check your internet connection"
                )
            )
        }
        return null
    }

    companion object {
        fun <T> create(connectivity: SupportConnectivity) =
            OnlineStrategy<T>(connectivity)
    }
}