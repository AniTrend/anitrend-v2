/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.util

import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.data.common.extension.toPageQuery
import co.anitrend.domain.common.sort.contract.ISortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder

/**
 * Graph request helper class
 */
internal object GraphUtil {
    private val SORT_ORDER_EXCEPTIONS =
        listOf(
            "SEARCH_MATCH",
            "RELEVANCE",
        )

    private const val SORT_ORDER_DESC_POSTFIX = "_DESC"

    /**
     * Default per page loading limit for this application
     */
    const val PAGING_LIMIT = 30

    /**
     * Applies order on sortable keys, if the key is not among the sort order exceptions
     *
     * @see SORT_ORDER_EXCEPTIONS
     */
    fun ISortWithOrder<*>.applySortOrderUsing(): String {
        val sortType = (sortable as Enum<*>).name
        if (order == SortOrder.DESC) {
            return if (SORT_ORDER_EXCEPTIONS.contains(sortType)) {
                sortType
            } else {
                sortType + SORT_ORDER_DESC_POSTFIX
            }
        }
        return sortType
    }
}
