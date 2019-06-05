package co.anitrend.data.model.mutation.user

import co.anitrend.data.model.contract.IGraphQuery

/** [ToggleFollow mutation](https://anilist.github.io/ApiV2-GraphQL-Docs/mutation.doc.html)
 *
 * Toggle the un/following of a user
 *
 * @param userId The id of the user to un/follow
 */
data class ToggleFollowMutation(
    val userId: Int
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "userId" to userId
    )
}