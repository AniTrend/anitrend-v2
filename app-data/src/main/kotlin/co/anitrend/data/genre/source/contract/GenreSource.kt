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

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.genre.cache.GenreCache
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.genre.model.GenreParam
import kotlinx.coroutines.flow.Flow

internal abstract class GenreSource : SupportCoreDataSource() {

    protected lateinit var param: GenreParam

    protected val cacheIdentity: CacheIdentity = GenreCache.Identity.GENRE

    protected abstract val cachePolicy: ICacheStorePolicy

    protected abstract fun observable(): Flow<List<Genre>>

    protected abstract suspend fun getGenres(callback: RequestCallback): Boolean

    internal operator fun invoke(param: GenreParam): Flow<List<Genre>> {
        this.param = param
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getGenres
        )

        return observable()
    }
}
