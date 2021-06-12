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

package co.anitrend.data.media.source

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.common.extension.from
import co.anitrend.data.jikan.media.model.query.JikanQuery
import co.anitrend.data.jikan.media.source.contract.JikanSource
import co.anitrend.data.media.MediaDetailController
import co.anitrend.data.media.MediaNetworkController
import co.anitrend.data.media.MediaPagedController
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.relation.model.local.RelationQuery
import co.anitrend.data.relation.source.contract.RelationSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn

internal class MediaSourceImpl {

    class Detail(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaDetailController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val moeSource: RelationSource,
        private val jikanSource: JikanSource,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : MediaSource.Detail() {

        override fun observable(): Flow<Media> {
            return localSource.mediaByIdFlow(cacheIdentity.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getMedia(requestCallback: RequestCallback): Boolean {
            moeSource(RelationQuery(query.param.id))
            val deferred = async {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getMediaDetail(queryBuilder)
            }

            val result = controller(deferred, requestCallback)

            if (result?.malId != null)
                jikanSource(JikanQuery(result.malId, result.type))

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            jikanSource.clearDataSource(context)
            moeSource.clearDataSource(context)
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearById(cacheIdentity.id)
            }
        }
    }

    class Paged(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val carouselSource: CarouselSource,
        private val controller: MediaPagedController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: MediaQueryFilter.Paged,
        override val dispatcher: ISupportDispatcher
    ) : MediaSource.Paged() {

        override val cacheIdentity = MediaCache.Identity.Paged()

        override fun observable(): Flow<PagedList<Media>> {
            val dataSourceFactory = localSource
                .rawFactory(filter.build(query.param))
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getMedia(requestCallback: RequestCallback) {
            val deferred = async {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getMediaPaged(queryBuilder)
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
            // Since carousel entities are media entities as well
            carouselSource.clearDataSource(context)
            clearDataHelper(context) {
                localSource.clear()
            }
        }
    }

    class Network(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaNetworkController,
        override val initialKey: MediaParam.Find,
        override val dispatcher: ISupportDispatcher
    ) : MediaSource.Network() {

        override val cacheIdentity = MediaCache.Identity.Network()

        override suspend fun getMedia(param: MediaParam.Find, callback: RequestCallback): List<Media> {
            val query = MediaQuery.Find(initialKey)
            val deferred = async {
                val builder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getMediaPaged(builder)
            }

            return controller(deferred, callback) {
                supportPagingHelper.from(it.page)
                it
            }.orEmpty()
        }
    }
}