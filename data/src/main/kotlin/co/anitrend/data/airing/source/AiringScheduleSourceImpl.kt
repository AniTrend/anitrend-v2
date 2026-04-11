/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.airing.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.airing.AiringSchedulePagedController
import co.anitrend.data.airing.cache.AiringCache
import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.datasource.remote.AiringRemoteSource
import co.anitrend.data.airing.entity.filter.AiringQueryFilter
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.airing.source.contract.AiringScheduleSource
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.domain.airing.model.AiringParam
import co.anitrend.domain.media.entity.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AiringScheduleSourceImpl {
    class Paging(
        private val remoteSource: AiringRemoteSource,
        private val localSource: AiringLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: AiringSchedulePagedController,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: AiringQueryFilter.Paged,
        private val dispatcher: ISupportDispatcher,
    ) : AiringScheduleSource.Paging() {
        override fun invoke(param: AiringParam.Find): Flow<PagingData<Media>> {
            val source =
                AiringSchedulePagingSource(
                    cacheIdentity = AiringCache.Identity.Paged(),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    mediaLocalSource = mediaLocalSource,
                    controller = controller,
                    clearDataHelper = clearDataHelper,
                    filter = filter,
                    query = AiringScheduleQuery(param),
                    dispatcher = dispatcher,
                )

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator = source,
                pagingSourceFactory = source.pagingSourceFactory(),
            ).flow.map { pagingData -> pagingData.map { entity -> converter.convertFrom(entity) } }
        }
    }
}
