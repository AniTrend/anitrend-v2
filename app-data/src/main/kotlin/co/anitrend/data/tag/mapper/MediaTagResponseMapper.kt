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
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource
import co.anitrend.data.tag.model.remote.MediaTagCollection
import co.anitrend.data.media.model.remote.MediaTag
import io.github.wax911.library.model.body.GraphContainer
import timber.log.Timber

internal class MediaTagResponseMapper(
    private val mediaTagLocalSource: MediaTagLocalSource
) : GraphQLMapper<MediaTagCollection, List<MediaTag>>() {

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     * @see [handleResponse]
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: GraphContainer<MediaTagCollection>): List<MediaTag> {
        return source.data?.mediaTagCollection.orEmpty()
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     * @see [responseCallback]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<MediaTag>) {
        if (mappedData.isNotEmpty()) {
            val current = mediaTagLocalSource.count()
            if (current < 1)
                mediaTagLocalSource.insert(mappedData)
            else
                mediaTagLocalSource.upsert(mappedData)
        }
        else
            Timber.tag(moduleTag).i("onResponseDatabaseInsert(mappedData: List<Show>) -> mappedData is empty")
    }
}