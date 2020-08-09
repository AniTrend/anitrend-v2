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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import org.koin.core.KoinComponent
import org.koin.core.get
import timber.log.Timber

/**
 * Runs connectivity check before prior to execution
 */
internal class OnlineStrategy<D> private constructor(
    private val connectivity: SupportConnectivity
) : ControllerStrategy<D>() {

    /**
     * Execute a task under an implementation strategy
     *
     * @param callback event emitter
     * @param block what will be executed
     */
    override suspend fun invoke(
        callback: RequestCallback,
        block: suspend () -> D?
    ) = kotlin.runCatching {
        if (connectivity.isConnected) {
            val result = block()
            callback.recordSuccess()
            result
        } else {
            callback.recordFailure(
                RequestError(
                    "No active connection",
                    "Please check your internet connection",
                    null
                )
            )
            null
        }
    }.onFailure { e ->
        Timber.tag(moduleTag).e(e)
        when (e) {
            is RequestError -> callback.recordFailure(e)
            else -> callback.recordFailure(
                RequestError("Unexpected error occurred", e.message, e.cause)
            )
        }
    }.getOrNull()

    companion object {
        fun <T> create(
            connectivity: SupportConnectivity
        ) = OnlineStrategy<T>(connectivity)
    }
}