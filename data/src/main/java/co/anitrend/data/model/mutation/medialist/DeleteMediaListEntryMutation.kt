package co.anitrend.data.model.mutation.medialist

import co.anitrend.data.model.contract.IGraphQuery

/** [DeleteMediaListEntry mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Delete a media list entry
 *
 * @param id The id of the media list entry to delete
 */
data class DeleteMediaListEntryMutation(
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