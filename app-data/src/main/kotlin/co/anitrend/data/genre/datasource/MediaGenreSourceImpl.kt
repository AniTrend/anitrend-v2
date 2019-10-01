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

package co.anitrend.data.genre.datasource

import androidx.lifecycle.LiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.data.extensions.controller
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.data.model.core.media.MediaGenre
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.get

internal class MediaGenreSourceImpl(
    private val mediaRemoteSource: MediaGenreRemoteSource,
    private val genreLocalSource: MediaGenreLocalSource
) : MediaGenreSource() {

    private val mapper = MediaGenreResponseMapper(genreLocalSource)

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     */
    override val observable =
        object : ISourceObservable<Nothing?, List<MediaGenre>> {

            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<List<MediaGenre>> {
                return genreLocalSource.findAllLiveData()
            }
        }

    /**
     * Handles dispatcher for requesting media tags
     */
    override fun getMediaGenres(queryContainerBuilder: QueryContainerBuilder): LiveData<List<MediaGenre>> {
        retry = { getMediaGenres(queryContainerBuilder) }
        val deferred = async {
            mediaRemoteSource.getMediaGenres(queryContainerBuilder)
        }

        launch {
            val controller =
                mapper.controller(connectivityHelper)

            controller(deferred, networkState)
        }

        return observable(null)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        genreLocalSource.deleteAll()
    }
}