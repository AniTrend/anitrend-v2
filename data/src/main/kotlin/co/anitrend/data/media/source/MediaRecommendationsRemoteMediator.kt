/*
 * Copyright (C) 2026 AniTrend
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

import androidx.paging.LoadType
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.MediaRecommendationsController
import co.anitrend.data.media.model.query.MediaConnectionQuery
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.recommendation.datasource.local.connection.MediaRecommendationConnectionLocalSource
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.data.recommendation.mapper.MediaRecommendationMapper
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import kotlinx.coroutines.flow.first
import org.threeten.bp.Instant

internal class MediaRecommendationsRemoteMediator(
    private val cacheIdentity: CacheIdentity,
    private val cachePolicy: ICacheStorePolicy,
    private val query: MediaConnectionQuery.RecommendationsPaged,
    private val remoteSource: MediaRemoteSource,
    private val localSource: MediaRecommendationConnectionLocalSource,
    private val controller: MediaRecommendationsController,
    private val mapper: MediaRecommendationMapper,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, MediaRecommendationConnectionEntity>() {
    override val supportPagingHelper =
        SupportPagingHelper(
            isPagingLimit = false,
            pageSize = query.param.perPage,
        )

    private suspend fun shouldRefresh(hasLocalData: Boolean): Boolean =
        !hasLocalData || cachePolicy.shouldRefresh(cacheIdentity, cacheIdentity.expiresAt)

    private suspend fun awaitResult(
        requestType: Request.Type,
        block: suspend (RequestCallback) -> Unit,
    ): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = block,
        )

        return when (
            val result =
                loadState.first {
                    it is LoadState.Success || it is LoadState.Error
                }
        ) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction =
        if (shouldRefresh(localSource.countByMediaId(query.param.id) > 0)) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }

    private suspend fun refreshRecommendations(requestCallback: RequestCallback) {
        mapper.onRequest(
            mediaId = query.param.id,
            page = supportPagingHelper.page,
        )

        val deferred =
            deferred {
                remoteSource.getMediaRecommendations(
                    query.toQueryContainerBuilder(supportPagingHelper),
                )
            }

        controller(deferred, requestCallback) {
            supportPagingHelper.from(it.media?.recommendations)
            it
        }?.let {
            cachePolicy.updateLastRequest(cacheIdentity, Instant.now())
        }
    }

    override suspend fun clearDataSource(context: kotlinx.coroutines.CoroutineDispatcher) {
        localSource.clearByMediaId(query.param.id)
        cachePolicy.invalidateLastRequest(cacheIdentity)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MediaRecommendationConnectionEntity>,
    ): MediatorResult =
        when (loadType) {
            LoadType.REFRESH -> {
                supportPagingHelper.onPageRefresh()
                awaitResult(Request.Type.INITIAL, ::refreshRecommendations)
            }

            LoadType.PREPEND -> MediatorResult.Success(true)

            LoadType.APPEND -> awaitResult(Request.Type.AFTER, ::refreshRecommendations)
        }
}
