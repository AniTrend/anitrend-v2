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

package co.anitrend.domain.review.interactor

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.review.model.ReviewParam
import co.anitrend.domain.review.repository.IReviewRepository

sealed class ReviewUseCase : IUseCase {

    abstract class GetEntry<State: UiState<*>>(
        protected val repository: IReviewRepository.Entry<State>
    ) : ReviewUseCase() {
        operator fun invoke(param: ReviewParam.Entry): State =
            repository.getEntry(param)
    }

    abstract class GetPaged<State: UiState<*>>(
        protected val repository: IReviewRepository.Paged<State>
    ) : ReviewUseCase() {
        operator fun invoke(param: ReviewParam.Paged): State =
            repository.getPaged(param)
    }

    abstract class RateEntry<State: UiState<*>>(
        protected val repository: IReviewRepository.Rate<State>
    ) : ReviewUseCase() {
        operator fun invoke(param: ReviewParam.Rate): State =
            repository.rate(param)
    }

    abstract class SaveEntry<State: UiState<*>>(
        protected val repository: IReviewRepository.Save<State>
    ) : ReviewUseCase() {
        operator fun invoke(param: ReviewParam.Save): State =
            repository.save(param)
    }

    abstract class DeleteEntry<State: UiState<*>>(
        protected val repository: IReviewRepository.Delete<State>
    ) : ReviewUseCase() {
        operator fun invoke(param: ReviewParam.Delete): State =
            repository.delete(param)
    }
}
