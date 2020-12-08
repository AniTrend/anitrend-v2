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
import co.anitrend.data.api.model.GraphQLResponse
import co.anitrend.data.arch.common.model.date.FuzzyDateModel
import co.anitrend.data.arch.extension.toFuzzyDateLike
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.carousel.CarouselController
import co.anitrend.data.carousel.cache.CarouselCache
import co.anitrend.data.carousel.datasource.local.CarouselLocalStore
import co.anitrend.data.carousel.datasource.remote.CarouselRemoteSource
import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.media.converter.MediaEntityConverter
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import retrofit2.Response

internal class CarouselSourceImpl(
    private val remoteSource: CarouselRemoteSource,
    private val localSource: CarouselLocalStore,
    private val clearDataHelper: IClearDataHelper,
    private val controller: CarouselController,
    private val converter: MediaEntityConverter,
    cachePolicy: ICacheStorePolicy,
    dispatcher: ISupportDispatcher
) : CarouselSource(cachePolicy, dispatcher) {

    private fun Flow<List<MediaEntity>?>.toMediaCarousel(
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

    override fun observable(): Flow<List<MediaCarousel>?> = flow {
        val carouselFlows = listOf(
            localSource.airingSoonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize,
                currentTime = query.currentTime
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.AIRING_SOON
            ),

            localSource.allTimePopularFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ALL_TIME_POPULAR
            ),

            localSource.trendingNowFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.TRENDING_RIGHT_NOW
            ),

            localSource.popularThisSeasonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize,
                season = query.season,
                seasonYear = FuzzyDateModel(
                    year = query.seasonYear,
                    month = FuzzyDateModel.UNKNOWN,
                    day = FuzzyDateModel.UNKNOWN,
                ).toFuzzyDateLike()
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.POPULAR_THIS_SEASON,
            ),

            localSource.recentlyAddedFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.RECENTLY_ADDED
            ),

            localSource.anticipatedNextSeasonFlow(
                mediaType = MediaType.ANIME,
                pageSize = query.pageSize,
                season = query.nextSeason,
                seasonYear = FuzzyDateModel(
                    year = query.nextSeasonYear,
                    month = FuzzyDateModel.UNKNOWN,
                    day = FuzzyDateModel.UNKNOWN,
                ).toFuzzyDateLike()
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON
            ),

            localSource.allTimePopularFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.ALL_TIME_POPULAR
            ),

            localSource.trendingNowFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.TRENDING_RIGHT_NOW
            ),

            localSource.popularManhwaFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.pageSize
            ).flowOn(dispatcher.io).toMediaCarousel(
                MediaType.MANGA,
                MediaCarousel.CarouselType.POPULAR_MANHWA
            ),

            localSource.recentlyAddedFlow(
                mediaType = MediaType.MANGA,
                pageSize = query.pageSize
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

    override suspend fun getMediaCarousel(
        requestCallback: RequestCallback,
        mediaType: MediaType
    ): Boolean {
        val queryBuilder = query.copy(type = mediaType).toQueryContainerBuilder()
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
            cachePolicy.invalidateLastRequest(CarouselCache.ANIME_META_ID)
            cachePolicy.invalidateLastRequest(CarouselCache.MANGA_META_ID)
            cachePolicy.invalidateLastRequest(CarouselCache.ANIME_ID)
            cachePolicy.invalidateLastRequest(CarouselCache.MANGA_ID)
            localSource.clear()
        }
    }
}