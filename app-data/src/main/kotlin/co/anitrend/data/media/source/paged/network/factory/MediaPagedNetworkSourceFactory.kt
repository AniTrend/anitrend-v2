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

package co.anitrend.data.media.source.paged.network.factory

import androidx.paging.DataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.source.paged.network.MediaPagedNetworkSourceImpl
import co.anitrend.data.media.source.paged.network.contract.MediaPagedNetworkController
import co.anitrend.data.media.source.paged.network.contract.MediaPagedNetworkSource
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.flow.MutableStateFlow

internal class MediaPagedNetworkSourceFactory(
    private val remoteSource: MediaRemoteSource,
    private val controller: MediaPagedNetworkController,
    private val sortOrderSettings: ISortOrderSettings,
    private val dispatchers: SupportDispatchers
) : DataSource.Factory<IGraphPayload, Media>() {


    private val stateSourceFlow =
        MutableStateFlow<MediaPagedNetworkSource?>(null)

    fun getLatestSource() = stateSourceFlow.value

    /**
     * Create a DataSource.
     *
     * The DataSource should invalidate itself if the snapshot is no longer valid. If a
     * DataSource becomes invalid, the only way to query more data is to create a new DataSource
     * from the Factory.
     *
     * [androidx.paging.LivePagedListBuilder] for example will construct a new PagedList and DataSource
     * when the current DataSource is invalidated, and pass the new PagedList through the
     * `LiveData<PagedList>` to observers.
     *
     * @return the new DataSource.
     */
    override fun create() =
        MediaPagedNetworkSourceImpl(
            remoteSource = remoteSource,
            controller = controller,
            sortOrderSettings = sortOrderSettings,
            dispatchers = dispatchers
        ).also { stateSourceFlow.value = it }
}