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

package co.anitrend.data.mapper.media

import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.datasource.local.media.MediaGenreDao
import co.anitrend.data.model.collection.GenreCollection
import co.anitrend.data.model.core.media.MediaGenre
import io.github.wax911.library.model.body.GraphContainer
import kotlinx.coroutines.Job
import timber.log.Timber

class MediaGenreMapper(
    private val mediaGenreDao: MediaGenreDao
) : GraphQLMapper<GenreCollection, List<MediaGenre>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     *
     * @see [invoke]
     */
    override suspend fun onResponseMapFrom(source: GraphContainer<GenreCollection>): List<MediaGenre> {
        return source.data?.genreCollection?.map {
            MediaGenre(it)
        }.orEmpty()
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     *
     * @see [invoke]
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaGenre>) {
        if (mappedData.isNotEmpty()) {
            val current = mediaGenreDao.count()
            if (current < 1)
                mediaGenreDao.insert(mappedData)
            else
                mediaGenreDao.upsert(mappedData)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}