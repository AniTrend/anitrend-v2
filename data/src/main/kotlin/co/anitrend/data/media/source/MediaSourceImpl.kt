/*
 * Copyright (C) 2021 AniTrend
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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.common.extension.from
import co.anitrend.data.edge.media.datasource.local.EdgeMediaLocalSource
import co.anitrend.data.edge.media.source.contract.EdgeMediaSource
import co.anitrend.data.graphql.anilist.GetMediaDetail
import co.anitrend.data.graphql.anilist.GetMediaPaged
import co.anitrend.data.graphql.anilist.GetMediaStats
import co.anitrend.data.graphql.anilist.GetMediaWithStudio
import co.anitrend.data.media.MediaDetailController
import co.anitrend.data.media.MediaPagedController
import co.anitrend.data.media.MediaStatsController
import co.anitrend.data.media.MediaStudiosController
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.converter.MediaStatsEntityConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.local.MediaStatsLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.mapper.MediaStatsMapper
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.studio.converter.MediaStudioConnectionEntityConverter
import co.anitrend.data.studio.converter.MediaStudioEntryEnricher
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.mapper.MediaStudioMapper
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import timber.log.Timber

internal class MediaSourceImpl {
    class Detail(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaDetailController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val edgeSource: EdgeMediaSource,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaSource.Detail() {
        override fun observable(): Flow<Media> =
            localSource
                .mediaByIdFlow(cacheIdentity.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun getMedia(requestCallback: RequestCallback): Boolean {
            runCatching {
                edgeSource(id = query.id)
            }.onFailure { throwable ->
                Timber.w(throwable, "Unable to refresh edge media for id=%s", query.id)
            }

            val deferred =
                deferred {
                    remoteSource.getMediaDetail(
                        GetMediaDetail.request(
                            id = query.id.toInt(),
                            type =
                                co.anitrend.data.graphql.anilist.MediaType
                                    .valueOf(query.type.name),
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(query.scoreFormat.name),
                        ),
                    )
                }

            val result = controller(deferred, requestCallback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            edgeSource.clearDataSource(context)
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearById(cacheIdentity.id)
            }
        }
    }

    class Studios(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaStudioConnectionLocalSource,
        private val edgeLocalSource: EdgeMediaLocalSource,
        private val controller: MediaStudiosController,
        private val mapper: MediaStudioMapper,
        private val converter: MediaStudioConnectionEntityConverter,
        private val enricher: MediaStudioEntryEnricher,
        private val clearDataHelper: IClearDataHelper,
        private val edgeSource: EdgeMediaSource,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaSource.Studios() {
        override fun observable(): Flow<List<MediaStudioEntry>> =
            combine(
                localSource.entriesByMediaIdFlow(query.id),
                edgeLocalSource.mediaViewByIdFlow(query.id),
            ) { entries, edge ->
                enricher.enrich(
                    entries = entries.map(converter::convertFrom),
                    networks = edge?.networks.orEmpty(),
                )
            }.flowOn(dispatcher.io)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun refreshStudios(requestCallback: RequestCallback): Boolean {
            runCatching {
                edgeSource(id = query.id)
            }.onFailure { throwable ->
                Timber.w(throwable, "Unable to refresh edge media for studios id=%s", query.id)
            }

            val deferred =
                deferred {
                    remoteSource.getMediaStudios(
                        GetMediaWithStudio.request(
                            id = query.id.toInt(),
                            sort =
                                query.sort?.map {
                                    val baseName = (it.sortable as Enum<*>).name
                                    val enumName = if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                    co.anitrend.data.graphql.anilist.StudioSort
                                        .valueOf(enumName)
                                },
                            isMain = query.isMain,
                        ),
                    )
                }

            val result =
                controller(deferred, requestCallback) {
                    it
                }

            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearByMediaId(cacheIdentity.id)
            }
        }
    }

    class Stats(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaStatsLocalSource,
        private val controller: MediaStatsController,
        private val mapper: MediaStatsMapper,
        private val converter: MediaStatsEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaSource.Stats() {
        override fun observable(): Flow<MediaStats> =
            localSource
                .entryByMediaIdFlow(query.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun refreshStats(requestCallback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    remoteSource.getMediaStats(
                        GetMediaStats.request(
                            id = query.id.toInt(),
                        ),
                    )
                }

            val result =
                controller(deferred, requestCallback) {
                    it
                }

            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearByMediaId(cacheIdentity.id)
            }
        }
    }

    class Paging(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val carouselSource: CarouselSource,
        private val controller: MediaPagedController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: MediaQueryFilter.Paged,
        private val dispatcher: ISupportDispatcher,
    ) : MediaSource.Paging() {
        override fun invoke(param: MediaParam.Find): Flow<PagingData<Media>> {
            assignQuery(param)

            val source =
                MediaPagingSource(
                    cacheIdentity = MediaCache.Identity.Paged(param),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    carouselSource = carouselSource,
                    controller = controller,
                    clearDataHelper = clearDataHelper,
                    filter = filter,
                    query = param,
                    dispatcher = dispatcher,
                )

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator = source,
                pagingSourceFactory = source.pagingSourceFactory(),
            ).flow.map { pagingData -> pagingData.map { entity -> converter.convertFrom(entity) } }
        }
    }
}
