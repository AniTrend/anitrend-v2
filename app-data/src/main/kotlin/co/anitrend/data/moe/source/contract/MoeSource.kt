/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.moe.source.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.cache.extensions.invoke
import co.anitrend.data.cache.model.CacheIdentity
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.moe.cache.MoeCache
import co.anitrend.data.moe.model.local.MoeSourceQuery
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class MoeSource : SupportCoreDataSource() {

    protected lateinit var query: MoeSourceQuery

    protected lateinit var cacheIdentity: CacheIdentity

    protected abstract val cachePolicy: ICacheStorePolicy

    internal abstract fun observable(): Flow<IMediaSourceId>

    protected abstract suspend fun getSourceRelation(
        callback: RequestCallback
    ): Boolean

    operator fun invoke(sourceQuery: MoeSourceQuery) {
        query = sourceQuery
        cacheIdentity = MoeCache.Identity(sourceQuery)
        cachePolicy(scope, requestHelper, cacheIdentity) {
            getSourceRelation(it)
        }
    }
}