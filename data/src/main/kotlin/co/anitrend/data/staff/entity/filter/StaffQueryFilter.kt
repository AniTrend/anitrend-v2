/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.staff.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.staff.entity.StaffEntitySchema
import co.anitrend.domain.staff.model.StaffParam
import co.anitrend.support.query.builder.core.criteria.extensions.like
import co.anitrend.support.query.builder.core.criteria.extensions.or
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class StaffQueryFilter<T> : FilterQueryBuilder<T>() {
    class Paged : StaffQueryFilter<StaffParam.Paged>() {
        private val staffTable = StaffEntitySchema.tableName.asTable()
        private val fullName = "name_full".asColumn(staffTable)
        private val userPreferredName = "name_user_preferred".asColumn(staffTable)
        private val originalName = "name_original".asColumn(staffTable)

        private fun searchSelection(filter: StaffParam.Paged) {
            filter.search?.trim()?.takeIf(String::isNotEmpty)?.also { term ->
                requireBuilder() whereAnd {
                    (
                        fullName.like(term) or
                            userPreferredName.like(term) or
                            originalName.like(term)
                    )
                }
            }
        }

        override fun onBuildQuery(filter: StaffParam.Paged) {
            requireBuilder() from staffTable
            searchSelection(filter)
        }
    }
}
