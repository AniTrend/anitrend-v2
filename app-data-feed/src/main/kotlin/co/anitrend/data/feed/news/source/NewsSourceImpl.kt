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

package co.anitrend.data.feed.news.source

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.feed.news.NewPagedController
import co.anitrend.data.feed.news.converter.NewsEntityConverter
import co.anitrend.data.feed.news.datasource.local.NewsLocalSource
import co.anitrend.data.feed.news.datasource.remote.NewsRemoteSource
import co.anitrend.data.feed.news.source.contract.NewsSource
import co.anitrend.domain.news.entity.News
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

internal class NewsSourceImpl(
    private val remoteSource: NewsRemoteSource,
    private val localSource: NewsLocalSource,
    private val clearDataHelper: IClearDataHelper,
    private val controller: NewPagedController,
    private val converter: NewsEntityConverter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : NewsSource() {

    override fun observable(): Flow<PagedList<News>> {
        val dataSourceFactory = localSource
            .entryFactory()
            .map(converter::convertFrom)

        return FlowPagedListBuilder(
            dataSourceFactory,
            PAGING_CONFIGURATION,
            null,
            this
        ).buildFlow()
    }

    override suspend fun getNews(requestCallback: RequestCallback): Boolean {
        val deferred = async {
            remoteSource.getNews(query.param.locale)
        }

        val result = controller(deferred, requestCallback)

        return !result.isNullOrEmpty()
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clear()
        }
    }
}