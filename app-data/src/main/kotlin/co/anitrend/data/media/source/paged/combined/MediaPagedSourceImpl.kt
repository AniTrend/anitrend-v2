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

package co.anitrend.data.media.source.paged.combined

import androidx.lifecycle.liveData
import androidx.paging.toLiveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.media.converter.MediaEntityConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.source.paged.combined.contract.MediaPagedCombinedController
import co.anitrend.data.media.source.paged.combined.contract.MediaPagedSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async

internal class MediaPagedSourceImpl(
    private val remoteSource: MediaRemoteSource,
    private val localSource: MediaLocalSource,
    private val clearDataHelper: IClearDataHelper,
    private val controller: MediaPagedCombinedController,
    private val sortOrderSettings: ISortOrderSettings,
    converter: MediaEntityConverter = MediaEntityConverter(),
    dispatchers: SupportDispatchers
) : MediaPagedSource(dispatchers) {

    override val observable = liveData {
        val result =  localSource.popularityDescFactory().map { converter.convertFrom(it) }
        emitSource(
            result.toLiveData(
                config = PAGING_CONFIGURATION,
                boundaryCallback = this@MediaPagedSourceImpl
            )
        )
    }

    override suspend fun getMedia(requestCallback: RequestCallback) {
        val deferred = async {
            val queryBuilder = query.toQueryContainerBuilder(
                supportPagingHelper,
                sortOrderSettings
            )
            remoteSource.getMediaPaged(queryBuilder)
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
            localSource.clear()
        }
    }
}