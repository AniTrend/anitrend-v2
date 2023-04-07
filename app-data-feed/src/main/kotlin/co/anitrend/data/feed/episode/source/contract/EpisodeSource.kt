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

package co.anitrend.data.feed.episode.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.paging.legacy.source.SupportPagingDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.feed.episode.cache.EpisodeCache
import co.anitrend.data.feed.episode.model.query.EpisodeQuery
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal sealed class EpisodeSource {

    internal abstract class Detail : SupportCoreDataSource() {

        protected abstract fun observable(param: EpisodeParam.Detail): Flow<Episode>

        operator fun invoke(param: EpisodeParam.Detail) = observable(param)
    }

    internal abstract class Paged : SupportPagingDataSource<Episode>() {

        protected lateinit var query: EpisodeQuery

        protected val cacheIdentity: CacheIdentity = EpisodeCache.Identity.EPISODE

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<PagedList<Episode>>

        protected abstract suspend fun getEpisodes(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: EpisodeParam.Paged): Flow<PagedList<Episode>> {
            query = EpisodeQuery(param)
            return observable()
        }

        fun sync(param: EpisodeParam.Paged): Flow<Boolean> = flow {
            query = EpisodeQuery(param)
            val resultFlow = MutableSharedFlow<Boolean>()
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = {
                    val result = getEpisodes(it)
                    resultFlow.emit(result)
                    result
                }
            )
            emitAll(resultFlow)
        }

        /**
         * Called when the item at the front of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be prepended to the PagedList before this item.
         *
         * @param itemAtFront The first item of PagedList
         */
        override fun onItemAtFrontLoaded(itemAtFront: Episode) {
            if (supportPagingHelper.isFirstPage()) {
                cachePolicy(
                    scope = scope,
                    requestHelper = requestHelper,
                    cacheIdentity = cacheIdentity,
                    requestType = Request.Type.BEFORE,
                    block = ::getEpisodes
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
                block = ::getEpisodes
            )
        }
    }
}
