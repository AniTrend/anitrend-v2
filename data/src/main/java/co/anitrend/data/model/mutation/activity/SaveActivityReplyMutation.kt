package co.anitrend.data.model.mutation.activity

import co.anitrend.data.model.contract.IGraphQuery

/** [mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 *  Create or update an activity reply
 *
 * @param activityId The id of the parent activity being replied to
 * @param id The activity reply id, required for updating
 * @param text The reply text
 */
data class SaveActivityReplyMutation(
    val activityId: Int,
    val id: Int? = null,
    val text: String
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "activityId" to activityId,
        "id" to id,
        "text" to text
    )
}