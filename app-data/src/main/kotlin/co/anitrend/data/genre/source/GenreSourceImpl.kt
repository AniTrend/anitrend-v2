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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.genre.MediaGenreController
import co.anitrend.data.genre.converters.GenreEntityConverter
import co.anitrend.data.genre.datasource.local.GenreLocalSource
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.entity.filter.GenreQueryFilter
import co.anitrend.data.genre.source.contract.GenreSource
import co.anitrend.domain.genre.entity.Genre
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

internal class GenreSourceImpl(
    private val remoteSource: MediaGenreRemoteSource,
    private val localSource: GenreLocalSource,
    private val controller: MediaGenreController,
    private val clearDataHelper: IClearDataHelper,
    private val converter: GenreEntityConverter,
    private val filter: GenreQueryFilter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : GenreSource() {

    override fun observable(): Flow<List<Genre>> {
        return localSource.rawFlowList(
            filter.build(param)
        ).flowOn(dispatcher.io)
         .map(converter::convertFrom)
         .flowOn(dispatcher.computation)
    }

    override suspend fun getGenres(callback: RequestCallback): Boolean {
        val deferred = async {
            remoteSource.getMediaGenres(
                QueryContainerBuilder()
            )
        }

        val result = controller(deferred, callback)

        return !result.isNullOrEmpty()
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clear()
        }
    }
}