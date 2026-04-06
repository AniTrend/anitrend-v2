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
package co.anitrend.review.discover.component.content.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.arch.extension.ext.extra
import co.anitrend.data.review.GetPagingReviewInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.extensions.nameOf
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ReviewDiscoverViewModel(
    private val interactor: GetPagingReviewInteractor,
    settings: IUserSettings,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val default by savedStateHandle.extra(
        key = nameOf<ReviewDiscoverRouter.ReviewDiscoverParam>(),
        default = {
            ReviewDiscoverRouter.ReviewDiscoverParam(
                mediaType = MediaType.ANIME,
                sort =
                    listOf(
                        Sorting(
                            sortable = ReviewSort.CREATED_AT,
                            order = SortOrder.DESC,
                        ),
                    ),
                scoreFormat = settings.scoreFormat.value,
            )
        },
    )

    private val mutableParams = MutableStateFlow(default)

    val params: StateFlow<ReviewDiscoverRouter.ReviewDiscoverParam> = mutableParams.asStateFlow()

    val reviews: Flow<PagingData<Review>> = params.flatMapLatest(::query).cachedIn(viewModelScope)

    private fun query(param: ReviewDiscoverRouter.ReviewDiscoverParam): Flow<PagingData<Review>> =
        interactor(
            ReviewParam.Paged(
                mediaId = param.mediaId,
                userId = param.userId,
                mediaType = param.mediaType,
                sort = param.sort,
                scoreFormat = param.scoreFormat,
            ),
        )
}
