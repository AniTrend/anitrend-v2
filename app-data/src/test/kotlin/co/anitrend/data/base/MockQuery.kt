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

package co.anitrend.data.base

import co.anitrend.data.common.FuzzyDateLike
import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.common.sort.contract.ISortWithOrder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaStatus

class MockQuery(
    val isAdult: Boolean? = null,
    val id_in: List<Int>? = null,
    val status_in: List<MediaStatus>? = null,
    val startDate_like: FuzzyDateLike? = null,
    val sort: List<ISortWithOrder<MediaSort>>? = null
) : IGraphPayload {
    override fun toMap() = mapOf(
        "isAdult" to isAdult,
        "id_in" to id_in,
        "status_in" to status_in,
        "startDate_like" to startDate_like,
        "sort" to sort
    )
}