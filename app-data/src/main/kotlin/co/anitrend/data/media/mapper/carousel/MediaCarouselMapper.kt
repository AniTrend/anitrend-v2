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

package co.anitrend.data.media.mapper.carousel

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.media.converters.MediaConverter
import co.anitrend.data.media.model.MediaCarouselAnimeModel
import co.anitrend.data.media.model.MediaCarouselMangaModel
import co.anitrend.domain.media.entity.MediaCarousel
import co.anitrend.domain.media.enums.MediaType

internal class MediaCarouselAnimeMapper(
    private val converter: MediaConverter = MediaConverter()
) : GraphQLMapper<MediaCarouselAnimeModel, List<MediaCarousel>>() {
    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaCarousel>) {

    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: MediaCarouselAnimeModel
    ) = listOf(
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.AIRING_SOON,
                converter.convertFrom(
                    source.airingSoon?.airingSchedules?.mapNotNull { it.media }.orEmpty()
                )
            ),
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ALL_TIME_POPULAR,
                converter.convertFrom(
                    source.allTimePopular?.media.orEmpty()
                )
            ),
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.TRENDING_RIGHT_NOW,
                converter.convertFrom(
                    source.trendingRightNow?.media.orEmpty()
                )
            ),
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.POPULAR_THIS_SEASON,
                converter.convertFrom(
                    source.popularThisSeason?.media.orEmpty()
                )
            ),
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.RECENTLY_ADDED,
                converter.convertFrom(
                    source.recentlyAdded?.media.orEmpty()
                )
            ),
            MediaCarousel(
                MediaType.ANIME,
                MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON,
                converter.convertFrom(
                    source.anticipatedNexSeason?.media.orEmpty()
                )
            )
        )
}

internal class MediaCarouselMangaMapper(
    private val converter: MediaConverter = MediaConverter()
) : GraphQLMapper<MediaCarouselMangaModel, List<MediaCarousel>>() {
    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaCarousel>) {

    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: MediaCarouselMangaModel
    ) = listOf(
        MediaCarousel(
            MediaType.MANGA,
            MediaCarousel.CarouselType.ALL_TIME_POPULAR,
            converter.convertFrom(
                source.allTimePopular?.media.orEmpty()
            )
        ),
        MediaCarousel(
            MediaType.MANGA,
            MediaCarousel.CarouselType.TRENDING_RIGHT_NOW,
            converter.convertFrom(
                source.trendingRightNow?.media.orEmpty()
            )
        ),
        MediaCarousel(
            MediaType.MANGA,
            MediaCarousel.CarouselType.POPULAR_MANHWA,
            converter.convertFrom(
                source.popularManhwa?.media.orEmpty()
            )
        ),
        MediaCarousel(
            MediaType.MANGA,
            MediaCarousel.CarouselType.RECENTLY_ADDED,
            converter.convertFrom(
                source.recentlyAdded?.media.orEmpty()
            )
        )
    )
}