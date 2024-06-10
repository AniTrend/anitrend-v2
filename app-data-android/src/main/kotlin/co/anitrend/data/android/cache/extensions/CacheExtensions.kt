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

package co.anitrend.data.android.cache.extensions

import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.contract.IRequestHelper
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

/**
 * Wrapper for handling cache updates and persistence
 *
 * @param requestHelper Request manager for handling requests
 * @param cacheIdentity Identity of the request cache
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
suspend operator fun ICacheStorePolicy.invoke(
    requestHelper: IRequestHelper,
    cacheIdentity: CacheIdentity,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Boolean
) {
    requestHelper.runIfNotRunning(
        Request.Default(cacheIdentity.key, requestType)
    ) { requestCallback ->
        if (shouldRefresh(cacheIdentity, cacheIdentity.expiresAt)) {
            if (block(requestCallback))
                updateLastRequest(cacheIdentity, Instant.now())
        } else requestCallback.recordSuccess()
    }
}

/**
 * Wrapper for handling cache updates and persistence
 *
 * @param requestHelper Request manager for handling requests
 * @param cacheIdentity Identity of the request cache
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
operator fun ICacheStorePolicy.invoke(
    scope: CoroutineScope,
    requestHelper: IRequestHelper,
    cacheIdentity: CacheIdentity,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Boolean
) {
    scope.launch {
        invoke(
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            requestType = requestType,
            block = block,
        )
    }
}

/**
 * Wrapper for handling paging and request dispatching
 *
 * @param paging Paging helper to managing paging tracking
 * @param requestHelper Request manager for handling requests
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
suspend operator fun CacheIdentity.invoke(
    paging: SupportPagingHelper,
    requestHelper: IRequestHelper,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    val request = Request.Default(key, requestType)
    requestHelper.runIfNotRunning(request) { requestCallback ->
        when (requestType) {
            Request.Type.BEFORE -> {
                if (!paging.isFirstPage()) {
                    paging.onPagePrevious()
                    block(requestCallback)
                } else requestCallback.recordSuccess()
            }

            Request.Type.AFTER -> {
                if (!paging.isPagingLimit) {
                    paging.onPageNext()
                    block(requestCallback)
                } else requestCallback.recordSuccess()
            }

            else -> {
                if (!paging.isPagingLimit)
                    block(requestCallback)
                else requestCallback.recordSuccess()
            }
        }
    }
}

/**
 * Wrapper for handling paging and request dispatching
 *
 * @param paging Paging helper to managing paging tracking
 * @param requestHelper Request manager for handling requests
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
operator fun CacheIdentity.invoke(
    scope: CoroutineScope,
    paging: SupportPagingHelper,
    requestHelper: IRequestHelper,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    scope.launch {
        invoke(
            paging = paging,
            requestHelper = requestHelper,
            requestType = requestType,
            block = block,
        )
    }
}

/**
 * Wrapper for handling request dispatching
 *
 * @param requestHelper Request manager for handling requests
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
suspend operator fun CacheIdentity.invoke(
    requestHelper: IRequestHelper,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    requestHelper.runIfNotRunning(
        Request.Default(key, requestType)
    ) { requestCallback ->
        block(requestCallback)
    }
}

/**
 * Wrapper for handling request dispatching
 *
 * @param requestHelper Request manager for handling requests
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
operator fun CacheIdentity.invoke(
    scope: CoroutineScope,
    requestHelper: IRequestHelper,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    scope.launch {
        invoke(
            requestHelper = requestHelper,
            requestType = requestType,
            block = block,
        )
    }
}
