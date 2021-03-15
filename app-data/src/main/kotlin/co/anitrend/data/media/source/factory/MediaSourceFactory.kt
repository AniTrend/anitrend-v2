/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.media.source.factory

import androidx.paging.DataSource
import co.anitrend.arch.data.source.live.SupportPagingLiveDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.media.MediaNetworkController
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.source.MediaSourceImpl
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.Delegates

internal sealed class MediaSourceFactory<Key, Value> : DataSource.Factory<Key, Value>() {

    protected abstract val stateSourceFlow: MutableStateFlow<SupportPagingLiveDataSource<Key, Value>?>

    class Network(
        private val remoteSource: MediaRemoteSource,
        private val controller: MediaNetworkController,
        private val sortOrderSettings: ISortOrderSettings,
        private val dispatcher: ISupportDispatcher
    ) : MediaSourceFactory<MediaParam.Find, Media>() {

        var initialKey by Delegates.notNull<MediaParam.Find>()

        override val stateSourceFlow = MutableStateFlow<SupportPagingLiveDataSource<MediaParam.Find, Media>?>(null)

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
            MediaSourceImpl.Network(
                remoteSource = remoteSource,
                controller = controller,
                sortOrderSettings = sortOrderSettings,
                dispatcher = dispatcher,
                initialKey = initialKey
            ).also { stateSourceFlow.value = it }
    }
}
