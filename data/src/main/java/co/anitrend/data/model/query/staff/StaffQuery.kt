package co.anitrend.data.model.query.staff

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.staff.attributes.StaffSort

/** [Staff query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id: Filter by the staff id
 * @param search: Filter by search query
 * @param id_not: Filter by the staff id
 * @param id_in: Filter by the staff id
 * @param id_not_in: Filter by the staff id
 * @param sort: The order the results will be returned in
 */
data class StaffQuery(
    val id: Int,
    val search: String,
    val id_not: Int,
    val id_in: List<Int>?,
    val id_not_in: List<Int>?,
    @StaffSort
    val sort: List<String>?
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "id" to id,
        "search" to search,
        "id_not" to id_not,
        "id_in" to id_in,
        "id_not_in" to id_not_in,
        "sort" to sort
    )
}