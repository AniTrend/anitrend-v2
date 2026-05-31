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
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.dsl.from

internal sealed class StaffQueryFilter<T> : FilterQueryBuilder<T>() {
    class Paged : StaffQueryFilter<StaffParam.Paged>() {
        private val staffTable = StaffEntitySchema.tableName.asTable()

        override fun onBuildQuery(filter: StaffParam.Paged) {
            requireBuilder() from staffTable
        }
    }
}
