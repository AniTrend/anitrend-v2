/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.media.repository

import androidx.paging.PagedList
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaNetworkRepository
import co.anitrend.data.media.MediaPagedRepository
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.media.source.factory.MediaSourceFactory
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam

internal sealed class MediaRepository(
    source: ISupportCoroutine? = null
) : SupportRepository(source) {

    class Detail(
        private val source: MediaSource.Detail
    ) : MediaRepository(source), MediaDetailRepository {
        override fun getMedia(
            param: MediaParam.Detail
        ) = source create source(param)
    }

    class Paged(
        private val source: MediaSource.Paged
    ) : MediaRepository(source), MediaPagedRepository {
        override fun getPaged(
            param: MediaParam.Find
        ) = source create source(param)
    }

    class Network(
        private val source: MediaSourceFactory.Network
    ) : MediaRepository(), MediaNetworkRepository {
        override fun getPaged(
            param: MediaParam.Find
        ): DataState<PagedList<Media>> {
            source.initialKey = param
            val dataSource = source.create()

            return dataSource create FlowPagedListBuilder(
                source,
                PAGING_CONFIGURATION
            ).buildFlow()
        }

        /**
         * Deals with cancellation of any pending or on going operations that the repository
         * might be working on
         */
        override fun onCleared() {
            source.getLatestSource()?.invalidate()
        }
    }
}
