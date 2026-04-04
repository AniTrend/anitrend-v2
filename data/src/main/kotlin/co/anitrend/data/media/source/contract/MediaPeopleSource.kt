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

import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Default
import co.anitrend.arch.paging.legacy.source.live.SupportPagingLiveDataSource
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam

internal sealed class MediaPeopleSource {
    abstract class Characters :
        SupportPagingLiveDataSource<MediaParam.Characters, MediaPerson.Character>(),
        ISupportCoroutine by Default() {
        protected abstract val cacheIdentity: CacheIdentity

        protected abstract val initialKey: MediaParam.Characters

        protected abstract suspend fun getCharacters(
            param: MediaParam.Characters,
            callback: RequestCallback,
        ): List<MediaPerson.Character>

        override fun loadInitial(
            params: LoadInitialParams<MediaParam.Characters>,
            callback: LoadInitialCallback<MediaParam.Characters, MediaPerson.Character>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
            ) {
                val result = getCharacters(initialKey, it)
                callback.onResult(result, null, initialKey)
            }
        }

        override fun loadAfter(
            params: LoadParams<MediaParam.Characters>,
            callback: LoadCallback<MediaParam.Characters, MediaPerson.Character>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
            ) {
                val result = getCharacters(params.key, it)
                callback.onResult(result, params.key)
            }
        }

        override fun loadBefore(
            params: LoadParams<MediaParam.Characters>,
            callback: LoadCallback<MediaParam.Characters, MediaPerson.Character>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
            ) {
                val result = getCharacters(params.key, it)
                callback.onResult(result, params.key)
            }
        }
    }

    abstract class Staff :
        SupportPagingLiveDataSource<MediaParam.Staff, MediaPerson.Staff>(),
        ISupportCoroutine by Default() {
        protected abstract val cacheIdentity: CacheIdentity

        protected abstract val initialKey: MediaParam.Staff

        protected abstract suspend fun getStaff(
            param: MediaParam.Staff,
            callback: RequestCallback,
        ): List<MediaPerson.Staff>

        override fun loadInitial(
            params: LoadInitialParams<MediaParam.Staff>,
            callback: LoadInitialCallback<MediaParam.Staff, MediaPerson.Staff>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
            ) {
                val result = getStaff(initialKey, it)
                callback.onResult(result, null, initialKey)
            }
        }

        override fun loadAfter(
            params: LoadParams<MediaParam.Staff>,
            callback: LoadCallback<MediaParam.Staff, MediaPerson.Staff>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
            ) {
                val result = getStaff(params.key, it)
                callback.onResult(result, params.key)
            }
        }

        override fun loadBefore(
            params: LoadParams<MediaParam.Staff>,
            callback: LoadCallback<MediaParam.Staff, MediaPerson.Staff>,
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
            ) {
                val result = getStaff(params.key, it)
                callback.onResult(result, params.key)
            }
        }
    }
}
