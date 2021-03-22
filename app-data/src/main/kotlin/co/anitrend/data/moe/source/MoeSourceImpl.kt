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

package co.anitrend.data.moe.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.moe.converters.SourceEntityConverter
import co.anitrend.data.moe.datasource.local.MoeLocalSource
import co.anitrend.data.moe.datasource.remote.MoeRemoteSource
import co.anitrend.data.moe.source.contract.MoeController
import co.anitrend.data.moe.source.contract.MoeSource
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MoeSourceImpl(
    private val remoteSource: MoeRemoteSource,
    private val localSource: MoeLocalSource,
    private val controller: MoeController,
    private val clearDataHelper: IClearDataHelper,
    private val converter: SourceEntityConverter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : MoeSource() {

    override fun observable(): Flow<IMediaSourceId> {
        return localSource.withAniListIdFlow(query.id)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .map(converter::convertFrom)
            .flowOn(coroutineContext)
    }

    override suspend fun getSourceRelation(callback: RequestCallback): Boolean {
        val deferred = async {
            remoteSource.getFromSource(
                query.source.type,
                query.id
            )
        }

        val result = controller(deferred, callback)

        return result != null
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clear(query.id)
        }
    }
}