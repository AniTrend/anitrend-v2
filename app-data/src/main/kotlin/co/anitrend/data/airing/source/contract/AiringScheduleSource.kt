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

package co.anitrend.data.airing.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.flow.Flow

internal class AiringScheduleSource {

    abstract class Paged : SupportPagingDataSource<Media>() {

        protected lateinit var query: AiringScheduleQuery

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract fun observable(): Flow<PagedList<Media>>

        protected abstract suspend fun getAiringSchedule(requestCallback: RequestCallback)

        operator fun invoke(param: AiringParam.Find): Flow<PagedList<Media>> {
            query = AiringScheduleQuery(param)
            return observable()
        }

        /**
         * Called when the item at the end of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be appended to the PagedList after this item.
         *
         * @param itemAtEnd The first item of PagedList
         */
        override fun onItemAtEndLoaded(itemAtEnd: Media) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
                block = ::getAiringSchedule,
            )
        }

        /**
         * Called when the item at the front of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be prepended to the PagedList before this item.
         *
         * @param itemAtFront The first item of PagedList
         */
        override fun onItemAtFrontLoaded(itemAtFront: Media) {
            /*cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
                block = ::getAiringSchedule,
            )*/
        }

        /**
         * Called when zero items are returned from an initial load of the PagedList's data source.
         */
        override fun onZeroItemsLoaded() {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                block = ::getAiringSchedule,
            )
        }
    }
}