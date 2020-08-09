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

package co.anitrend.data.source.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.extension.fetchBodyWithRetry
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.source.converters.SourceEntityConverter
import co.anitrend.data.source.datasource.local.SourceLocal
import co.anitrend.data.source.datasource.remote.SourceRemote
import co.anitrend.data.source.entity.SourceEntity
import co.anitrend.data.source.mapper.SourceResponseMapper
import co.anitrend.data.source.model.local.SourceQuery
import co.anitrend.data.source.source.contract.SourceRelation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class SourceRelationImpl(
    private val remoteSource: SourceRemote,
    private val localSource: SourceLocal,
    private val mapper: SourceResponseMapper,
    private val clearDataHelper: ClearDataHelper,
    private val strategy: ControllerStrategy<SourceEntity>,
    converter: SourceEntityConverter = SourceEntityConverter(),
    dispatchers: SupportDispatchers
) : SourceRelation(dispatchers) {

    override val observable =
        localSource.withAniListIdX(query.id)
            .flowOn(dispatchers.io)
            .filterNotNull()
            .map { converter.convertFrom(it) }
            .flowOn(coroutineContext)


    override suspend fun getSourceRelation(query: SourceQuery, callback: RequestCallback) {
        mapper.anilistId = query.id
        val deferred = async {
            remoteSource.getFromSource(query)
        }

        strategy(callback) {
            val response = deferred.fetchBodyWithRetry(dispatchers.io)
            response?.let { data ->
                val mapped = mapper.onResponseMapFrom(data)
                withContext(dispatchers.io) {
                    mapper.onResponseDatabaseInsert(mapped)
                }
                mapped
            }
        }
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            localSource.clear(query.id)
        }
    }
}