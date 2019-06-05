package co.anitrend.data.model.mutation.thread

import co.anitrend.data.model.contract.IGraphQuery

/** [SaveThreadComment mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update a thread comment
 *
 * @param id The comment id, required for updating
 * @param threadId The id of thread the comment belongs to
 * @param parentCommentId The id of thread comment to reply to
 * @param comment The comment markdown text
 */
data class SaveThreadCommentMutation(
    val id: Int? = null,
    val threadId: Int,
    val parentCommentId: Int,
    val comment: String
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "threadId" to threadId,
        "parentCommentId" to parentCommentId,
        "comment" to comment
    )
}