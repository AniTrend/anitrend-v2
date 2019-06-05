package co.anitrend.data.model.mutation.review

import co.anitrend.data.model.contract.IGraphQuery

/** [DeleteReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Delete a review
 *
 * @param id The id of the review to delete
 */
data class DeleteReviewMutation(
    val id: Int
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id
    )
}