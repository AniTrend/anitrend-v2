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
package co.anitrend.media.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.media.GetMediaStaffInteractor
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.staff.enums.StaffSort
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.Flow

class MediaStaffViewModel(
    private val interactor: GetMediaStaffInteractor,
) : ViewModel() {
    fun staff(mediaId: Long): Flow<PagingData<MediaPerson.Staff>> =
        interactor(
            MediaParam.Staff(
                id = mediaId,
                sort =
                    listOf(
                        Sorting(StaffSort.RELEVANCE, SortOrder.DESC),
                        Sorting(StaffSort.ROLE, SortOrder.ASC),
                    ),
            ),
        ).cachedIn(viewModelScope)
}
