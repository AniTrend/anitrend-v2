/*
 * Copyright (C) 2025 AniTrend
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
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.feed.episode.EpisodePagedController
import co.anitrend.data.feed.episode.converter.EpisodeEntityConverter
import co.anitrend.data.feed.episode.datasource.local.EpisodeLocalSource
import co.anitrend.data.feed.episode.datasource.remote.EpisodeRemoteSource
import co.anitrend.data.feed.episode.model.query.EpisodeQuery
import co.anitrend.domain.episode.entity.Episode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
internal class EpisodePagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: EpisodeRemoteSource,
    private val localSource: EpisodeLocalSource,
    private val controller: EpisodePagedController,
    private val converter: EpisodeEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    private val query: EpisodeQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, Episode>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, Episode> =
        localSource
            .entryFactory()
            .map { entity -> converter.convertFrom(entity) }
            .asPagingSourceFactory()

    private suspend fun getEpisodes(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getLatestEpisodes(query.param.locale)
            }
        controller(deferred, requestCallback)
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getEpisodes,
        )
        val result = loadState.first { it is LoadState.Success || it is LoadState.Error }
        return when (result) {
            is LoadState.Success -> MediatorResult.Success(endOfPaginationReached = true)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Episode>,
    ): MediatorResult =
        when (loadType) {
            LoadType.REFRESH -> {
                clearDataSource(dispatcher.io)
                invoke(requestType = Request.Type.INITIAL)
            }
            LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> MediatorResult.Success(endOfPaginationReached = true)
        }
}
