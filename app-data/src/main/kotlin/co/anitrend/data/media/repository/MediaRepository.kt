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
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaNetworkRepository
import co.anitrend.data.media.MediaPagedRepository
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.media.source.factory.MediaSourceFactory
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam

internal sealed class MediaRepository {

    class Detail(
        private val source: MediaSource.Detail
    ) : MediaRepository(), MediaDetailRepository {
        override fun getMedia(
            param: MediaParam.Detail
        ) = source create source(param)
    }

    class Paged(
        private val source: MediaSource.Paged
    ) : MediaRepository(), MediaPagedRepository {
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
    }
}
