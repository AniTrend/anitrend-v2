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

package co.anitrend.data.media.source.paged.network.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.live.SupportPagingLiveDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaSort
import kotlinx.coroutines.launch

internal abstract class MediaPagedNetworkSource(
    dispatcher: ISupportDispatcher,
    private val initialKey: IGraphPayload
) : SupportPagingLiveDataSource<IGraphPayload, Media>(dispatcher) {

    protected abstract suspend fun getMedia(
        query: IGraphPayload,
        callback: RequestCallback
    ) : List<Media>?

    /**
     * Load initial data.
     *
     * This method is called first to initialize a PagedList with data. If it's possible to count
     * the items that can be loaded by the DataSource, it's recommended to pass the loaded data to
     * the callback via the three-parameter
     * [LoadInitialCallback.onResult]. This enables PagedLists
     * presenting data from this source to display placeholders to represent unloaded items.
     *
     * [LoadInitialParams.requestedLoadSize] is a hint, not a requirement, so it may be may be
     * altered or ignored.
     *
     * @param params Parameters for initial load, including requested load size.
     * @param callback Callback that receives initial load data.
     */
    override fun loadInitial(
        params: LoadInitialParams<IGraphPayload>,
        callback: LoadInitialCallback<IGraphPayload, Media>
    ) {
        launch {
            requestHelper.runIfNotRunning(
                Request.Default("media_paged_network_initial", Request.Type.INITIAL)
            ) {
                val result = getMedia(initialKey, it)
                callback.onResult(result.orEmpty(), null, initialKey)
            }
        }
    }



    /**
     * Append page with the key specified by [LoadParams.key].
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     * Data may be passed synchronously during the load method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key for the new page, and requested load
     * size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadAfter(
        params: LoadParams<IGraphPayload>,
        callback: LoadCallback<IGraphPayload, Media>
    ) {
        supportPagingHelper.onPageNext()
        launch {
            requestHelper.runIfNotRunning(
                Request.Default("media_paged_network_after", Request.Type.AFTER)
            ) {
                val result = getMedia(params.key, it)
                callback.onResult(result.orEmpty(), params.key)
            }
        }
    }

    /**
     * Prepend page with the key specified by [LoadParams.key].
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     * Data may be passed synchronously during the load method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key for the new page, and requested load
     * size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadBefore(
        params: LoadParams<IGraphPayload>,
        callback: LoadCallback<IGraphPayload, Media>
    ) {
        if (!supportPagingHelper.isFirstPage())
            supportPagingHelper.onPagePrevious()
        launch {
            requestHelper.runIfNotRunning(
                Request.Default("media_paged_network_before", Request.Type.BEFORE)
            ) {
                if (!supportPagingHelper.isFirstPage()) {
                    val result = getMedia(params.key, it)
                    callback.onResult(result.orEmpty(), params.key)
                }
            }
        }
    }
}