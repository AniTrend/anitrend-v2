package co.anitrend.data.model.query

import androidx.annotation.IntRange
import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.util.graphql.GraphUtil

/** [Page query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param page The page number
 * @param perPage The amount of entries per page, max 50
 */
data class PageQuery(
    val page: Int,
    @IntRange(from = 0, to = 50)
    val perPage: Int = GraphUtil.PAGING_LIMIT
): IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "page" to page,
        "perPage" to perPage
    )
}
