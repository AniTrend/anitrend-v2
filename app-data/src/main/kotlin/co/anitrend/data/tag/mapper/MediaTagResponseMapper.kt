/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.tag.mapper

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.tag.converter.TagModelConverter
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.model.remote.MediaTagCollection

internal class MediaTagResponseMapper(
    private val localSource: MediaTagLocalSource,
    private val converter: TagModelConverter = TagModelConverter()
) : GraphQLMapper<MediaTagCollection, List<TagEntity>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: MediaTagCollection
    ) =
        converter.convertFrom(
            source.mediaTagCollection
        )

    /**
     * Inserts the given object into the implemented room database
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<TagEntity>) {
        if (mappedData.isNotEmpty())
            localSource.upsert(mappedData)
        else
            onEmptyResponse()
    }
}