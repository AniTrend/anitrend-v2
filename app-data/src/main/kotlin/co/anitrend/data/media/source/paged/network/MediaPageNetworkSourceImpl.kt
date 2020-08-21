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

package co.anitrend.data.media.source.paged.network

import androidx.lifecycle.liveData
import androidx.paging.toLiveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.arch.extension.controller
import co.anitrend.data.arch.helper.data.ClearDataHelper
import co.anitrend.data.media.converters.MediaEntityConverter
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.mapper.MediaPagedResponseMapper
import co.anitrend.data.media.source.paged.network.contract.MediaPagedNetworkSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async

internal class MediaPageNetworkSourceImpl(
    private val remoteSource: MediaRemoteSource,
    private val mapper: MediaPagedResponseMapper,
    private val strategy: ControllerStrategy<List<MediaEntity>>,
    private val clearDataHelper: ClearDataHelper,
    private val sortOrderSettings: ISortOrderSettings,
    private val converter: MediaEntityConverter = MediaEntityConverter(),
    dispatchers: SupportDispatchers
) : MediaPagedNetworkSource(dispatchers) {

    override val observable = liveData(coroutineContext) {
        val result = toLiveData(PAGING_CONFIGURATION)
        emitSource(result)
    }

    override suspend fun getMedia(
        callback: RequestCallback,
        pageInfo: IRequestHelper.RequestType
    ): List<Media>? {
        val deferred = async {
            val builder = query.toQueryContainerBuilder(supportPagingHelper, sortOrderSettings)
            remoteSource.getMediaPaged(builder)
        }

        val controller = mapper.controller(dispatchers, strategy)

        val result = controller(deferred, callback)

        return result?.let {
            converter.convertFrom(it)
        }
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {

    }
}