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

package co.anitrend.data.airing.source

import androidx.paging.PagedList
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.airing.AiringSchedulePagedController
import co.anitrend.data.airing.cache.AiringCache
import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.datasource.remote.AiringRemoteSource
import co.anitrend.data.airing.entity.filter.AiringQueryFilter
import co.anitrend.data.airing.source.contract.AiringScheduleSource
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class AiringScheduleSourceImpl {

    class Paged(
        private val remoteSource: AiringRemoteSource,
        private val localSource: AiringLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: AiringSchedulePagedController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: AiringQueryFilter.Paged,
        override val dispatcher: ISupportDispatcher
    ) : AiringScheduleSource.Paged() {

        override val cacheIdentity = AiringCache.Identity.Paged()

        override fun observable(): Flow<PagedList<Media>> {
            val dataSourceFactory = mediaLocalSource
                .rawFactory(filter.build(query.param))
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getAiringSchedule(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getAiringPaged(queryBuilder)
            }

            controller(deferred, requestCallback) {
                supportPagingHelper.from(it.page)
                it
            }
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context = context, action = localSource::clear)
        }
    }
}
