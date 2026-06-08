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
package co.anitrend.data.staff

import androidx.paging.PagingData
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.staff.model.container.StaffModelContainer
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.staff.interactor.StaffUseCase
import co.anitrend.domain.staff.repository.IStaffRepository
import kotlinx.coroutines.flow.Flow

internal typealias StaffPagedController = GraphQLController<StaffModelContainer.Paged, List<StaffEntity>>

internal typealias StaffPagingRepository = IStaffRepository.Paged<Flow<PagingData<Staff>>>

typealias GetPagingStaffInteractor = StaffUseCase.GetPaged<Flow<PagingData<Staff>>>
