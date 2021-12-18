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

package co.anitrend.data.android.network.client

import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.data.android.network.contract.AbstractNetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * A network client that manages how requests should be run and when to retry
 *
 * @property dispatcher A [CoroutineDispatcher] that should be used for running requests
 */
abstract class DeferrableNetworkClient<T> : AbstractNetworkClient<Deferred<Response<T>>, Response<T>>() {

    protected abstract val dispatcher: CoroutineDispatcher

    /**
     * @return [Response.body] of the response
     *
     * @throws HttpException When the request was not successful
     */
    @Throws(HttpException::class)
    protected open fun Response<T>.bodyOrThrow(): T {
        if (!isSuccessful) throw HttpException(this)
        return requireNotNull(body()) {
            "Response<T>.bodyOrThrow() -> response body was null"
        }
    }

    /**
     * @return [Boolean] whether or not the request should be retried based on the [exception] received
     */
    override fun defaultShouldRetry(exception: Throwable) = when (exception) {
        is HttpException -> exception.code() == 429
        is SocketTimeoutException,
        is IOException -> true
        else -> false
    }

    /**
     * Executes the request
     *
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param defaultDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     */
    override suspend fun Deferred<Response<T>>.execute(
        defaultDelay: Long,
        maxAttempts: Int,
        shouldRetry: (Throwable) -> Boolean
    ): Response<T> {
        var lastKnownException: Throwable? = null

        repeat(maxAttempts) { attempt ->
            runCatching {
                withContext(dispatcher) { await() }
            }.onSuccess { response ->
                return response
            }.onFailure { exception ->
                if (lastKnownException != exception)
                    lastKnownException = exception
                delay(
                    exception.getNextDelay(
                        attempt,
                        maxAttempts,
                        defaultDelay,
                        shouldRetry
                    )
                )
            }
        }

        throw lastKnownException ?: RequestError(
            "Unable to recover from unknown error",
            "Maximum retry attempts exhausted without success"
        )
    }

    /**
     * Automatically runs the suspendable operation and returns the body
     *
     * @param deferredRequest The request which needs to be executed
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param firstDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     *
     * @throws HttpException When the [maxAttempts] have been exhausted, or unhandled exception
     */
    @Throws(HttpException::class)
    open suspend fun fetch(
        deferredRequest: Deferred<Response<T>>,
        firstDelay: Long = 500,
        maxAttempts: Int = 3,
        shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry,
    ) = deferredRequest.execute(
        firstDelay,
        maxAttempts,
        shouldRetry
    ).bodyOrThrow()
}