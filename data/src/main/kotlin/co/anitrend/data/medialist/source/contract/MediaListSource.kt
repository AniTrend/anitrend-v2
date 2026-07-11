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
package co.anitrend.data.medialist.source.contract

import androidx.paging.PagingData
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.medialist.cache.MediaListCache
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.model.MediaListParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class MediaListSource {
    abstract class Sync : AbstractCoreDataSource() {
        protected lateinit var query: MediaListParam.Collection

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun getMediaList(requestCallback: RequestCallback)

        operator fun invoke(param: MediaListParam.Collection): Flow<Boolean> {
            query = param
            invoke(block = ::getMediaList)
            return observable.filterNotNull()
        }
    }

    abstract class Entry : AbstractCoreDataSource() {
        protected lateinit var query: MediaListParam.Entry

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<Media>

        protected abstract suspend fun getEntry(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaListParam.Entry): Flow<Media> {
            query = param
            cacheIdentity = MediaListCache.Identity.Entry(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getEntry,
            )
            return observable()
        }
    }

    abstract class Paging {
        protected lateinit var query: MediaListParam.Paged

        abstract operator fun invoke(param: MediaListParam.Paged): Flow<PagingData<Media>>

        protected fun assignQuery(param: MediaListParam.Paged) {
            query = param
        }
    }

    abstract class SaveEntry : AbstractCoreDataSource() {
        protected lateinit var mutation: MediaListParam.SaveEntry

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun saveEntry(requestCallback: RequestCallback)

        operator fun invoke(param: MediaListParam.SaveEntry): Flow<Boolean> {
            mutation = param
            invoke(block = ::saveEntry)
            return observable.filterNotNull()
        }
    }

    abstract class SaveEntries : AbstractCoreDataSource() {
        protected lateinit var mutation: MediaListParam.SaveEntries

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun saveEntries(requestCallback: RequestCallback)

        operator fun invoke(param: MediaListParam.SaveEntries): Flow<Boolean> {
            mutation = param
            invoke(block = ::saveEntries)
            return observable.filterNotNull()
        }
    }

    abstract class DeleteEntry : AbstractCoreDataSource() {
        protected lateinit var mutation: MediaListParam.DeleteEntry

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(requestCallback: RequestCallback)

        operator fun invoke(param: MediaListParam.DeleteEntry): Flow<Boolean> {
            mutation = param
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class DeleteCustomList : AbstractCoreDataSource() {
        protected lateinit var mutation: MediaListParam.DeleteCustomList

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteCustomList(requestCallback: RequestCallback)

        operator fun invoke(param: MediaListParam.DeleteCustomList): Flow<Boolean> {
            mutation = param
            invoke(block = ::deleteCustomList)
            return observable.filterNotNull()
        }
    }
}
