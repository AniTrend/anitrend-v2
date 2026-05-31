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
package co.anitrend.data.staff.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.staff.StaffPagedController
import co.anitrend.data.staff.cache.StaffCache
import co.anitrend.data.staff.converter.StaffEntityConverter
import co.anitrend.data.staff.datasource.local.StaffLocalSource
import co.anitrend.data.staff.datasource.remote.StaffRemoteSource
import co.anitrend.data.staff.entity.filter.StaffQueryFilter
import co.anitrend.data.staff.source.contract.StaffSource
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.staff.model.StaffParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class StaffSourceImpl {
    class Paging(
        private val remoteSource: StaffRemoteSource,
        private val localSource: StaffLocalSource,
        private val controller: StaffPagedController,
        private val converter: StaffEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: StaffQueryFilter.Paged,
        override val dispatcher: ISupportDispatcher,
    ) : StaffSource.Paging() {
        override fun invoke(param: StaffParam.Paged): Flow<PagingData<Staff>> {
            assignQuery(param)

            val source =
                StaffPagingSource(
                    cacheIdentity = StaffCache.Identity.Paged(param),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    controller = controller,
                    clearDataHelper = clearDataHelper,
                    filter = filter,
                    query = query,
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

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                localSource.clear()
            }
        }
    }
}
