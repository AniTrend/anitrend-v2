/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.review.model.mutation

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.review.enums.ReviewRating
import kotlinx.android.parcel.Parcelize

/** [RateReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Rate a review
 *
 * @param reviewId The id of the review to rate
 * @param rating The rating to apply to the review
 */
@Parcelize
data class RateReviewMutation(
    val reviewId: Long,
    val rating: ReviewRating
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "reviewId" to reviewId,
        "rating" to rating
    )
}