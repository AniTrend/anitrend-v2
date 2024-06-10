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

package co.anitrend.data.relation.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.relation.converters.RelationEntityConverter
import co.anitrend.data.relation.datasource.local.RelationLocalSource
import co.anitrend.data.relation.datasource.remote.MoeRemoteSource
import co.anitrend.data.relation.source.contract.MoeController
import co.anitrend.data.relation.source.contract.RelationSource
import co.anitrend.domain.media.entity.attribute.origin.IMediaSourceId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class RelationSourceImpl(
    private val remoteSource: MoeRemoteSource,
    private val localSource: RelationLocalSource,
    private val controller: MoeController,
    private val clearDataHelper: IClearDataHelper,
    private val converter: RelationEntityConverter,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher
) : RelationSource() {

    override fun observable(): Flow<IMediaSourceId> {
        return localSource.withAniListIdFlow(query.id)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .map(converter::convertFrom)
            .distinctUntilChanged()
            .flowOn(coroutineContext)
    }

    override suspend fun getSourceRelation(callback: RequestCallback): Boolean {
        val deferred = deferred {
            remoteSource.getFromSource(
                id = query.id,
                source = query.source.type,
                include = query.includeRelations()
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
