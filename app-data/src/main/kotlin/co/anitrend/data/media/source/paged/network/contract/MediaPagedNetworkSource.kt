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

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.source.live.SupportPagingLiveDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.query.MediaQuery
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

internal abstract class MediaPagedNetworkSource(
    dispatchers: SupportDispatchers
) : SupportPagingLiveDataSource<Long, Media>(dispatchers) {

    protected lateinit var query: MediaQuery

    protected abstract val observable: LiveData<PagedList<Media>>

    protected abstract suspend fun getMedia(
        callback: RequestCallback,
        pageInfo: IRequestHelper.RequestType
    ) : List<Media>?

    internal operator fun invoke(query: MediaQuery) {
        this.query = query
    }

    /**
     * Return a key associated with the given item.
     *
     * If your ItemKeyedDataSource is loading from a source that is sorted and loaded by a unique
     * integer ID, you would return `item.getID()` here. This key can then be passed to
     * [loadBefore] or [loadAfter] to load additional items adjacent to the item
     * passed to this function.
     *
     * If your key is more complex, such as when you're sorting by name, then resolving collisions
     * with integer ID, you'll need to return both. In such a case you would use a wrapper class,
     * such as `Pair<String, Integer>` or, in Kotlin,
     * `data class Key(val name: String, val id: Int)`
     *
     * @param item Item to get the key from.
     * @return Key associated with given item.
     */
    override fun getKey(item: Media) = item.id

    /**
     * Load initial data.
     *
     * This method is called first to initialize a PagedList with data. If it's possible to count
     * the items that can be loaded by the DataSource, it's recommended to pass the loaded data to
     * the callback via the three-parameter
     * [androidx.paging.ItemKeyedDataSource.LoadInitialCallback.onResult]. This enables PagedLists
     * presenting data from this source to display placeholders to represent unloaded items.
     *
     * [androidx.paging.ItemKeyedDataSource.LoadInitialParams.requestedInitialKey] and
     * [androidx.paging.ItemKeyedDataSource.LoadInitialParams.requestedLoadSize]
     * are hints, not requirements, so they may be altered or ignored. Note that ignoring the
     * `requestedInitialKey` can prevent subsequent PagedList/DataSource pairs from
     * initializing at the same location. If your data source never invalidates (for example,
     * loading from the network without the network ever signalling that old data must be reloaded),
     * it's fine to ignore the `initialLoadKey` and always start from the beginning of the
     * data set.
     *
     * @param params Parameters for initial load, including initial key and requested size.
     * @param callback Callback that receives initial load data.
     */
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Media>) {
        launch(coroutineContext, CoroutineStart.LAZY) {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.INITIAL
            ) {
                val result = getMedia(it, IRequestHelper.RequestType.INITIAL)
                callback.onResult(result.orEmpty())
                if (!result.isNullOrEmpty())
                    supportPagingHelper.onPageNext()
            }
        }
    }

    /**
     * Load list data after the key specified in [androidx.paging.ItemKeyedDataSource.LoadParams.key].
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     * Data may be passed synchronously during the loadAfter method, or deferred and called at a
     * later time. Further loads going down will be blocked until the callback is called.
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key to load after, and requested size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Media>) {
        launch(coroutineContext, CoroutineStart.LAZY) {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.AFTER
            ) {
                val result = getMedia(it, IRequestHelper.RequestType.AFTER)
                callback.onResult(result.orEmpty())
                if (!result.isNullOrEmpty())
                    supportPagingHelper.onPageNext()
            }
        }
    }

    /**
     * Load list data before the key specified in [androidx.paging.ItemKeyedDataSource.LoadParams.key].
     *
     * It's valid to return a different list size than the page size if it's easier, e.g. if your
     * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
     *
     * **Note:** Data returned will be prepended just before the key
     * passed, so if you vary size, ensure that the last item is adjacent to the passed key.
     *
     * Data may be passed synchronously during the loadBefore method, or deferred and called at a
     * later time. Further loads going up will be blocked until the callback is called.
     *
     * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
     * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
     * and prevent further loading.
     *
     * @param params Parameters for the load, including the key to load before, and requested size.
     * @param callback Callback that receives loaded data.
     */
    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Media>) {
        launch(coroutineContext, CoroutineStart.LAZY) {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.BEFORE
            ) {
                supportPagingHelper.onPagePrevious()
                val result = getMedia(it, IRequestHelper.RequestType.BEFORE)
                callback.onResult(result.orEmpty())
            }
        }
    }

    companion object : DataSource.Factory<Long, Media>(), KoinComponent {
        /**
         * Create a DataSource.
         *
         * The DataSource should invalidate itself if the snapshot is no longer valid. If a
         * DataSource becomes invalid, the only way to query more data is to create a new DataSource
         * from the Factory.
         *
         * [androidx.paging.LivePagedListBuilder] for example will construct a new PagedList and DataSource
         * when the current DataSource is invalidated, and pass the new PagedList through the
         * `LiveData<PagedList>` to observers.
         *
         * @return the new DataSource.
         */
        override fun create() = get<MediaPagedNetworkSource>()
    }
}