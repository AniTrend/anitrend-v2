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

package co.anitrend.data.genre.entity.filter

import co.anitrend.data.arch.database.filter.FilterQueryBuilder
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.genre.entity.GenreEntity

internal class GenreQueryFilter : FilterQueryBuilder<ISortOrderSettings>() {
    /**
     * Staring point of the query builder, that should make use of [requireBuilder]
     * to add query objections
     */
    override fun onBuildQuery(filter: ISortOrderSettings) {
        //val direction = filter.asOrderBy()
        val column = GenreEntity::genre
        //builder.orderBy("$column $direction")
    }
}