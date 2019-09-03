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

package co.anitrend.data.datasource.remote.media

import androidx.lifecycle.LiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.data.api.endpoint.MediaEndPoint
import co.anitrend.data.arch.mapper.GraphQLMapper
import co.anitrend.data.datasource.local.media.MediaGenreDao
import co.anitrend.data.datasource.remote.media.contract.MediaGenreSource
import co.anitrend.data.mapper.media.MediaGenreMapper
import co.anitrend.data.model.collection.GenreCollection
import co.anitrend.data.model.core.media.MediaGenre
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MediaGenreSourceImpl(
    private val mediaEndPoint: MediaEndPoint,
    private val genreDao: MediaGenreDao
) : MediaGenreSource() {

    override val mapper = MediaGenreMapper(genreDao)

    /**
     * Handles dispatcher for requesting media tags
     */
    override fun getMediaGenres(queryContainerBuilder: QueryContainerBuilder): LiveData<List<MediaGenre>> {
        networkState.value = NetworkState.Loading
        retry = { getMediaGenres(queryContainerBuilder) }

        val deferred = async {
            mediaEndPoint.getMediaGenres(queryContainerBuilder)
        }

        launch {
            val state = mapper(deferred)
            networkState.postValue(state)
        }

        return observable(null)
    }

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     */
    override val observable: ISourceObservable<List<MediaGenre>, Nothing?> =
        object : ISourceObservable<List<MediaGenre>, Nothing?> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<List<MediaGenre>> {
                return genreDao.findAllLiveData()
            }
        }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override fun clearDataSource() {
        launch {
            genreDao.deleteAll()
        }
    }
}