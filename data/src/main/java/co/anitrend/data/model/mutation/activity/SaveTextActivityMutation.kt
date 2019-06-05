package co.anitrend.data.model.mutation.activity

import co.anitrend.data.model.contract.IGraphQuery

/** [SaveTextActivity mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Create or update text activity for the currently authenticated user
 *
 * @param id The activity's id, required for updating
 * @param text The activity text
 */
data class SaveTextActivityMutation(
    val id: Int? = null,
    val text: String
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "text" to text
    )
}