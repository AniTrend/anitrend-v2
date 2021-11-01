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

package co.anitrend.domain.review.repository

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.review.model.ReviewParam

interface IReviewRepository {

    interface Entry<State: UiState<*>> : IReviewRepository {
        fun getEntry(param: ReviewParam.Entry): State
    }

    interface Paged<State: UiState<*>> : IReviewRepository {
        fun getPaged(param: ReviewParam.Paged): State
    }

    interface Rate<State: UiState<*>> : IReviewRepository {
        fun rate(param: ReviewParam.Rate): State
    }

    interface Save<State: UiState<*>> : IReviewRepository {
        fun save(param: ReviewParam.Save): State
    }

    interface Delete<State: UiState<*>> : IReviewRepository {
        fun delete(param: ReviewParam.Delete): State
    }
}