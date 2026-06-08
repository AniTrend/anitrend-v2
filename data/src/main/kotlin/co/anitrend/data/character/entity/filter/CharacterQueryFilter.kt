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
package co.anitrend.data.character.entity.filter

import co.anitrend.data.android.filter.FilterQueryBuilder
import co.anitrend.data.character.entity.CharacterEntitySchema
import co.anitrend.domain.character.model.CharacterParam
import co.anitrend.support.query.builder.core.criteria.extensions.like
import co.anitrend.support.query.builder.core.criteria.extensions.or
import co.anitrend.support.query.builder.core.from.extentions.asTable
import co.anitrend.support.query.builder.core.projection.extensions.asColumn
import co.anitrend.support.query.builder.dsl.from
import co.anitrend.support.query.builder.dsl.whereAnd

internal sealed class CharacterQueryFilter<T> : FilterQueryBuilder<T>() {
    class Search : CharacterQueryFilter<CharacterParam.Find>() {
        private val characterTable = CharacterEntitySchema.tableName.asTable()

        private val fullName = CharacterEntitySchema.nameFull.asColumn(characterTable)
        private val userPreferredName = CharacterEntitySchema.nameUser_preferred.asColumn(characterTable)
        private val originalName = CharacterEntitySchema.nameOriginal.asColumn(characterTable)

        private fun searchSelection(filter: CharacterParam.Find) {
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

        override fun onBuildQuery(filter: CharacterParam.Find) {
            requireBuilder() from characterTable
            searchSelection(filter)
        }
    }
}
