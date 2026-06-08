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
package co.anitrend.data.character.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.character.CharacterPagedController
import co.anitrend.data.character.cache.CharacterCache
import co.anitrend.data.character.converter.CharacterEntityConverter
import co.anitrend.data.character.datasource.local.CharacterLocalSource
import co.anitrend.data.character.datasource.remote.CharacterRemoteSource
import co.anitrend.data.character.entity.filter.CharacterQueryFilter
import co.anitrend.data.character.model.query.CharacterQuery
import co.anitrend.data.character.source.contract.CharacterSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.character.model.CharacterParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CharacterSourceImpl {
    class Search(
        private val remoteSource: CharacterRemoteSource,
        private val localSource: CharacterLocalSource,
        private val controller: CharacterPagedController,
        private val converter: CharacterEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: CharacterQueryFilter.Search,
        private val dispatcher: ISupportDispatcher,
    ) : CharacterSource.Search() {
        override fun invoke(param: CharacterParam.Find): Flow<PagingData<Character>> {
            assignQuery(param)

            val source =
                CharacterPagingSource(
                    cacheIdentity = CharacterCache.Identity.Search(param),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    controller = controller,
                    clearDataHelper = clearDataHelper,
                    filter = filter,
                    query = CharacterQuery(param),
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
