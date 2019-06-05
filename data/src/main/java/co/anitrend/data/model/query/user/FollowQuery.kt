package co.anitrend.data.model.query.user

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.user.attributes.UserSort

/** [Follow query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param userId User id of the follower/followed
 * @param sort The order the results will be returned in
 */
data class FollowQuery(
    val userId: Int,
    val sort: List<UserSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "userId" to userId,
        "sort" to sort
    )
}