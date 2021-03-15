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

import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.arch.database.settings.ISortOrderSettings
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
            requireBuilder().asSupportSQLiteQuery()
        }
        else -> {
            val filterHash = filter.hashCode()
            Timber.d("Creating new builder instance, old != new filter has: $hash -> $filterHash")
            hash = filterHash
            builder = QueryBuilder()
            onBuildQuery(filter)
            val query = requireBuilder().asSupportSQLiteQuery()
            Timber.d("Generated SQL query: \n\r${query.sql} \n\rparams: [${requireBuilder().buildParameters().joinToString()}]")
            query
        }
    }

    companion object {
        /**
         * Helper to add an orderBy selector using the provided
         * [projection] and [settings] to establish the direction
         *
         * @param caseSensitive If the order should be case sensitive
         */
        fun AbstractQueryBuilder.orderBy(
            projection: Projection,
            settings: ISortOrderSettings,
            caseSensitive: Boolean = true
        ): AbstractQueryBuilder {
            val isDesc = settings.isSortOrderDescending.value
            when (caseSensitive) {
                true -> {
                    if (isDesc) orderByDesc(projection)
                    else orderByAsc(projection)
                }
                false -> {
                    if (isDesc) orderByDescCollate(projection)
                    else orderByAscCollate(projection)
                }
            }
            return this
        }
    }
}