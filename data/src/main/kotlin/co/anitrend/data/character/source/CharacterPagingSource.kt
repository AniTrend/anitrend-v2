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
package co.anitrend.data.character.source

import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.character.CharacterPagedController
import co.anitrend.data.character.datasource.local.CharacterLocalSource
import co.anitrend.data.character.datasource.remote.CharacterRemoteSource
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.entity.filter.CharacterQueryFilter
import co.anitrend.data.character.model.query.CharacterQuery
import co.anitrend.data.common.extension.from
import co.anitrend.data.graphql.anilist.GetCharacterPaged
import co.anitrend.data.graphql.anilist.GetCharacterPagedVariables
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class CharacterPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: CharacterRemoteSource,
    private val localSource: CharacterLocalSource,
    private val controller: CharacterPagedController,
    private val clearDataHelper: IClearDataHelper,
    private val filter: CharacterQueryFilter.Search,
    private val query: CharacterQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, CharacterEntity>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, CharacterEntity> =
        {
            localSource.rawPagingSource(filter.build(query.param))
        }

    private suspend fun getCharacter(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getCharacterPaged(
                    GraphQLRequest(
                        query = GetCharacterPaged.document,
                        operationName = GetCharacterPaged.name,
                        variables =
                            GetCharacterPagedVariables(
                                page = supportPagingHelper.page,
                                perPage = supportPagingHelper.pageSize,
                                id_in = query.param.id_in?.map { it.toInt() },
                                id_not = query.param.id_not?.toInt(),
                                id_not_in = query.param.id_not_in?.map { it.toInt() },
                                search = query.param.search,
                                sort =
                                    query.param.sort?.map {
                                        co.anitrend.data.graphql.anilist.CharacterSort
                                            .valueOf(it.name)
                                    },
                            ),
                    ),
                )
            }

        controller(deferred, requestCallback) {
            supportPagingHelper.from(it.page)
            it
        }
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getCharacter,
        )

        val result =
            loadState.first {
                it is LoadState.Success || it is LoadState.Error
            }

        return when (result) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = Request.Type.INITIAL,
            block = ::getCharacter,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            localSource.clear()
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
    ): MediatorResult =
        when (loadType) {
            REFRESH -> {
                clearDataSource(dispatcher.io)
                invoke(requestType = Request.Type.INITIAL)
            }

            PREPEND -> MediatorResult.Success(true)

            APPEND -> invoke(requestType = Request.Type.AFTER)
        }
}
