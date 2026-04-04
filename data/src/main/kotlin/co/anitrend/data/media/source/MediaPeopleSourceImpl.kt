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

import androidx.paging.PagedList
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.MediaCharactersController
import co.anitrend.data.media.MediaStaffController
import co.anitrend.data.media.converter.MediaCharacterConnectionEntityConverter
import co.anitrend.data.media.converter.MediaStaffConnectionEntityConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.mapper.MediaPeopleMapper
import co.anitrend.data.media.source.contract.MediaPeopleSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.MediaPerson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class MediaPeopleSourceImpl {
    class Characters(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaCharactersController,
        private val mapper: MediaPeopleMapper.Characters,
        private val converter: MediaCharacterConnectionEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Characters() {
        override fun observable(): Flow<PagedList<MediaPerson.Character>> {
            val dataSourceFactory =
                localSource
                    .mediaCharactersFactory(query.param.id)
                    .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this,
            ).buildFlow()
        }

        override suspend fun refreshCharacters(requestCallback: RequestCallback): Boolean {
            mapper.onRequest(
                mediaId = query.param.id,
                page = supportPagingHelper.page,
            )

            val deferred =
                deferred {
                    val queryBuilder =
                        query.toQueryContainerBuilder(
                            supportPagingHelper,
                        )
                    remoteSource.getMediaCharacters(queryBuilder)
                }

            val result =
                controller(deferred, requestCallback) {
                    supportPagingHelper.from(it.media?.characters)
                    it
                }

            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) = Unit
    }

    class Staff(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaLocalSource,
        private val controller: MediaStaffController,
        private val mapper: MediaPeopleMapper.Staff,
        private val converter: MediaStaffConnectionEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Staff() {
        override fun observable(): Flow<PagedList<MediaPerson.Staff>> {
            val dataSourceFactory =
                localSource
                    .mediaStaffFactory(query.param.id)
                    .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this,
            ).buildFlow()
        }

        override suspend fun refreshStaff(requestCallback: RequestCallback): Boolean {
            mapper.onRequest(
                mediaId = query.param.id,
                page = supportPagingHelper.page,
            )

            val deferred =
                deferred {
                    val queryBuilder =
                        query.toQueryContainerBuilder(
                            supportPagingHelper,
                        )
                    remoteSource.getMediaStaff(queryBuilder)
                }

            val result =
                controller(deferred, requestCallback) {
                    supportPagingHelper.from(it.media?.staff)
                    it
                }

            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) = Unit
    }
}
