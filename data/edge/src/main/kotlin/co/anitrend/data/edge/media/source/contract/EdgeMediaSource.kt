/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.media.source.contract

import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.edge.media.cache.EdgeMediaCache
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import kotlinx.coroutines.flow.Flow

abstract class EdgeMediaSource : AbstractCoreDataSource() {
    protected lateinit var cacheIdentity: CacheIdentity

    protected abstract val cachePolicy: ICacheStorePolicy

    internal abstract fun observable(id: Long): Flow<EdgeMediaEntity>

    protected abstract suspend fun getMediaById(callback: RequestCallback): Boolean

    operator fun invoke(id: Long): Flow<EdgeMediaEntity> {
        cacheIdentity = EdgeMediaCache.Identity(id = id)
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getMediaById,
        )
        return observable(id)
    }
}
