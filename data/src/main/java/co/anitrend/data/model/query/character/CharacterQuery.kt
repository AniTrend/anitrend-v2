package co.anitrend.data.model.query.character

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.character.attributes.CharacterSort

/** [Character query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id: Filter by character id
 * @param search: Filter by search query
 * @param id_not: Filter by character id
 * @param id_in: Filter by character id
 * @param id_not_in: Filter by character id
 * @param sort: The order the results will be returned in
 */
data class CharacterQuery (
    val id: Int? = null,
    val search: String? = null,
    val id_not: Int? = null,
    val id_in: List<Int>? = null,
    val id_not_in: List<Int>? = null,
    @CharacterSort
    val sort: List<String>? = null
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