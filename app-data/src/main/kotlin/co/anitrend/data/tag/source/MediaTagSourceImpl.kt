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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.tag.MediaTagController
import co.anitrend.data.tag.cache.TagCache
import co.anitrend.data.tag.converter.TagEntityConverter
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource
import co.anitrend.data.tag.datasource.remote.MediaTagRemoteSource
import co.anitrend.data.tag.source.contract.MediaTagSource
import io.github.wax911.library.model.request.QueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MediaTagSourceImpl(
    private val remoteSource: MediaTagRemoteSource,
    private val localSource: MediaTagLocalSource,
    private val controller: MediaTagController,
    private val clearDataHelper: IClearDataHelper,
    private val converter: TagEntityConverter,
    cachePolicy: ICacheStorePolicy,
    dispatcher: ISupportDispatcher
) : MediaTagSource(cachePolicy, dispatcher) {

    override fun observable() =
        localSource.findAllFlow()
            .flowOn(dispatcher.io)
            .map { converter.convertFrom(it) }
            .flowOn(dispatcher.computation)

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
            cachePolicy.invalidateLastRequest(TagCache.Identity.TAG.id)
            localSource.clear()
        }
    }
}