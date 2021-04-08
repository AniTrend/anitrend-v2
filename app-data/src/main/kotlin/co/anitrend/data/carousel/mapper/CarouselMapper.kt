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

import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.MediaMapper

internal class CarouselMapper(
    private val mapper: MediaMapper.EmbedWithMediaList
) : DefaultMapper<CarouselModel, List<MediaEntity>>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: List<MediaEntity>) {
        mapper.onResponseDatabaseInsert(data)
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: CarouselModel): List<MediaEntity> {
        val models = when (source) {
            is CarouselModel.Anime -> source.airingSoon?.airingSchedules
                    ?.mapNotNull(AiringScheduleModel.Extended::media).orEmpty() +
                    source.anticipatedNexSeason?.media.orEmpty() +
                    source.popularThisSeason?.media.orEmpty()

            is CarouselModel.Manga -> source.popularManhwa?.media.orEmpty()
            is CarouselModel.Core -> source.allTimePopular?.media.orEmpty() +
                    source.recentlyAdded?.media.orEmpty() +
                    source.trendingRightNow?.media.orEmpty()
        }

        return mapper.onResponseMapFrom(models)
    }
}