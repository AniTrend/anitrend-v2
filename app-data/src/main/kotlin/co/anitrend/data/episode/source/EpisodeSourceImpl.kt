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

package co.anitrend.data.episode.source

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.arch.helper.data.contract.IClearDataHelper
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.episode.EpisodePagedController
import co.anitrend.data.episode.converter.EpisodeEntityConverter
import co.anitrend.data.episode.datasource.local.EpisodeLocalSource
import co.anitrend.data.episode.datasource.remote.EpisodeRemoteSource
import co.anitrend.data.episode.source.contract.EpisodeSource
import co.anitrend.domain.episode.entity.Episode
import co.anitrend.domain.episode.model.EpisodeParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

internal sealed class EpisodeSourceImpl {

    internal class Detail(
        private val localSource: EpisodeLocalSource,
        private val converter: EpisodeEntityConverter,
        override val dispatcher: ISupportDispatcher
    ) : EpisodeSource.Detail() {

        override fun observable(param: EpisodeParam.Detail): Flow<Episode> {
            return localSource.episodeByIdFlow(param.id)
                    .flowOn(dispatcher.io)
                    .filterNotNull()
                    .map(converter::convertFrom)
                    .flowOn(dispatcher.computation)
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            // Not supported for this operation
        }
    }

    internal class Paged(
        private val remoteSource: EpisodeRemoteSource,
        private val localSource: EpisodeLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: EpisodePagedController,
        private val converter: EpisodeEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : EpisodeSource.Paged() {

        override fun observable(): Flow<PagedList<Episode>> {
            val dataSourceFactory = localSource
                .entryFactory()
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getEpisodes(requestCallback: RequestCallback): Boolean {
            val deferred = async {
                remoteSource.getLatestEpisodes(query.param.locale)
            }

            val result = controller(deferred, requestCallback)

            return !result.isNullOrEmpty()
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clear()
            }
        }
    }
}