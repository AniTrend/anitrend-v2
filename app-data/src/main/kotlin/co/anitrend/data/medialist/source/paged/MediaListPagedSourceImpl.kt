/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.medialist.source.paged

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.medialist.MediaListPagedController
import co.anitrend.data.medialist.converter.MediaListEntityViewConverter
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.datasource.remote.MediaListRemoteSource
import co.anitrend.data.medialist.source.paged.contract.MediaListPagedSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.medialist.entity.MediaList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

internal class MediaListPagedSourceImpl(
    private val remoteSource: MediaListRemoteSource,
    private val localSource: MediaListLocalSource,
    private val clearDataHelper: IClearDataHelper,
    private val controller: MediaListPagedController,
    private val sortOrderSettings: ISortOrderSettings,
    private val converter: MediaListEntityViewConverter,
    dispatcher: ISupportDispatcher
) : MediaListPagedSource(dispatcher) {

    override fun observable(): Flow<PagedList<MediaList>> {
        val dataSourceFactory = localSource
            .entryFactory(query.userId!!)
            .map { converter.convertFrom(it) }

        return FlowPagedListBuilder(
            dataSourceFactory,
            PAGING_CONFIGURATION,
            null,
            this
        ).buildFlow()
    }

    override suspend fun getMediaList(requestCallback: RequestCallback) {
        val deferred = async {
            val queryBuilder = query.toQueryContainerBuilder(settings = sortOrderSettings)
            remoteSource.getMediaListPaged(queryBuilder)
        }

        controller(deferred, requestCallback)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            localSource.clearByUserId(query.userId!!)
        }
    }
}