/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.media.source.contract

import androidx.paging.PagingData
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow

internal class MediaSource {
    abstract class Detail : AbstractCoreDataSource() {
        protected lateinit var query: MediaQuery.Detail

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<Media>

        protected abstract suspend fun getMedia(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Detail): Flow<Media> {
            query = MediaQuery.Detail(param)
            cacheIdentity = MediaCache.Identity.Detail(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getMedia,
            )
            return observable()
        }
    }

    abstract class Studios : AbstractCoreDataSource() {
        protected lateinit var query: MediaQuery.Studios

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<List<MediaStudioEntry>>

        protected abstract suspend fun refreshStudios(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Studios): Flow<List<MediaStudioEntry>> {
            query = MediaQuery.Studios(param)
            cacheIdentity = MediaCache.Identity.Studios(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::refreshStudios,
            )
            return observable()
        }
    }

    abstract class Stats : AbstractCoreDataSource() {
        protected lateinit var query: MediaQuery.Stats

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<MediaStats>

        protected abstract suspend fun refreshStats(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Stats): Flow<MediaStats> {
            query = MediaQuery.Stats(param)
            cacheIdentity = MediaCache.Identity.Stats(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::refreshStats,
            )
            return observable()
        }
    }

    abstract class Paging {
        protected lateinit var query: MediaQuery.Find

        abstract operator fun invoke(param: MediaParam.Find): Flow<PagingData<Media>>

        protected fun assignQuery(param: MediaParam.Find) {
            query = MediaQuery.Find(param)
        }
    }
}
