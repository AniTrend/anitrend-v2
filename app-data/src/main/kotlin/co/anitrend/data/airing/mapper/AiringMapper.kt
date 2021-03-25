/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.airing.mapper

import co.anitrend.data.airing.converters.AiringModelConverter
import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.airing.model.AiringScheduleModel
import co.anitrend.data.airing.model.container.AiringScheduleModelContainer
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.mapper.EmbedMapper
import co.anitrend.data.media.mapper.MediaMapper

internal sealed class AiringMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: AiringLocalSource
    protected abstract val converter: AiringModelConverter

    class Paged(
        private val mediaMapper: MediaMapper.Embed,
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : AiringMapper<AiringScheduleModelContainer.Paged, List<AiringScheduleEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<AiringScheduleEntity>) {
            mediaMapper.persistEmbedded()
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: AiringScheduleModelContainer.Paged
        ): List<AiringScheduleEntity> {
            mediaMapper.onEmbedded(
                source.page.airingSchedules.mapNotNull(
                    AiringScheduleModel.Extended::media
                )
            )
            return converter.convertFrom(source.page.airingSchedules)
        }
    }

    class Airing(
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : AiringMapper<AiringScheduleModel, AiringScheduleEntity>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: AiringScheduleEntity) {
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: AiringScheduleModel
        ): AiringScheduleEntity = converter.convertFrom(source)
    }

    class Embed(
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : EmbedMapper<AiringScheduleModel, AiringScheduleEntity>()
}
