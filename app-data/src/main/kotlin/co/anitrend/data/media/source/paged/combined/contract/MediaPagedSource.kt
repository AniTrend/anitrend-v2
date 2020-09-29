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

package co.anitrend.data.media.source.paged.combined.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.contract.IRequestHelper
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.launch

internal abstract class MediaPagedSource(
    dispatchers: SupportDispatchers
) : SupportPagingDataSource<Media>(dispatchers) {

    protected lateinit var query: MediaQuery

    protected abstract val observable: LiveData<PagedList<Media>>

    protected abstract suspend fun getMedia(requestCallback: RequestCallback)

    operator fun invoke(mediaQuery: IGraphPayload): LiveData<PagedList<Media>> {
        query = mediaQuery as MediaQuery
        return observable
    }

    /**
     * Called when the item at the end of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     * No more data will be appended to the PagedList after this item.
     *
     * @param itemAtEnd The first item of PagedList
     */
    override fun onItemAtEndLoaded(itemAtEnd: Media) {
        launch {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.AFTER
            ) {
                supportPagingHelper.onPageNext()
                getMedia(it)
            }
        }
    }

    /**
     * Called when the item at the front of the PagedList has been loaded, and access has
     * occurred within [Config.prefetchDistance] of it.
     *
     * No more data will be prepended to the PagedList before this item.
     *
     * @param itemAtFront The first item of PagedList
     */
    override fun onItemAtFrontLoaded(itemAtFront: Media) {
        launch {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.BEFORE
            ) {
                supportPagingHelper.onPagePrevious()
                getMedia(it)
            }
        }
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        launch {
            requestHelper.runIfNotRunning(
                IRequestHelper.RequestType.INITIAL
            ) { getMedia(it) }
        }
    }
}