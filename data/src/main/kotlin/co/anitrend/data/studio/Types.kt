/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.studio

import androidx.paging.PagingData
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.mapper.StudioDetailPersistenceData
import co.anitrend.data.studio.model.remote.StudioDetailContainer
import co.anitrend.data.studio.model.remote.StudioPagedContainer
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.domain.studio.interactor.StudioUseCase
import co.anitrend.domain.studio.repository.IStudioRepository
import kotlinx.coroutines.flow.Flow

internal typealias MediaStudioDetailController = GraphQLController<StudioDetailContainer, StudioDetailPersistenceData>
internal typealias StudioPagedController = GraphQLController<StudioPagedContainer, List<StudioEntity>>
internal typealias StudioDetailRepository = IStudioRepository<DataState<StudioDetailData>>
internal typealias StudioSearchRepository = IStudioRepository.Search<Flow<PagingData<Studio>>>
typealias StudioDetailInteractor = StudioUseCase<DataState<StudioDetailData>>
typealias GetSearchStudioInteractor = StudioUseCase.GetSearch<Flow<PagingData<Studio>>>
