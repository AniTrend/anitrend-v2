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

package co.anitrend.data.carousel.mapper

import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.mapper.paged.AiringSchedulePagedMapper
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.media.converters.MediaModelConverter
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.paged.MediaPagedCombinedMapper
import co.anitrend.data.carousel.model.CarouselAnimeModel
import co.anitrend.data.carousel.model.CarouselMangaModel

internal class CarouselAnimeMapper(
    private val combinedMapper: MediaPagedCombinedMapper,
    private val airingMapper: AiringSchedulePagedMapper,
    private val converter: MediaModelConverter = MediaModelConverter()
) : DefaultMapper<CarouselAnimeModel, List<MediaEntity>>() {

    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaEntity>) {
        combinedMapper.onResponseDatabaseInsert(mappedData)
        airingMapper.onResponseDatabaseInsert(
            mappedData.mapNotNull { entity ->
                entity.nextAiring?.let {
                    AiringScheduleEntity(
                        airingAt = it.airingAt,
                        episode = it.episode,
                        mediaId = entity.id,
                        timeUntilAiring = it.timeUntilAiring,
                        id = it.airingId
                    )
                }
            }
        )
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: CarouselAnimeModel
    ) = (
            source.airingSoon?.airingSchedules?.mapNotNull { it.media }.orEmpty() +
            source.allTimePopular?.media.orEmpty() +
            source.trendingRightNow?.media.orEmpty() +
            source.anticipatedNexSeason?.media.orEmpty() +
            source.popularThisSeason?.media.orEmpty() +
            source.recentlyAdded?.media.orEmpty()
    ).map { converter.convertFrom(it) }
}

internal class CarouselMangaMapper(
    private val combinedMapper: MediaPagedCombinedMapper,
    private val converter: MediaModelConverter = MediaModelConverter()
) : DefaultMapper<CarouselMangaModel, List<MediaEntity>>() {

    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaEntity>) {
        combinedMapper.onResponseDatabaseInsert(mappedData)
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: CarouselMangaModel
    ) = (
            source.allTimePopular?.media.orEmpty() +
            source.popularManhwa?.media.orEmpty() +
            source.recentlyAdded?.media.orEmpty() +
            source.trendingRightNow?.media.orEmpty()
    ).map { converter.convertFrom(it) }
}