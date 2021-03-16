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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.data.arch.network.model.NetworkMessage
import retrofit2.HttpException
import java.net.SocketTimeoutException

/**
 * Contract for controller strategy
 */
internal abstract class ControllerStrategy<D> {

    protected val moduleTag: String = javaClass.simpleName
    protected abstract val networkMessage: NetworkMessage

    /**
     * Creates human readable exceptions from a given exception
     */
    protected fun Throwable.generateForError() = when (this) {
        is SocketTimeoutException -> {
            RequestError(
                networkMessage.connectivityErrorTittle,
                networkMessage.connectivityErrorMessage,
                cause
            )
        }
        is HttpException -> {
            // TODO: inspect a range of error codes and provide the user with an appropriate message
            RequestError(
                networkMessage.unrecoverableErrorTittle,
                message,
                cause
            )
        }
        else -> RequestError(
            networkMessage.unrecoverableErrorTittle,
            message,
            cause
        )
    }

    /**
     * Execute a task under an implementation strategy
     *
     * @param callback event emitter
     * @param block what will be executed
     */
    internal abstract suspend operator fun invoke(
        callback: RequestCallback,
        block: suspend () -> D?
    ): D?
}