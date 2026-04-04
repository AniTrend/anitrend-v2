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
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.MediaCharactersController
import co.anitrend.data.media.MediaStaffController
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.model.query.MediaPeopleQuery
import co.anitrend.data.media.source.contract.MediaPeopleSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam

internal class MediaPeopleSourceImpl {
    class Characters(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaCharactersController,
        override val initialKey: MediaParam.Characters,
        override val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Characters() {
        override val cacheIdentity = MediaCache.Identity.Characters(initialKey)

        override suspend fun getCharacters(
            param: MediaParam.Characters,
            callback: RequestCallback,
        ): List<MediaPerson.Character> {
            val query = MediaPeopleQuery.Characters(param)
            val deferred =
                deferred {
                    val queryBuilder =
                        query.toQueryContainerBuilder(
                            supportPagingHelper,
                        )
                    remoteSource.getMediaCharacters(queryBuilder)
                }

            return controller(deferred, callback) {
                supportPagingHelper.from(it.media?.characters)
                it
            }.orEmpty()
        }
    }

    class Staff(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaStaffController,
        override val initialKey: MediaParam.Staff,
        override val dispatcher: ISupportDispatcher,
    ) : MediaPeopleSource.Staff() {
        override val cacheIdentity = MediaCache.Identity.Staff(initialKey)

        override suspend fun getStaff(
            param: MediaParam.Staff,
            callback: RequestCallback,
        ): List<MediaPerson.Staff> {
            val query = MediaPeopleQuery.Staff(param)
            val deferred =
                deferred {
                    val queryBuilder =
                        query.toQueryContainerBuilder(
                            supportPagingHelper,
                        )
                    remoteSource.getMediaStaff(queryBuilder)
                }

            return controller(deferred, callback) {
                supportPagingHelper.from(it.media?.staff)
                it
            }.orEmpty()
        }
    }
}
