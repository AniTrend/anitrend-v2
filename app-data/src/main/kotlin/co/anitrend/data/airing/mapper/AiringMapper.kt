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
import co.anitrend.data.airing.model.page.AiringSchedulePageModel
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome

internal sealed class AiringMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: AiringLocalSource
    protected abstract val converter: AiringModelConverter

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: D): OutCome<Nothing?> {
        return runCatching {
            require (data is AiringScheduleEntity)
            localSource.upsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    class Paged(
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : AiringMapper<AiringSchedulePageModel, List<AiringScheduleEntity>>() {
        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: List<AiringScheduleEntity>): OutCome<Nothing?> {
            return runCatching {
                localSource.upsert(data)
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: AiringSchedulePageModel
        ) = converter.convertFrom(source.page.airingSchedules)
    }

    class Collection(
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : AiringMapper<List<AiringScheduleModel>, List<AiringScheduleEntity>>() {
        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: List<AiringScheduleEntity>): OutCome<Nothing?> {
            return runCatching {
                localSource.upsert(data)
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: List<AiringScheduleModel>
        ) = converter.convertFrom(source)
    }

    class Airing(
        override val localSource: AiringLocalSource,
        override val converter: AiringModelConverter
    ) : AiringMapper<AiringScheduleModel, AiringScheduleEntity>() {
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
}
