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
package co.anitrend.common.review.ui.widget.vote.presenter

import android.content.Context
import androidx.work.Operation
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.common.review.ui.widget.vote.controller.ReviewVoteController
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.domain.review.entity.contract.IReview
import co.anitrend.domain.review.enums.ReviewRating
import co.anitrend.navigation.ReviewTaskRouter
import co.anitrend.navigation.extensions.createOneTimeUniqueWorker

internal class ReviewVotePresenter(
    context: Context,
    settings: Settings,
    entity: IReview,
) : CorePresenter(context, settings) {
    val controller by lazy(UNSAFE) {
        ReviewVoteController(entity)
    }

    fun rateReview(rating: ReviewRating): Operation {
        val params =
            ReviewTaskRouter.Param.RateEntry(
                id = controller.entity.id,
                rating = rating,
            )

        return ReviewTaskRouter.forReviewRateWorker()
            .createOneTimeUniqueWorker(context, params)
            .enqueue()
    }
}
