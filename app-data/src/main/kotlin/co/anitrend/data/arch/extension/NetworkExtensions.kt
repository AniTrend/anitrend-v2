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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


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
        try {
            return withContext(dispatcher) { await() }
        } catch (e: Exception) {
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