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

package co.anitrend.data.genre.source.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.genre.cache.GenreCache
import co.anitrend.domain.genre.entity.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class MediaGenreSource(
    private val cachePolicy: ICacheStorePolicy,
    dispatcher: ISupportDispatcher
) : SupportCoreDataSource(dispatcher) {

    protected abstract fun observable(): Flow<List<Genre>>

    protected abstract suspend fun getGenres(callback: RequestCallback): Boolean

    internal operator fun invoke(): Flow<List<Genre>> {
        launch {
            requestHelper.runIfNotRunning(
                Request.Default(String.empty(), Request.Type.INITIAL)
            ) {
                if (cachePolicy.shouldRefresh(GenreCache.ID)) {
                    val success = getGenres(it)
                    if (success)
                        cachePolicy.updateLastRequest(GenreCache.ID)
                }
            }
        }
        return observable()
    }
}