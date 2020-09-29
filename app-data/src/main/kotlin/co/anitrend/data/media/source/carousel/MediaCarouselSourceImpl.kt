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

package co.anitrend.data.media.source.carousel

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.media.converters.MediaEntityConverter
import co.anitrend.data.media.datasource.local.carousel.MediaCarouselLocalStore
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.carousel.MediaCarouselAnimeMapper
import co.anitrend.data.media.mapper.carousel.MediaCarouselMangaMapper
import co.anitrend.data.media.source.carousel.contract.MediaCarouselSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.common.extension.toFuzzyDateLike
import co.anitrend.domain.media.entity.MediaCarousel
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

internal class MediaCarouselSourceImpl(
    private val remoteSource: MediaRemoteSource,
    private val localSource: MediaCarouselLocalStore,
    private val animeMapper: MediaCarouselAnimeMapper,
    private val mangaMapper: MediaCarouselMangaMapper,
    private val strategy: ControllerStrategy<List<MediaEntity>>,
    private val converter: MediaEntityConverter = MediaEntityConverter(),
    dispatchers: SupportDispatchers
) : MediaCarouselSource(dispatchers) {

    private fun Flow<List<MediaEntity>>.toMediaList() =
        map { converter.convertFrom(it) }

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
                    year = query.nextYear,
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
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            remoteSource.getMediaCarouselAnime(queryBuilder)
        }
        val controller = animeMapper.controller(dispatchers, strategy)

        controller(deferred, requestCallback).orEmpty()
    }

    override suspend fun getMediaCarouselManga(requestCallback: RequestCallback) {
        val queryBuilder = query.toQueryContainerBuilder(
            ignoreNulls = false
        )
        val deferred = async {
            remoteSource.getMediaCarouselManga(queryBuilder)
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

    }
}