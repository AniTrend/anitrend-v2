/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.arch.database.filter

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.support.query.builder.core.QueryBuilder
import co.anitrend.support.query.builder.core.contract.AbstractQueryBuilder
import co.anitrend.support.query.builder.core.projection.Projection
import co.anitrend.support.query.builder.dsl.*
import timber.log.Timber

/**
 * Contract for a [QueryBuilder] builder to be used in conjunction with [androidx.room.RawQuery]
 */
internal abstract class FilterQueryBuilder<F> {

    private var hash: Int? = null
    private var builder: QueryBuilder? = null

    protected fun requireBuilder() = requireNotNull(builder) {
        "Required instance of builder has not yet been set"
    }

    /**
     * Staring point of the query builder, that should make use of [requireBuilder]
     * to add query objections
     */
    protected abstract fun onBuildQuery(filter: F)

    private fun asSupportSQLiteQuery(): SimpleSQLiteQuery {
        // Temporary filter since projections seem to be added into params
        val query = requireBuilder().build()
        val parameters = requireBuilder().buildParameters()
            .filterNot { it is Projection }
            .toTypedArray()
        Timber.d(
            "Generated SQL query: \n\r${query} \n\rparams: [${parameters.joinToString()}]"
        )
        return SimpleSQLiteQuery(query, parameters)
    }

    /**
     * Builds a [SupportSQLiteQuery] for raw query consumption
     *
     * @param filter Object that has filter options
     *
     * @return [SupportSQLiteQuery]
     */
    fun build(filter: F): SupportSQLiteQuery = when (hash) {
        filter.hashCode() -> {
            Timber.d("Reusing existing builder instance, filter hash has not changed")
            asSupportSQLiteQuery()
        }
        else -> {
            val filterHash = filter.hashCode()
            Timber.d("Creating new builder instance, old != new filter has: $hash -> $filterHash")
            hash = filterHash
            builder = QueryBuilder()
            onBuildQuery(filter)
            asSupportSQLiteQuery()
        }
    }

    companion object {
        /**
         * Helper to add an orderBy selector using the provided
         * [projection] and [sortOrder] to establish the direction
         *
         * @param caseSensitive If the order should be case sensitive
         */
        fun AbstractQueryBuilder.orderBy(
            projection: Projection,
            sortOrder: SortOrder,
            caseSensitive: Boolean = true
        ): AbstractQueryBuilder {
            when (caseSensitive) {
                true -> {
                    if (sortOrder == SortOrder.DESC) orderByDesc(projection)
                    else orderByAsc(projection)
                }
                false -> {
                    if (sortOrder == SortOrder.DESC) orderByDescCollate(projection)
                    else orderByAscCollate(projection)
                }
            }
            return this
        }
    }
}