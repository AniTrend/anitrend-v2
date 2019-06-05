package co.anitrend.data.model.mutation.review

import co.anitrend.data.model.contract.IGraphQuery

/** [SaveReview mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update a review
 *
 * @param id The review id, required for updating
 * @param mediaId The id of the media the review is of
 * @param body The main review text.
 * @param summary A short summary/preview of the review.
 * @param score Score of the show
 * @param private If the review should only be visible to its creator
 */
data class SaveReviewMutation(
    val id: Int? = null,
    val mediaId: Int,
    val body: String,
    val summary: String,
    val score: Int,
    val private: Boolean
) : IGraphQuery {

    /**
     * Summary validation Min:20, Max:120 characters
     *
     * @see summary
     */
    fun isSummaryValid() = summary.length in 20..120

    /**
     * Body validation Min:1500 characters
     *
     * @see body
     */
    fun isBodyValid() = body.length >= 1500

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "mediaId" to mediaId,
        "body" to body,
        "summary" to summary,
        "score" to score,
        "private" to private
    )
}