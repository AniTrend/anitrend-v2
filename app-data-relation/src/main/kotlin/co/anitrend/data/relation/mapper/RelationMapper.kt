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

package co.anitrend.data.relation.mapper

import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.relation.converters.RelationModelConverter
import co.anitrend.data.relation.datasource.local.RelationLocalSource
import co.anitrend.data.relation.entity.RelationEntity
import co.anitrend.data.relation.model.remote.MoeModel

internal class RelationMapper(
    private val localSource: RelationLocalSource,
    private val converter: RelationModelConverter
) : DefaultMapper<MoeModel?, RelationEntity?>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: RelationEntity?) {
        runInTransaction {
            if (data != null)
                localSource.upsert(data)
        }
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: MoeModel?
    ) = source?.let { converter.convertFrom(it) }
}