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

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.data.media.source.paged.combined.contract.MediaPagedSource
import co.anitrend.data.media.source.paged.network.factory.MediaPagedNetworkSourceFactory
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.repository.MediaRepository

internal class MediaRepositoryImpl(
    private val source: MediaPagedSource,
    private val sourceFactory: MediaPagedNetworkSourceFactory
) : SupportRepository(source), MediaRepository<DataState<PagedList<Media>>> {

    override fun getMediaPaged(query: IGraphPayload) =
        source create source(query)

    override fun getMediaPagedByNetwork(
        query: IGraphPayload
    ) = sourceFactory.create() create
            LivePagedListBuilder(
                sourceFactory,
                PAGING_CONFIGURATION
            ).build()



    /**
     * Deals with cancellation of any pending or on going operations that the repository
     * might be working on
     */
    override fun onCleared() {
        super.onCleared()
        sourceFactory.getLatestSource()?.cancelAllChildren()
    }
}