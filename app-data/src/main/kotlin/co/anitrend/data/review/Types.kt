/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.review

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.review.model.container.ReviewContainerModel
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.interactor.ReviewUseCase
import co.anitrend.domain.review.repository.IReviewRepository

internal typealias ReviewEntryController =  GraphQLController<ReviewContainerModel.Entry, ReviewEntity>
internal typealias ReviewDeleteController =  GraphQLController<ReviewContainerModel.DeletedEntry, Boolean>
internal typealias ReviewSaveController =  GraphQLController<ReviewContainerModel.SavedEntry, ReviewEntity>
internal typealias ReviewRateController =  GraphQLController<ReviewContainerModel.RatedEntry, ReviewEntity>
internal typealias ReviewPagedController =  GraphQLController<ReviewContainerModel.Paged, List<ReviewEntity>>

internal typealias ReviewEntryRepository = IReviewRepository.Entry<DataState<Review>>
internal typealias ReviewDeleteRepository = IReviewRepository.Delete<DataState<Boolean>>
internal typealias ReviewSaveRepository = IReviewRepository.Save<DataState<Boolean>>
internal typealias ReviewRateRepository = IReviewRepository.Rate<DataState<Boolean>>
internal typealias ReviewPagedRepository = IReviewRepository.Paged<DataState<PagedList<Review>>>

typealias GetReviewInteractor = ReviewUseCase.GetEntry<DataState<Review>>
typealias DeleteReviewInteractor = ReviewUseCase.DeleteEntry<DataState<Boolean>>
typealias SaveReviewInteractor = ReviewUseCase.SaveEntry<DataState<Boolean>>
typealias RateReviewInteractor = ReviewUseCase.RateEntry<DataState<Boolean>>
typealias GetReviewPagedInteractor = ReviewUseCase.GetPaged<DataState<PagedList<Review>>>