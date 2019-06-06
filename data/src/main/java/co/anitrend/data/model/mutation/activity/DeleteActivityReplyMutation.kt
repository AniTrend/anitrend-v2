package co.anitrend.data.model.mutation.activity

import co.anitrend.data.model.contract.IGraphQuery

/** [DeleteActivityReply mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Delete an activity reply of the authenticated users
 *
 * @param id The id of the activity to delete
 */
data class DeleteActivityReplyMutation(
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