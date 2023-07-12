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

package co.anitrend.data.tag.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.tag.TagController
import co.anitrend.data.tag.converter.TagEntityConverter
import co.anitrend.data.tag.datasource.local.TagLocalSource
import co.anitrend.data.tag.datasource.remote.MediaTagRemoteSource
import co.anitrend.data.tag.entity.filter.TagQueryFilter
import co.anitrend.data.tag.source.contract.TagSource
import co.anitrend.domain.tag.entity.Tag
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class TagSourceImpl(
    private val remoteSource: MediaTagRemoteSource,
    private val localSource: TagLocalSource,
    private val controller: TagController,
    private val clearDataHelper: IClearDataHelper,
    private val converter: TagEntityConverter,
    private val filter: TagQueryFilter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : TagSource() {

    override fun observable(): Flow<List<Tag>> {
        return localSource.rawFlowList(
            filter.build(param)
        ).flowOn(dispatcher.io)
         .map(converter::convertFrom)
         .flowOn(dispatcher.computation)
    }

    override suspend fun getTags(callback: RequestCallback): Boolean {
        val deferred = async {
            remoteSource.getMediaTags(
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
