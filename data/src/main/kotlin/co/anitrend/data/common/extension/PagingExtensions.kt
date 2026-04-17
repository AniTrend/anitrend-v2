/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.common.extension

import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import co.anitrend.data.common.model.paging.data.IPageModel
import co.anitrend.data.common.model.paging.query.PageQuery

internal fun SupportPagingHelper.toPageQuery() = PageQuery(page, pageSize)

/**
 * Update support paging helper based off of [pageModel]
 */
internal fun SupportPagingHelper.from(pageModel: IPageModel?) {
    val pageInfo = pageModel?.pageInfo ?: return

    pageInfo.currentPage?.let { currentPage ->
        page = currentPage
    }
    pageInfo.hasNextPage?.let { hasNextPage ->
        isPagingLimit = !hasNextPage
    }
}

/**
 * Restore paging state from already-persisted rows when a new paging session starts from cache.
 *
 * The sort index contract for AniTrend paging caches is a zero-based contiguous range per media.
 */
internal fun SupportPagingHelper.seedFromLocalCount(itemCount: Int) {
    if (itemCount <= 0) {
        return
    }

    page = ((itemCount - 1) / pageSize) + 1

    // A partial last page implies there can be no following page.
    if (itemCount % pageSize != 0) {
        isPagingLimit = true
    }
}
