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

package co.anitrend.review.discover.component.content.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.extension.ext.extra
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.model.sorting.Sorting
import co.anitrend.review.discover.component.content.viewmodel.state.ReviewDiscoverState

class ReviewDiscoverViewModel(
    val state: ReviewDiscoverState,
    settings: IUserSettings,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        state.context = viewModelScope.coroutineContext
    }

    val default by savedStateHandle.extra(
        ReviewDiscoverRouter.Param.KEY,
        ReviewDiscoverRouter.Param(
            mediaType = MediaType.ANIME,
            sort = listOf(
                Sorting(
                    sortable = ReviewSort.CREATED_AT,
                    order = SortOrder.DESC
                )
            ),
            scoreFormat = settings.scoreFormat.value
        )
    )

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        state.onCleared()
        super.onCleared()
    }
}