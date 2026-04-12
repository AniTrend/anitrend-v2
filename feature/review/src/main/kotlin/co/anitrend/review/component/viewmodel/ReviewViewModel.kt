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
package co.anitrend.review.component.viewmodel

import androidx.lifecycle.SavedStateHandle
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.review.GetReviewInteractor
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.navigation.ReviewRouter
import co.anitrend.navigation.extensions.nameOf

class ReviewViewModel(
    private val interactor: GetReviewInteractor,
    savedStateHandle: SavedStateHandle,
) : AniTrendViewModelState<Review>() {
    private val param =
        requireNotNull(
            savedStateHandle.get<ReviewRouter.ReviewParam>(nameOf<ReviewRouter.ReviewParam>()),
        )

    val reviewId: Long = requireNotNull(param.id)

    operator fun invoke() {
        state.postValue(
            interactor(
                ReviewParam.Entry(
                    id = reviewId,
                ),
            ),
        )
    }
}
