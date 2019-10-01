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

package co.anitrend.data.arch.common.model.paging

import androidx.annotation.IntRange
import co.anitrend.arch.extension.util.pagination.contract.ISupportPagingHelper
import co.anitrend.domain.common.entity.IEntityPageQuery
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.data.util.graphql.GraphUtil

/** [Page query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param page The page number
 * @param perPage The amount of entries per page, max 50
 */
data class PageQuery(
    override var page: Int,
    @IntRange(from = 0, to = 50)
    override val perPage: Int = GraphUtil.PAGING_LIMIT
): IGraphPayload, IEntityPageQuery, ISupportPagingHelper {

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
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "page" to page,
        "perPage" to perPage
    )
}
