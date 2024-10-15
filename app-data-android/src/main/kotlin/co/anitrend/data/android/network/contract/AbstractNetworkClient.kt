/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.android.network.contract

import retrofit2.HttpException
import timber.log.Timber

abstract class AbstractNetworkClient<in Input, out Output> {
    /**
     * @return [Boolean] whether or not the request should be retried based on the [exception] received
     */
    protected abstract fun defaultShouldRetry(exception: Throwable): Boolean

    /**
     * Executes the request
     *
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param defaultDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     */
    protected abstract suspend fun Input.execute(
        defaultDelay: Long,
        maxAttempts: Int,
        shouldRetry: (Throwable) -> Boolean,
    ): Output

    /**
     *
     */
    protected fun Throwable.getNextDelay(
        attempt: Int,
        maxAttempts: Int,
        defaultDelay: Long,
        shouldRetry: (Throwable) -> Boolean,
    ): Long {
        var nextDelay: Long = attempt * attempt * defaultDelay
        Timber.w("Request threw an exception -> $this")

        // The response failed, so lets see if we should retry again
        if (!shouldRetry(this)) {
            Timber.w(this, "Specific request is not allowed to retry on this exception")
            throw this
        }

        if (attempt == maxAttempts) {
            Timber.w(this, "Cannot retry on exception or maximum retries reached")
        }

        if (this is HttpException) {
            // If we have a HttpException, check whether we have a Retry-After
            // header to decide how long to delay
            val retryAfterHeader = response()?.headers()?.get(RETRY_AFTER_KEY)
            if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
                // Got a Retry-After value, try and parse it to an long
                Timber.i("Rate limit reached")
                try {
                    nextDelay = (retryAfterHeader.toLong() + 10).coerceAtLeast(defaultDelay)
                } catch (nfe: NumberFormatException) {
                    Timber.e(
                        nfe,
                        "Highly unlikely exception was caught on header retry after",
                    )
                }
            }
        }

        Timber.i(
            "Retrying request in $nextDelay ms -> attempt: ${attempt + 1} maxAttempts: $maxAttempts",
        )
        return nextDelay
    }

    companion object {
        internal const val RETRY_AFTER_KEY = "Retry-After"
    }
}
