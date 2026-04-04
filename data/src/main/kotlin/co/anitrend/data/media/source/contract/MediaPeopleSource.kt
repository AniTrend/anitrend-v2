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
package co.anitrend.data.media.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.paging.AbstractPagingSource
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.model.query.MediaPeopleQuery
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow

internal class MediaPeopleSource {
    abstract class Characters : AbstractPagingSource<MediaPerson.Character>() {
        protected lateinit var query: MediaPeopleQuery.Characters

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<PagedList<MediaPerson.Character>>

        protected abstract suspend fun refreshCharacters(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Characters): Flow<PagedList<MediaPerson.Character>> {
            query = MediaPeopleQuery.Characters(param)
            cacheIdentity = MediaCache.Identity.Characters(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::refreshCharacters,
            )
            return observable()
        }

        override fun onItemAtEndLoaded(itemAtEnd: MediaPerson.Character) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
                block = { refreshCharacters(it) },
            )
        }

        override fun onItemAtFrontLoaded(itemAtFront: MediaPerson.Character) {
            /*cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
                block = { refreshCharacters(it) },
            )*/
        }

        override fun onZeroItemsLoaded() {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                block = { refreshCharacters(it) },
            )
        }
    }

    abstract class Staff : AbstractPagingSource<MediaPerson.Staff>() {
        protected lateinit var query: MediaPeopleQuery.Staff

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<PagedList<MediaPerson.Staff>>

        protected abstract suspend fun refreshStaff(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Staff): Flow<PagedList<MediaPerson.Staff>> {
            query = MediaPeopleQuery.Staff(param)
            cacheIdentity = MediaCache.Identity.Staff(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::refreshStaff,
            )
            return observable()
        }

        override fun onItemAtEndLoaded(itemAtEnd: MediaPerson.Staff) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
                block = { refreshStaff(it) },
            )
        }

        override fun onItemAtFrontLoaded(itemAtFront: MediaPerson.Staff) {
            /*cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
                block = { refreshStaff(it) },
            )*/
        }

        override fun onZeroItemsLoaded() {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                block = { refreshStaff(it) },
            )
        }
    }
}
