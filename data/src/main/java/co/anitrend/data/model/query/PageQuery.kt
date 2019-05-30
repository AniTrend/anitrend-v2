package co.anitrend.data.model.query

import android.os.Parcelable
import androidx.annotation.IntRange
import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.util.graphql.GraphUtil
import io.wax911.support.data.util.pagination.contract.ISupportPagingHelper
import kotlinx.android.parcel.Parcelize

/** [Page query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param page The page number
 * @param perPage The amount of entries per page, max 50
 */
@Parcelize
data class PageQuery(
    var page: Int,
    @IntRange(from = 0, to = 50)
    val perPage: Int = GraphUtil.PAGING_LIMIT
): IGraphQuery, ISupportPagingHelper, Parcelable {

    /**
     * Resets the paging parameters to their default
     */
    override fun onPageRefresh() {
        page = 0
    }

    /**
     * Calculates the next page offset and index
     */
    override fun onPageNext() {
        page = page.inc()
    }

    /**
     * Checks if the current page and offset is the first
     */
    override fun isFirstPage() = page == 1

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "page" to page,
        "perPage" to perPage
    )
}
