package co.anitrend.data.model.query.thread

import co.anitrend.data.model.contract.IGraphQuery

/** [ThreadComment query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the comment id
 * @param threadId Filter by the thread id
 * @param userId Filter by the user id of the comment's creator
 */
data class ThreadCommentQuery(
    val id: Int? = null,
    val threadId: Int? = null,
    val userId: Int? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "threadId" to threadId,
        "userId" to userId
    )
}