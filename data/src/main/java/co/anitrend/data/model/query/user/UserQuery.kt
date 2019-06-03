package co.anitrend.data.model.query.user

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.user.attributes.UserSort

/** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the user id
 * @param name Filter by the name of the user
 * @param search Filter by search query
 * @param sort The order the results will be returned in
 */
data class UserQuery(
    val id: Int? = null,
    val name: String? = null,
    val search: String? = null,
    @UserSort
    val sort: List<String>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "search" to search,
        "sort" to sort
    )
}