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

package co.anitrend.data.genre.mapper

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.genre.converters.GenreModelConverter
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.model.remote.GenreCollection

internal class MediaGenreResponseMapper(
    private val localSource: MediaGenreLocalSource,
    private val mapper: GenreModelConverter = GenreModelConverter()
) : GraphQLMapper<GenreCollection, List<GenreEntity>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: GenreCollection
    )= mapper.convertTo(
        source.genreCollection
    )

    /**
     * Inserts the given object into the implemented room database
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<GenreEntity>) {
        if (mappedData.isNotEmpty())
            localSource.upsert(mappedData)
        else
            onEmptyResponse()
    }
}