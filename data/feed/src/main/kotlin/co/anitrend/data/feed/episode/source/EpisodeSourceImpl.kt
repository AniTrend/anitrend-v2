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
package co.anitrend.data.feed.episode.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.feed.episode.EpisodePagedController
import co.anitrend.data.feed.episode.cache.EpisodeCache
import co.anitrend.data.feed.episode.converter.EpisodeEntityConverter
import co.anitrend.data.feed.episode.datasource.local.EpisodeLocalSource
import co.anitrend.data.feed.episode.datasource.remote.EpisodeRemoteSource
import co.anitrend.data.feed.episode.model.query.EpisodeQuery
import co.anitrend.data.feed.episode.source.contract.EpisodeSource
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal sealed class EpisodeSourceImpl {
    internal class Detail(
        private val localSource: EpisodeLocalSource,
        private val converter: EpisodeEntityConverter,
        override val dispatcher: ISupportDispatcher,
    ) : EpisodeSource.Detail() {
        override fun observable(param: EpisodeParam.Detail): Flow<Episode> =
            localSource
                .episodeByIdFlow(param.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            // Not supported for this operation
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    internal class Paging(
        private val remoteSource: EpisodeRemoteSource,
        private val localSource: EpisodeLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: EpisodePagedController,
        private val converter: EpisodeEntityConverter,
        override val dispatcher: ISupportDispatcher,
    ) : EpisodeSource.Paging() {
        private fun createPagingSource() =
            EpisodePagingSource(
                cacheIdentity = EpisodeCache.Identity.EPISODE,
                remoteSource = remoteSource,
                localSource = localSource,
                controller = controller,
                converter = converter,
                clearDataHelper = clearDataHelper,
                query = query,
                dispatcher = dispatcher,
            )

        override fun invoke(param: EpisodeParam.Paged): Flow<PagingData<Episode>> {
            query = EpisodeQuery(param)
            val source = createPagingSource()
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
            ).flow
        }

        override fun sync(param: EpisodeParam.Paged): Flow<Boolean> =
            flow {
                query = EpisodeQuery(param)
                val result =
                    createPagingSource().load(
                        loadType = LoadType.REFRESH,
                        state =
                            PagingState(
                                pages = emptyList(),
                                anchorPosition = null,
                                config =
                                    PagingConfig(
                                        pageSize = DEFAULT_PAGE_SIZE,
                                        initialLoadSize = DEFAULT_PAGE_SIZE,
                                        prefetchDistance = DEFAULT_PAGE_SIZE,
                                        enablePlaceholders = false,
                                    ),
                                leadingPlaceholderCount = 0,
                            ),
                    )
                emit(result is androidx.paging.RemoteMediator.MediatorResult.Success)
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
