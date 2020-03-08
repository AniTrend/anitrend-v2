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
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.extensions.controller
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.domain.genre.entities.Genre
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

internal class MediaGenreSourceImpl(
    private val remoteSource: MediaGenreRemoteSource,
    private val localSource: MediaGenreLocalSource,
    private val mapper: MediaGenreResponseMapper,
    private val connectivity: SupportConnectivity,
    dispatchers: SupportDispatchers
) : MediaGenreSource(dispatchers) {

    /**
     * Registers a dispatcher executing a unit of work and then returns a
     * [androidx.lifecycle.LiveData] observable
     */
    @ExperimentalCoroutinesApi
    override val observable =
        object : ISourceObservable<QueryContainerBuilder, List<Genre>> {

            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: QueryContainerBuilder): LiveData<List<Genre>> {
                val genreFlow = localSource.findAllX()
                return genreFlow.map {
                        it.map { entity ->
                            GenreEntity.transform(entity)
                        }
                    }
                    .flowOn(dispatchers.computation)
                    .asLiveData()
            }
        }

    /**
     * Handles dispatcher for requesting media tags
     */
    override fun getMediaGenres(queryContainerBuilder: QueryContainerBuilder): LiveData<List<Genre>> {
        retry = { getMediaGenres(queryContainerBuilder) }
        val deferred = async {
            remoteSource.getMediaGenres(queryContainerBuilder)
        }

        launch {
            val controller =
                mapper.controller(connectivity, dispatchers)

            controller(deferred, networkState)
        }

        return observable(queryContainerBuilder)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        localSource.clear()
    }
}