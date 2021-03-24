/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.arch.network.client

import co.anitrend.data.arch.network.contract.AbstractNetworkClient
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

internal abstract class RetrofitCallNetworkClient<T> : AbstractNetworkClient<Call<T>, Response<T>>() {

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
    override suspend fun Call<T>.execute(
        defaultDelay: Long,
        maxAttempts: Int,
        shouldRetry: (Throwable) -> Boolean
    ): Response<T> {
        repeat(maxAttempts) { attempt ->
            runCatching {
                val call = if (isExecuted) clone() else this
                call.execute()
            }.onSuccess { response ->
                return response
            }.onFailure { exception ->
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

        // We should never hit here
        throw IllegalStateException("Unrecoverable state while executing request")
    }

    /**
     * Automatically runs the suspendable operation and returns the body
     *
     * @param call The request which needs to be executed
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param firstDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     *
     * @throws HttpException When the [maxAttempts] have been exhausted, or unhandled exception
     */
    @Throws(HttpException::class)
    open suspend fun fetch(
        call: Call<T>,
        firstDelay: Long = 500,
        maxAttempts: Int = 3,
        shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry,
    ) = call.execute(
        firstDelay,
        maxAttempts,
        shouldRetry
    ).bodyOrThrow()
}