/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.arch.helper.paging

import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.extension.util.pagination.SupportPagingHelper
import timber.log.Timber

/**
 * Paging utility to provide paging size from cached results
 * W.I.P
 */
internal object PagingConfigHelper {

    private fun setupPagingFrom(itemCount: Int, pagingHelper: SupportPagingHelper) {
        if (itemCount > 0) {
            val lastLoadedPage = itemCount / pagingHelper.pageSize
            pagingHelper.page = lastLoadedPage
            Timber.v(
                "Setting up paging -> items: $itemCount with pages: $lastLoadedPage"
            )
        }
    }

    /**
     * Since we plan on using a paging source backed by a database, Ideally we should
     * configure [pagingHelper] with the records count/paging limit to start load
     * from the last page of results in our backend.
     *
     * @param requestType Current request
     * @param pagingHelper paging helper
     * @param action what need to be run to return the number of available records
     */
    suspend inline operator fun invoke(
        requestType: Request.Type,
        pagingHelper: SupportPagingHelper,
        crossinline action: suspend () -> Int
    ) {
        when (requestType) {
            Request.Type.BEFORE -> {
                Timber.v("Triggered request: $requestType on paging helper configuration")
                if (!pagingHelper.isFirstPage()) {
                    runCatching {
                        // TODO: implement on previous paging calculator
                    }.onFailure {
                        Timber.e(it)
                    }
                }
            }
            Request.Type.AFTER -> {
                Timber.v("Triggered request: $requestType on paging helper configuration")
                if (pagingHelper.isInitialAfterFirstLoad()) {
                    runCatching {
                        val count = action()
                        setupPagingFrom(count, pagingHelper)
                    }.onFailure {
                        Timber.e(it)
                    }
                }
            }
            else -> {
                // We won't be support paging before a first item
                Timber.v("Ignoring request: $requestType on paging helper configuration")
            }
        }
    }

    private fun SupportPagingHelper.isInitialAfterFirstLoad() = page == 2
}