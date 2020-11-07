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

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.mapper.paged.MediaPagedNetworkMapper
import co.anitrend.data.media.source.paged.network.contract.MediaPagedNetworkController
import co.anitrend.data.media.source.paged.network.contract.MediaPagedNetworkSource
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async

internal class MediaPagedNetworkSourceImpl(
    private val remoteSource: MediaRemoteSource,
    private val controller: MediaPagedNetworkController,
    private val sortOrderSettings: ISortOrderSettings,
    dispatchers: SupportDispatchers
) : MediaPagedNetworkSource(dispatchers) {

    override suspend fun getMedia(
        callback: RequestCallback,
        pageInfo: IRequestHelper.RequestType
    ): List<Media>? {
        val deferred = async {
            val builder = query.toQueryContainerBuilder(
                supportPagingHelper,
                sortOrderSettings
            )
            remoteSource.getMediaPaged(builder)
        }

        return controller(deferred, callback)
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {

    }
}