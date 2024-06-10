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

package co.anitrend.data.review.usecase

import co.anitrend.data.review.DeleteReviewInteractor
import co.anitrend.data.review.GetReviewInteractor
import co.anitrend.data.review.GetReviewPagedInteractor
import co.anitrend.data.review.RateReviewInteractor
import co.anitrend.data.review.ReviewDeleteRepository
import co.anitrend.data.review.ReviewEntryRepository
import co.anitrend.data.review.ReviewPagedRepository
import co.anitrend.data.review.ReviewRateRepository
import co.anitrend.data.review.ReviewSaveRepository
import co.anitrend.data.review.SaveReviewInteractor

internal interface ReviewInteractor {

    class Entry(
        repository: ReviewEntryRepository
    ) : GetReviewInteractor(repository)

    class Delete(
        repository: ReviewDeleteRepository
    ) : DeleteReviewInteractor(repository)

    class Rate(
        repository: ReviewRateRepository
    ) : RateReviewInteractor(repository)

    class Save(
        repository: ReviewSaveRepository
    ) : SaveReviewInteractor(repository)

    class Paged(
        repository: ReviewPagedRepository
    ) : GetReviewPagedInteractor(repository)
}
