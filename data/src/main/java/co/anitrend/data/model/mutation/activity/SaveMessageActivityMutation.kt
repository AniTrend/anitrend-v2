package co.anitrend.data.model.mutation.activity

import co.anitrend.data.model.contract.IGraphQuery

/** [SaveMessageActivity mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update message activity for the currently authenticated user
 *
 * @param id The activity id, required for updating
 * @param message The activity message text
 * @param recipientId The id of the user the message is being sent to
 */
data class SaveMessageActivityMutation(
    val id: Int? = null,
    val message: String,
    val recipientId: Int
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "message" to message,
        "recipientId" to recipientId
    )
}