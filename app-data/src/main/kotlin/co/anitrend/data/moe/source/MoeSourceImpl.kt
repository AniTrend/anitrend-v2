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
import co.anitrend.data.moe.converters.SourceEntityConverter
import co.anitrend.data.moe.datasource.local.MoeLocalSource
import co.anitrend.data.moe.datasource.remote.MoeRemoteSource
import co.anitrend.data.moe.model.local.MoeSourceQuery
import co.anitrend.data.moe.source.contract.MoeController
import co.anitrend.data.moe.source.contract.MoeSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MoeSourceImpl(
    private val remoteSource: MoeRemoteSource,
    private val localSource: MoeLocalSource,
    private val controller: MoeController,
    private val clearDataHelper: IClearDataHelper,
    converter: SourceEntityConverter = SourceEntityConverter(),
    dispatcher: ISupportDispatcher
) : MoeSource(dispatcher) {

    override val observable =
        localSource.withAniListIdX(query.id)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .map { converter.convertFrom(it) }
            .flowOn(coroutineContext)


    override suspend fun getSourceRelation(query: MoeSourceQuery, callback: RequestCallback) {
        val deferred = async {
            remoteSource.getFromSource(query)
        }

        controller(deferred, callback)
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