package co.anitrend.core.model.query

import androidx.annotation.IntRange
import co.anitrend.core.model.contract.IGraphQuery
import co.anitrend.core.util.graphql.GraphUtil

/** [Page query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param page The page number
 * @param perPage The amount of entries per page, max 50
 */
data class PageQuery(
    val page: Int,
    @IntRange(from = 0, to = 50)
    val perPage: Int = GraphUtil.PAGING_LIMIT
): IGraphQuery
