package co.anitrend.data.model.mutation.thread

import co.anitrend.data.model.contract.IGraphQuery

/** [DeleteThread mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 *  Delete a thread
 *
 *  @param id The id of the thread to delete
 */
data class DeleteThreadMutation(
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