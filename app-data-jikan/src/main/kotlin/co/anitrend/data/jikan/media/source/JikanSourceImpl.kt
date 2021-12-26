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

package co.anitrend.data.jikan.media.source

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.network.default.DefaultNetworkClient
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.jikan.media.JikanController
import co.anitrend.data.jikan.media.datasource.local.JikanLocalSource
import co.anitrend.data.jikan.media.datasource.remote.JikanRemoteSource
import co.anitrend.data.jikan.media.entity.projection.JikanWithConnection
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel
import co.anitrend.data.jikan.media.source.contract.JikanSource
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber
import java.util.*

internal class JikanSourceImpl(
    private val remoteSource: JikanRemoteSource,
    private val localSource: JikanLocalSource,
    private val controller: JikanController,
    private val clearDataHelper: IClearDataHelper,
    override val dispatcher: ISupportDispatcher,
    override val cachePolicy: ICacheStorePolicy
) : JikanSource() {

    override fun observable(): Flow<JikanWithConnection> {
        return localSource.withConnectionFlow(query.id)
            .flowOn(dispatcher.io)
            .filterNotNull()
            .distinctUntilChanged()
            .flowOn(coroutineContext)
    }

    private suspend fun getMediaInfo() = runCatching {
        val mediaType = query.type.name.uppercase()
        val deferred = async {
            remoteSource.getExtraInfo(query.id, mediaType)
        }
        DefaultNetworkClient<JikanMediaModel.MoreInfo>(dispatcher.io)
            .fetch(deferred)
    }.onFailure {
        Timber.e(it)
    }.getOrNull()

    override suspend fun getMedia(callback: RequestCallback): Boolean {
        val deferred = async {
            @Suppress("UNCHECKED_CAST")
            when (query.type) {
                MediaType.ANIME -> remoteSource.getAnimeDetails(query.id)
                else -> remoteSource.getMangaDetails(query.id)
            } as Response<JikanMediaModel>
        }

        val jikanMediaInfo = getMediaInfo()

        val result = controller(deferred, callback) { model ->
            when (model) {
                is JikanMediaModel.Anime -> model.copy(
                    moreInfo = jikanMediaInfo?.moreInfo
                )
                is JikanMediaModel.Manga -> model.copy(
                    moreInfo = jikanMediaInfo?.moreInfo
                )
            }
        }

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