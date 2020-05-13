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

package co.anitrend.data.util.graphql

import co.anitrend.data.arch.AniTrendExperimentalFeature
import co.anitrend.data.arch.common.model.paging.PageQuery
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import io.github.wax911.library.model.request.QueryContainerBuilder

/**
 * Graph request helper class
 */
internal object GraphUtil {

    private val SORT_ORDER_EXCEPTIONS = listOf(
        "SEARCH_MATCH"
    )

    private const val SORT_ORDER_DESC_POSTFIX = "_DESC"

    /**
     * Default per page loading limit for this application
     */
    const val PAGING_LIMIT = 30

    /**
     * Builder provider helper method, that provides a default GraphQL Query and Variable Builder
     * @param pageQuery paging
     */
    fun getDefaultQuery(pageQuery: PageQuery? = null): QueryContainerBuilder {
        val queryContainer = QueryContainerBuilder()
        pageQuery?.apply {
            queryContainer.putVariables(toMap())
        }
        return queryContainer
    }

    /**
     * Compacts the request body aiding in the shrinkage of the request payload
     */
    @AniTrendExperimentalFeature
    internal fun minify(rawData: String?): String? {
        if (!rawData.isNullOrBlank())
            return rawData.trimIndent()
        return rawData
    }

    /**
     * Applies order on sortable keys, if the key is not among the sort order exceptions
     *
     * @see SORT_ORDER_EXCEPTIONS
     */
    fun <E : Enum<E>> Enum<E>.applySortOrderUsing(settings: ISortOrderSettings): String {
        val sortType = name
        if (settings.isSortOrderDescending) {
            return if (SORT_ORDER_EXCEPTIONS.contains(sortType))
                sortType
            else
                sortType + SORT_ORDER_DESC_POSTFIX
        }
        return sortType
    }
}