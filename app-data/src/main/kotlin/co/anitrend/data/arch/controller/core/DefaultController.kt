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

package co.anitrend.data.arch.controller.core

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.extension.fetchBodyWithRetry
import co.anitrend.data.arch.mapper.DefaultMapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * AniTrend controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 *
 * @see DefaultMapper
 */
internal class DefaultController<S, out D> private constructor(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatchers: SupportDispatchers
) : ISupportResponse<Deferred<Response<S>>, D> {

    override suspend fun invoke(
        resource: Deferred<Response<S>>,
        requestCallback: RequestCallback
    ) = strategy(requestCallback) {
        /**
         * Suppressing this because: https://discuss.kotlinlang.org/t/warning-inappropriate-blocking-method-call-with-coroutines-how-to-fix/16903
         */
        @Suppress("BlockingMethodInNonBlockingContext")
        val response = resource.fetchBodyWithRetry(dispatchers.io)

        response?.let { data ->
            val mapped = mapper.onResponseMapFrom(data)
            withContext(dispatchers.io) {
                mapper.onResponseDatabaseInsert(mapped)
            }
            mapped
        }
    }


    companion object {
        private val moduleTag = DefaultController::class.java.simpleName

        fun <S, D> newInstance(
            mapper: DefaultMapper<S, D>,
            strategy: ControllerStrategy<D>,
            dispatchers: SupportDispatchers
        ) = DefaultController(
            mapper, strategy, dispatchers
        )
    }
}