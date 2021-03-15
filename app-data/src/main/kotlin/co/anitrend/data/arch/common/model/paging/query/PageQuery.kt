/*
 * Copyright (C) 2019  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.data.arch.common.model.paging.query

import androidx.annotation.IntRange
import co.anitrend.arch.extension.util.pagination.contract.ISupportPagingHelper
import co.anitrend.data.arch.common.model.graph.IGraphPayload
import co.anitrend.data.util.graphql.GraphUtil

/** [Page query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param page The page number
 * @param perPage The amount of entries per page, max 50
 */

internal data class PageQuery(
    var page: Int,
    @IntRange(from = 0, to = 50)
    val perPage: Int = GraphUtil.PAGING_LIMIT
): IGraphPayload, ISupportPagingHelper {

    /**
     * Resets the paging parameters to their default
     */
    override fun onPageRefresh() {
        page = 0
    }

    /**
     * Calculates the previous page offset and index
     */
    override fun onPagePrevious() {
        page = page.dec()
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
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "page" to page,
        "perPage" to perPage
    )
}
