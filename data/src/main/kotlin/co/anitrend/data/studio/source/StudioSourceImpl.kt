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
package co.anitrend.data.studio.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.studio.StudioPagedController
import co.anitrend.data.studio.cache.StudioCache
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.remote.StudioRemoteSource
import co.anitrend.data.studio.model.query.StudioQuery
import co.anitrend.data.studio.source.contract.StudioSource
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.model.StudioParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StudioSourceImpl {
    class Search(
        private val remoteSource: StudioRemoteSource,
        private val localSource: StudioLocalSource,
        private val controller: StudioPagedController,
        private val converter: StudioEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        private val dispatcher: ISupportDispatcher,
    ) : StudioSource.Search() {
        override fun invoke(param: StudioParam.Find): Flow<PagingData<Studio>> {
            assignQuery(param)

            val source =
                StudioPagingSource(
                    cacheIdentity = StudioCache.Identity.Search(param),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    controller = controller,
                    clearDataHelper = clearDataHelper,
                    query = StudioQuery(param),
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
