/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.media.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.media.MediaRecommendationsController
import co.anitrend.data.media.MediaRelationsController
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.source.contract.MediaConnectionSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow

internal sealed class MediaConnectionSourceImpl {
    class Relations(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaRelationsController,
        override val dispatcher: ISupportDispatcher,
    ) : MediaConnectionSource.Relations() {
        override val observable: MutableStateFlow<List<MediaRelationEntry>?> = MutableStateFlow(null)

        override suspend fun getRelations(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    val queryBuilder = query.toQueryContainerBuilder()
                    remoteSource.getMediaRelations(queryBuilder)
                }

            observable.value = controller(deferred, requestCallback).orEmpty()
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            observable.value = null
        }
    }

    class Recommendations(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaRecommendationsController,
        override val dispatcher: ISupportDispatcher,
    ) : MediaConnectionSource.Recommendations() {
        override val observable: MutableStateFlow<List<MediaRecommendationEntry>?> = MutableStateFlow(null)

        override suspend fun getRecommendations(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    val queryBuilder = query.toQueryContainerBuilder()
                    remoteSource.getMediaRecommendations(queryBuilder)
                }

            observable.value = controller(deferred, requestCallback).orEmpty()
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            observable.value = null
        }
    }
}
