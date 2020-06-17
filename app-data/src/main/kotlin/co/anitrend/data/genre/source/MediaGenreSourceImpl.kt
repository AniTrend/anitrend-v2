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

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.policy.OnlineStrategy
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.genre.converters.GenreEntityConverter
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.data.genre.source.contract.MediaGenreSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MediaGenreSourceImpl(
    private val remoteSource: MediaGenreRemoteSource,
    private val localSource: MediaGenreLocalSource,
    private val mapper: MediaGenreResponseMapper,
    private val clearDataHelper: ClearDataHelper,
    converter: GenreEntityConverter = GenreEntityConverter(),
    dispatchers: SupportDispatchers
) : MediaGenreSource(dispatchers) {

    override val observable =
        localSource.findAllFlow().map {
            converter.convertFrom(it)
        }.flowOn(dispatchers.computation)

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