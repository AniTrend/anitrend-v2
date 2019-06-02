package co.anitrend.data.model.query.thread

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.thread.attributes.ThreadSort

/** [Thread query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id: Filter by the thread id
 * @param userId: Filter by the user id of the thread's creator
 * @param replyUserId: Filter by the user id of the last user to comment on the thread
 * @param subscribed: Filter by if the currently authenticated user's subscribed threads
 * @param categoryId: Filter by thread category id
 * @param mediaCategoryId: Filter by thread media id category
 * @param search: Filter by search query
 * @param id_in: Filter by the thread id
 * @param sort: The order the results will be returned in
 */
data class ThreadQuery(
    val id: Int? = null,
    val userId: Int? = null,
    val replyUserId: Int? = null,
    val subscribed: Boolean? = null,
    val categoryId: Int? = null,
    val mediaCategoryId: Int? = null,
    val search: String? = null,
    val id_in: List<Int>? = null,
    @ThreadSort
    val sort: List<String>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "userId" to userId,
        "replyUserId" to replyUserId,
        "subscribed" to subscribed,
        "categoryId" to categoryId,
        "mediaCategoryId" to mediaCategoryId,
        "search" to search,
        "id_in" to id_in,
        "sort" to sort
    )
}