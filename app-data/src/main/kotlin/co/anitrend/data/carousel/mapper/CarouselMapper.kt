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

import co.anitrend.data.airing.converters.AiringModelConverter
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.mapper.paged.AiringSchedulePagedMapper
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.arch.railway.extension.evaluate
import co.anitrend.data.arch.railway.extension.otherwise
import co.anitrend.data.arch.railway.extension.then
import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.paged.MediaPagedCombinedMapper
import co.anitrend.data.media.model.MediaModel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class CarouselMapper(
    private val combinedMapper: MediaPagedCombinedMapper,
    private val airingMapper: AiringSchedulePagedMapper,
    private val airingConverter: AiringModelConverter,
    private val converter: MediaModelConverter,
    private val context: CoroutineContext
) : DefaultMapper<CarouselModel, List<MediaEntity>>() {

    private suspend fun List<MediaModel>.flatMapAndExtract(): List<MediaEntity> {
        val airingSchedules = airingConverter.convertFrom(
            mapNotNull { it.nextAiringEpisode as? AiringScheduleModel }
        )
        withContext(context) {
            airingMapper.onResponseDatabaseInsert(airingSchedules)
        }
        return converter.convertFrom(this)
    }

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: List<MediaEntity>): OutCome<Nothing?> {
        return runCatching {
            combinedMapper.onResponseDatabaseInsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }


    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaEntity>) {
        mappedData evaluate
                ::checkValidity then
                ::persistChanges otherwise
                ::handleException
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: CarouselModel) = when (source) {
        is CarouselModel.Anime -> (
            source.airingSoon?.airingSchedules
                    ?.mapNotNull { it.media }.orEmpty() +
            source.anticipatedNexSeason?.media.orEmpty() +
            source.popularThisSeason?.media.orEmpty()
        )
        is CarouselModel.Manga -> (
            source.popularManhwa?.media.orEmpty()
        )
        is CarouselModel.Core -> (
            source.allTimePopular?.media.orEmpty() +
            source.recentlyAdded?.media.orEmpty() +
            source.trendingRightNow?.media.orEmpty()
        )
    }.flatMapAndExtract()
}