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
package co.anitrend.data.studio.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.studio.entity.StudioEntitySchema
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.support.query.builder.core.criteria.extensions.like
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.orderByAsc
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class StudioQueryFilter<T> : FilterQueryBuilder<T>() {
    class Search : StudioQueryFilter<StudioParam.Find>() {
        private val studioTable = StudioEntitySchema.tableName.asTable()
        private val nameColumn = StudioEntitySchema.name.asColumn(studioTable)

        private fun searchSelection(filter: StudioParam.Find) {
            filter.search?.trim()?.takeIf(String::isNotEmpty)?.also { term ->
                requireBuilder() whereAnd {
                    nameColumn.like(term)
                }
            }
        }

        override fun onBuildQuery(filter: StudioParam.Find) {
            requireBuilder() from studioTable
            searchSelection(filter)
            requireBuilder() orderByAsc nameColumn
        }
    }
}
