/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.status.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.media.entity.MediaEntitySchema
import co.anitrend.data.status.entity.ListStatusSchema
import co.anitrend.domain.status.model.StatusParam
import co.anitrend.support.query.builder.core.criteria.extensions.equal
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.from.extentions.innerJoin
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.orderByAsc
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class StatusQueryFilter<T> : FilterQueryBuilder<T>() {
    class List : StatusQueryFilter<StatusParam.Find>() {
        private val statusTable = ListStatusSchema.tableName.asTable()
        private val mediaTable = MediaEntitySchema.tableName.asTable()

        /**
         * Staring point of the query builder, that should make use of [requireBuilder]
         * to add query objections
         */
        override fun onBuildQuery(filter: StatusParam.Find) {
            requireBuilder() from statusTable innerJoin mediaTable

            filter.userId?.also {
                requireBuilder() whereAnd {
                    ListStatusSchema.userId equal it
                } orderByAsc ListStatusSchema.sortIndex.asColumn()
            }
        }
    }
}
