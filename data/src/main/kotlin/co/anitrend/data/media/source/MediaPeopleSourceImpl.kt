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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.media.MediaCharactersController
import co.anitrend.data.media.MediaStaffController
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaCharacterConnectionEntityConverter
import co.anitrend.data.media.converter.MediaStaffConnectionEntityConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.mapper.MediaPeopleMapper
import co.anitrend.data.media.model.query.MediaPeopleQuery
import co.anitrend.data.media.source.contract.MediaPeopleSource
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MediaPeopleSourceImpl {
    class Characters(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaCharactersController,
        private val mapper: MediaPeopleMapper.Characters,
        private val converter: MediaCharacterConnectionEntityConverter,
        private val cachePolicy: ICacheStorePolicy,
        private val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Characters() {
        override fun invoke(param: MediaParam.Characters): Flow<PagingData<MediaPerson.Character>> {
            val query = MediaPeopleQuery.Characters(param)
            val cacheIdentity = MediaCache.Identity.Characters(param)

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    MediaPeopleRemoteMediator.Characters(
                        cacheIdentity = cacheIdentity,
                        cachePolicy = cachePolicy,
                        query = query,
                        remoteSource = remoteSource,
                        localSource = localSource,
                        controller = controller,
                        mapper = mapper,
                        dispatcher = dispatcher,
                    ),
                pagingSourceFactory = { localSource.mediaCharactersPagingSource(param.id) },
            ).flow.map { pagingData ->
                pagingData.map(converter::convertFrom)
            }
        }
    }

    class Staff(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaStaffController,
        private val mapper: MediaPeopleMapper.Staff,
        private val converter: MediaStaffConnectionEntityConverter,
        private val cachePolicy: ICacheStorePolicy,
        private val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Staff() {
        override fun invoke(param: MediaParam.Staff): Flow<PagingData<MediaPerson.Staff>> {
            val query = MediaPeopleQuery.Staff(param)
            val cacheIdentity = MediaCache.Identity.Staff(param)

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    MediaPeopleRemoteMediator.Staff(
                        cacheIdentity = cacheIdentity,
                        cachePolicy = cachePolicy,
                        query = query,
                        remoteSource = remoteSource,
                        localSource = localSource,
                        controller = controller,
                        mapper = mapper,
                        dispatcher = dispatcher,
                    ),
                pagingSourceFactory = { localSource.mediaStaffPagingSource(param.id) },
            ).flow.map { pagingData ->
                pagingData.map(converter::convertFrom)
            }
        }
    }
}
