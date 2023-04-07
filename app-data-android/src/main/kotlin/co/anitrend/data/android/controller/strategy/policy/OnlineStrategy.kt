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

package co.anitrend.data.android.controller.strategy.policy

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.data.android.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.android.network.model.NetworkMessage
import timber.log.Timber

/**
 * Runs connectivity check before prior to execution
 */
class OnlineStrategy<D> private constructor(
    private val connectivity: ISupportConnectivity,
    override val networkMessage: NetworkMessage
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
    ): D? {
        runCatching {
            if (connectivity.isConnected)
                block()
            else
                throw RequestError(
                    networkMessage.connectivityErrorTittle,
                    networkMessage.connectivityErrorMessage
                )
        }.onSuccess { result ->
            callback.recordSuccess()
            return result
        }.onFailure { exception ->
            Timber.e(exception)
            when (exception) {
                is RequestError -> callback.recordFailure(exception)
                else -> callback.recordFailure(exception.generateForError())
            }
        }

        return null
    }

    companion object {
        fun <T> create(
            connectivity: ISupportConnectivity,
            networkMessage: NetworkMessage
        ) = OnlineStrategy<T>(connectivity, networkMessage)
    }
}
