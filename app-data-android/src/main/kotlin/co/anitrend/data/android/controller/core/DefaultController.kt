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

package co.anitrend.data.android.controller.core

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.data.android.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.network.client.DeferrableNetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * General controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 */
class DefaultController<S, out D>(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatcher: CoroutineDispatcher,
    private val client: DeferrableNetworkClient<S>
) : ISupportResponse<Deferred<Response<S>>, D> {

    override suspend fun invoke(
        resource: Deferred<Response<S>>,
        requestCallback: RequestCallback
    ) = strategy(requestCallback) {
        val response = client.fetch(resource)

        val mapped = mapper.onResponseMapFrom(response)
        withContext(dispatcher) {
            mapper.onResponseDatabaseInsert(mapped)
        }
        mapped
    }

    suspend operator fun invoke(
        resource: Deferred<Response<S>>,
        requestCallback: RequestCallback,
        transformer: (S) -> S
    ) = strategy(requestCallback) {
        val response = client.fetch(resource)
        val data = transformer(response)
        val mapped = mapper.onResponseMapFrom(data)
        withContext(dispatcher) {
            mapper.onResponseDatabaseInsert(mapped)
        }
        mapped
    }
}