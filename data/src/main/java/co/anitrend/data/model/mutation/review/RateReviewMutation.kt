package co.anitrend.data.model.mutation.review

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.review.attributes.ReviewRating

/** [RateReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Rate a review
 *
 * @param reviewId The id of the review to rate
 * @param rating The rating to apply to the review
 */
data class RateReviewMutation(
    val reviewId: Int,
    val rating: ReviewRating
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "reviewId" to reviewId,
        "rating" to rating
    )
}