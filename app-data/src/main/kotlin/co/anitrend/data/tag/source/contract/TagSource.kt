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

package co.anitrend.data.tag.source.contract

import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.tag.cache.TagCache
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.domain.tag.model.TagParam
import kotlinx.coroutines.flow.Flow

internal abstract class TagSource : AbstractCoreDataSource() {

    protected lateinit var param: TagParam

    protected val cacheIdentity: CacheIdentity = TagCache.Identity.TAG

    protected abstract val cachePolicy: ICacheStorePolicy

    protected abstract fun observable(): Flow<List<Tag>>

    protected abstract suspend fun getTags(callback: RequestCallback): Boolean

    internal operator fun invoke(param: TagParam): Flow<List<Tag>> {
        this.param = param
        cachePolicy(
            scope = scope,
            requestHelper = requestHelper,
            cacheIdentity = cacheIdentity,
            block = ::getTags
        )

        return observable()
    }
}
