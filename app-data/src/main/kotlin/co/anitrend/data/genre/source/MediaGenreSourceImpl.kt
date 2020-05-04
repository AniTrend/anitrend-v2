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

package co.anitrend.data.genre.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.data.genre.source.contract.MediaGenreSource
import co.anitrend.domain.genre.entities.Genre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MediaGenreSourceImpl(
    private val remoteSource: MediaGenreRemoteSource,
    private val localSource: MediaGenreLocalSource,
    private val mapper: MediaGenreResponseMapper,
    private val clearDataHelper: ClearDataHelper,
    dispatchers: SupportDispatchers
) : MediaGenreSource(dispatchers) {

    @ExperimentalCoroutinesApi
    override val observable =
        object : ISourceObservable<Nothing?, List<Genre>> {

            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<List<Genre>> {
                val genreFlow = localSource.findAllFlow()
                return genreFlow.map {
                        it.map { entity ->
                            GenreEntity.transform(entity)
                        }
                    }
                    .flowOn(dispatchers.computation)
                    .asLiveData()
            }
        }

    override suspend fun getGenres() {
        val deferred = async {
            remoteSource.getMediaGenres()
        }

        val controller =
            mapper.controller(dispatchers, OnlineStrategy.create())

        controller(deferred, networkState)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        clearDataHelper {
            localSource.clear()
        }
    }
}