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

package co.anitrend.data.news.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.cache.extensions.invoke
import co.anitrend.data.cache.model.CacheIdentity
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.news.cache.NewsCache
import co.anitrend.data.news.model.query.NewsQuery
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.model.NewsParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class NewsSource : SupportPagingDataSource<News>() {

    protected lateinit var query: NewsQuery

    protected val cacheIdentity: CacheIdentity = NewsCache.Identity.NEWS

    protected abstract val cachePolicy: ICacheStorePolicy

    protected abstract fun observable(): Flow<PagedList<News>>

    protected abstract suspend fun getNews(requestCallback: RequestCallback): Boolean

    operator fun invoke(param: NewsParam): Flow<PagedList<News>> {
        query = NewsQuery(param)
        return observable()
    }

    /**
     * Called when the item at the front of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     * No more data will be prepended to the PagedList before this item.
     *
     * @param itemAtFront The first item of PagedList
     */
    override fun onItemAtFrontLoaded(itemAtFront: News) {
        if (supportPagingHelper.isFirstPage()) {
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                requestType = Request.Type.BEFORE,
                block = ::getNews
            )
        }
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getNews
        )
    }
}