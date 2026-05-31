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
package co.anitrend.data.user.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.user.entity.UserEntitySchema
import co.anitrend.domain.user.enums.UserSort
import co.anitrend.domain.user.model.UserParam
import co.anitrend.support.query.builder.core.criteria.extensions.match
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class UserQueryFilter<T> : FilterQueryBuilder<T>() {
    class Search : UserQueryFilter<UserParam.Search>() {
        private val userTable = UserEntitySchema.tableName.asTable()

        private val userNameColumn = "user_name".asColumn(userTable)

        private val idColumn = UserEntitySchema.id.asColumn(userTable)

        private fun searchSelection(filter: UserParam.Search) {
            val term = filter.search
            requireBuilder() whereAnd {
                userNameColumn.match(term)
            }
        }

        private fun order(filter: UserParam.Search) {
            filter.sort?.forEach { sort ->
                with(requireBuilder()) {
                    when (sort.sortable) {
                        UserSort.SEARCH_MATCH -> {
                            orderBy(userNameColumn, sort.order)
                        }

                        UserSort.USERNAME -> {
                            orderBy(userNameColumn, sort.order)
                        }

                        UserSort.ID -> {
                            orderBy(idColumn, sort.order)
                        }

                        else -> {
                            val qualifier = sort.sortable.name.lowercase()
                            orderBy(qualifier.asColumn(userTable), sort.order)
                        }
                    }
                }
            }
        }

        override fun onBuildQuery(filter: UserParam.Search) {
            requireBuilder() from userTable
            searchSelection(filter)
            order(filter)
        }
    }
}
