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

package co.anitrend.review.discover.component.content.viewmodel.state

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagedList
import co.anitrend.arch.extension.ext.extra
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.review.GetReviewPagedInteractor
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.enums.ReviewSort
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.navigation.model.sorting.Sorting

class ReviewDiscoverState(
    override val interactor: GetReviewPagedInteractor,
    settings: IUserSettings,
    savedStateHandle: SavedStateHandle,
): AniTrendViewModelState<PagedList<Review>>() {

    val default by savedStateHandle.extra(
        ReviewDiscoverRouter.ReviewDiscoverParam(
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

    operator fun invoke(param: ReviewDiscoverRouter.ReviewDiscoverParam) {
        val query = ReviewParam.Paged(
            mediaId = param.mediaId,
            userId = param.userId,
            mediaType = param.mediaType,
            sort = param.sort,
            scoreFormat = param.scoreFormat
        )
        val result = interactor(query)
        state.postValue(result)
    }
}
