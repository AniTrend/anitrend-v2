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
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.media.converters.MediaEntityConverter
import co.anitrend.data.carousel.datasource.local.CarouselLocalStore
import co.anitrend.data.carousel.datasource.remote.CarouselRemoteSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.carousel.mapper.CarouselAnimeMapper
import co.anitrend.data.carousel.mapper.CarouselMangaMapper
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.extension.toFuzzyDateLike
import co.anitrend.domain.media.entity.MediaCarousel
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

internal class CarouselSourceImpl(
    private val remoteSource: CarouselRemoteSource,
    private val localSource: CarouselLocalStore,
    private val animeMapper: CarouselAnimeMapper,
    private val mangaMapper: CarouselMangaMapper,
    private val clearDataHelper: IClearDataHelper,
    private val strategy: ControllerStrategy<List<MediaEntity>>,
    private val converter: MediaEntityConverter = MediaEntityConverter(),
    cachePolicy: ICacheStorePolicy,
    dispatchers: SupportDispatchers
) : CarouselSource(cachePolicy, dispatchers) {

    private fun Flow<List<MediaEntity>>.toMediaList() = mapNotNull {
        if (it.isNotEmpty())
            converter.convertFrom(it)
        else null
    }

    override fun observable(): Flow<List<MediaCarousel>> = flow {
        val flows = listOf(
            localSource.airingSoonFlow(
                mediaType = MediaType.ANIME,
                currentTime = query.currentTime
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.AIRING_SOON,
                    mediaCollection
                )
            },
            localSource.allTimePopularFlow(
                mediaType = MediaType.ANIME
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.ALL_TIME_POPULAR,
                    mediaCollection
                )
            },
            localSource.trendingNowFlow(
                mediaType = MediaType.ANIME
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.TRENDING_RIGHT_NOW,
                    mediaCollection
                )
            },
            localSource.popularThisSeasonFlow(
                mediaType = MediaType.ANIME,
                season = query.season,
                seasonYear = FuzzyDate(
                    year = query.seasonYear,
                    month = FuzzyDate.UNKNOWN,
                    day = FuzzyDate.UNKNOWN,
                ).toFuzzyDateLike()
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.POPULAR_THIS_SEASON,
                    mediaCollection
                )
            },
            localSource.recentlyAddedFlow(
                mediaType = MediaType.ANIME
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.RECENTLY_ADDED,
                    mediaCollection
                )
            },
            localSource.anticipatedNextSeasonFlow(
                mediaType = MediaType.ANIME,
                season = query.nextSeason,
                seasonYear = FuzzyDate(
                    year = query.nextSeasonYear,
                    month = FuzzyDate.UNKNOWN,
                    day = FuzzyDate.UNKNOWN,
                ).toFuzzyDateLike()
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.ANIME,
                    MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON,
                    mediaCollection
                )
            },
            localSource.allTimePopularFlow(
                mediaType = MediaType.MANGA
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.MANGA,
                    MediaCarousel.CarouselType.ALL_TIME_POPULAR,
                    mediaCollection
                )
            },
            localSource.trendingNowFlow(
                mediaType = MediaType.MANGA
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.MANGA,
                    MediaCarousel.CarouselType.TRENDING_RIGHT_NOW,
                    mediaCollection
                )
            },
            localSource.popularManhwaFlow(
                mediaType = MediaType.MANGA
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.MANGA,
                    MediaCarousel.CarouselType.POPULAR_MANHWA,
                    mediaCollection
                )
            },
            localSource.recentlyAddedFlow(
                mediaType = MediaType.MANGA
            ).toMediaList().map { mediaCollection ->
                MediaCarousel(
                    MediaType.MANGA,
                    MediaCarousel.CarouselType.RECENTLY_ADDED,
                    mediaCollection
                )
            }
        )

        val mergedFlows = combine(flows) {
            it.toList()
        }

        emitAll(mergedFlows)
    }

    override suspend fun getMediaCarouselAnime(requestCallback: RequestCallback) {
        val queryBuilder = query.toQueryContainerBuilder()
        val deferred = async {
            remoteSource.getCarouselAnime(queryBuilder)
        }
        val controller = animeMapper.controller(dispatchers, strategy)

        controller(deferred, requestCallback).orEmpty()
    }

    override suspend fun getMediaCarouselManga(requestCallback: RequestCallback) {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            remoteSource.getCarouselManga(queryBuilder)
        }
        val controller = mangaMapper.controller(dispatchers, strategy)

        controller(deferred, requestCallback).orEmpty()
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(carouselAnimeId)
            cachePolicy.invalidateLastRequest(carouselMangaId)
            localSource.clear()
        }
    }
}