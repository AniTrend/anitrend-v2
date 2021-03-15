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

package co.anitrend.data.medialist.source.paged.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.medialist.model.query.MediaListQuery
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.model.MediaListParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal abstract class MediaListPagedSource : SupportPagingDataSource<MediaList>() {

    protected lateinit var query: MediaListQuery.Paged

    protected abstract fun observable(): Flow<PagedList<MediaList>>

    protected abstract suspend fun getMediaList(requestCallback: RequestCallback)

    operator fun invoke(param: MediaListParam.Paged): Flow<PagedList<MediaList>> {
        query = MediaListQuery.Paged(param)
        return observable()
    }

    /**
     * Called when zero items are returned from an initial load of the PagedList's data source.
     */
    override fun onZeroItemsLoaded() {
        launch {
            requestHelper.runIfNotRunning(
                Request.Default("media_list_paged", Request.Type.INITIAL)
            ) { getMediaList(it) }
        }
    }
}