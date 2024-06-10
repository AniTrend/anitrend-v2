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

package co.anitrend.data.android.extensions

import co.anitrend.arch.extension.ext.empty
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.paging.AbstractPagingSource
import co.anitrend.data.android.source.AbstractCoreDataSource
import kotlinx.coroutines.launch


/**
 * Wrapper for handling request dispatching from an paging source
 *
 * @param key A key to identify the request with
 * @param paging Paging helper to managing paging tracking
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
operator fun AbstractPagingSource<*>.invoke(
    key: String = String.empty(),
    paging: SupportPagingHelper,
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    scope.launch {
        requestHelper.runIfNotRunning(
            Request.Default(key, requestType)
        ) { requestCallback ->
            when (requestType) {
                Request.Type.BEFORE -> {
                    if (!paging.isFirstPage()) {
                        paging.onPagePrevious()
                        block(requestCallback)
                    }
                    else requestCallback.recordSuccess()
                }
                Request.Type.AFTER -> {
                    paging.onPageNext()
                    block(requestCallback)
                }
                else -> block(requestCallback)
            }
        }
    }
}

/**
 * Wrapper for handling request dispatching from a data source
 *
 * @param key A key to identify the request with
 * @param requestType Type of request that is being run
 * @param block Unit of work to execute
 */
operator fun AbstractCoreDataSource.invoke(
    key: String = String.empty(),
    requestType: Request.Type = Request.Type.INITIAL,
    block: suspend (RequestCallback) -> Unit
) {
    scope.launch {
        requestHelper.runIfNotRunning(
            Request.Default(key, requestType)
        ) { requestCallback ->
            block(requestCallback)
        }
    }
}
