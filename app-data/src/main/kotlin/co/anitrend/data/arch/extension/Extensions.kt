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

package co.anitrend.data.arch.extension

import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.api.provider.RetrofitProvider
import co.anitrend.data.arch.common.model.paging.PageQuery
import co.anitrend.data.arch.controller.graphql.GraphQLController
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.controller.strategy.policy.OfflineStrategy
import co.anitrend.data.arch.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.mapper.GraphQLMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.scope.Scope
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Extension to help us create a controller from a a mapper instance
 */
internal fun <S, D> GraphQLMapper<S, D>.controller(
    supportDispatchers: SupportDispatchers,
    strategy: ControllerStrategy<D>
) = GraphQLController.newInstance(
    mapper = this,
    strategy = strategy,
    dispatchers = supportDispatchers
)

/**
 * Facade for supplying retrofit interface types
 */
internal inline fun <reified T> Scope.api(endpointType: EndpointType): T =
    RetrofitProvider.provideRetrofit(endpointType, this).create(T::class.java)

/**
 * Facade for supplying database contract
 */
internal fun Scope.db() = get<IAniTrendStore>()

/**
 * Facade for supplying online strategy
 */
internal fun <T> Scope.online() = OnlineStrategy.create<T>(get())

/**
 * Facade for supplying offline strategy
 */
internal fun <T> Scope.offline() = OfflineStrategy.create<T>()

@Throws(HttpException::class)
private fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

private fun defaultShouldRetry(exception: Throwable) = when (exception) {
    is HttpException -> exception.code() == 429
    is SocketTimeoutException,
    is IOException -> true
    else -> false
}

private suspend inline fun <T> Deferred<Response<T>>.executeWithRetry(
    dispatcher: CoroutineDispatcher,
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry
): Response<T> {
    repeat(maxAttempts) { attempt ->
        var nextDelay = attempt * attempt * defaultDelay
        runCatching {
            withContext(dispatcher) {
                await()
            }
        }.onFailure { e ->
            // The response failed, so lets see if we should retry again
            if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                throw e
            }

            if (e is HttpException) {
                // If we have a HttpException, check whether we have a Retry-After
                // header to decide how long to delay
                val retryAfterHeader = e.response()?.headers()?.get("Retry-After")
                if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
                    // Got a Retry-After value, try and parse it to an long
                    try {
                        nextDelay = (retryAfterHeader.toLong() + 10).coerceAtLeast(defaultDelay)
                    } catch (nfe: NumberFormatException) {
                        // Probably won't happen, ignore the value and use the generated default above
                    }
                }
            }
            delay(nextDelay)
        }
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

/**
 * Automatically runs the suspendable operation and returns the body adopted from **tivi**
 *
 * @param dispatcher dispatcher to use
 * @param firstDelay initial delay before retrying
 * @param maxAttempts max number of attempts to retry
 * @param shouldRetry when request should be retried after failure
 *
 * @throws HttpException
 */
@Throws(RequestError::class, IOException::class, HttpException::class, UnknownHostException::class)
internal suspend inline fun <T> Deferred<Response<T>>.fetchBodyWithRetry(
    dispatcher: CoroutineDispatcher,
    firstDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry
) = executeWithRetry(dispatcher, firstDelay, maxAttempts, shouldRetry).bodyOrThrow()

internal fun SupportPagingHelper.toPageQuery() = PageQuery(page, pageSize)
