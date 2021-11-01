 /*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.carousel.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.common.extension.toFuzzyDateLike
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.MediaCarouselListController
import co.anitrend.data.carousel.datasource.local.CarouselLocalSource
import co.anitrend.data.carousel.datasource.remote.CarouselRemoteSource
import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import retrofit2.Response

internal class CarouselSourceImpl(
    private val remoteSource: CarouselRemoteSource,
    private val localSource: CarouselLocalSource,
    private val clearDataHelper: IClearDataHelper,
    private val controller: MediaCarouselListController,
    private val converter: MediaEntityViewConverter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : CarouselSource() {

    private fun Flow<List<MediaEntityView>?>.toMediaCarousel(
        mediaType: MediaType,
        carouselType: MediaCarousel.CarouselType
    ) = mapNotNull { entities->
        entities?.let {
            MediaCarousel(
                mediaType,
                carouselType,
                converter.convertFrom(it)
            )
        }
    }.flowOn(dispatcher.computation)

    override fun observable(): Flow<List<MediaCarousel>> = flow {
        val carouselFlows = listOf(
            localSource.airingSoonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize,
                currentTime = query.param.currentTime
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.AIRING_SOON
            ),

            localSource.allTimePopularFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ALL_TIME_POPULAR
            ),

            localSource.trendingNowFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.TRENDING_RIGHT_NOW
            ),

            localSource.popularThisSeasonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize,
                season = query.param.season,
                seasonYear = FuzzyDateModel(
                    year = query.param.seasonYear,
                    month = FuzzyDateModel.UNKNOWN,
                    day = FuzzyDateModel.UNKNOWN,
                ).toFuzzyDateLike()
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.POPULAR_THIS_SEASON,
            ),

            localSource.recentlyAddedFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.RECENTLY_ADDED
            ),

            localSource.anticipatedNextSeasonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.param.pageSize,
                season = query.param.nextSeason,
                seasonYear = FuzzyDateModel(
                    year = query.param.nextSeasonYear,
                    month = FuzzyDateModel.UNKNOWN,
                    day = FuzzyDateModel.UNKNOWN,
                ).toFuzzyDateLike()
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON
            ),

            localSource.allTimePopularFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.ALL_TIME_POPULAR
            ),

            localSource.trendingNowFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.TRENDING_RIGHT_NOW
            ),

            localSource.popularManhwaFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.POPULAR_MANHWA
            ),

            localSource.recentlyAddedFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.param.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.RECENTLY_ADDED
            )
        )

        val mergedFlows =
            combine(carouselFlows) { carouselItems ->
                carouselItems.toList()
            }.filter(List<MediaCarousel>::isNotEmpty)

        emitAll(mergedFlows)
    }

    override suspend fun getMediaCarouselAnimeMeta(
        requestCallback: RequestCallback
    ): Boolean {
        val param = query.param.copy(type = MediaType.ANIME)
        val queryBuilder = query.copy(param = param).toQueryContainerBuilder()
        val deferred = async {
            val carousel = remoteSource.getCarousel(queryBuilder)
            @Suppress("UNCHECKED_CAST")
            carousel as Response<GraphQLResponse<CarouselModel>>
        }

        val result = controller(deferred, requestCallback)
        return !result.isNullOrEmpty()
    }

    override suspend fun getMediaCarouselMangaMeta(
        requestCallback: RequestCallback
    ): Boolean {
        val param = query.param.copy(type = MediaType.MANGA)
        val queryBuilder = query.copy(param = param).toQueryContainerBuilder()
        val deferred = async {
            val carousel = remoteSource.getCarousel(queryBuilder)
            @Suppress("UNCHECKED_CAST")
            carousel as Response<GraphQLResponse<CarouselModel>>
        }

        val result = controller(deferred, requestCallback)
        return !result.isNullOrEmpty()
    }

    override suspend fun getMediaCarouselAnime(requestCallback: RequestCallback): Boolean {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            val carousel = remoteSource.getCarouselAnime(queryBuilder)
            @Suppress("UNCHECKED_CAST")
            carousel as Response<GraphQLResponse<CarouselModel>>
        }

        val result = controller(deferred, requestCallback)
        return !result.isNullOrEmpty()
    }

    override suspend fun getMediaCarouselManga(requestCallback: RequestCallback): Boolean {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            val carousel = remoteSource.getCarouselManga(queryBuilder)
            @Suppress("UNCHECKED_CAST")
            carousel as Response<GraphQLResponse<CarouselModel>>
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
            cachePolicy.invalidateLastRequest(animeMetaCacheIdentity)
            cachePolicy.invalidateLastRequest(mangaMetaCacheIdentity)
            cachePolicy.invalidateLastRequest(animeCacheIdentity)
            cachePolicy.invalidateLastRequest(mangaCacheIdentity)
            localSource.clear()
        }
    }
}