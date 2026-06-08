/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.staff.model.query

import co.anitrend.data.common.model.graph.IGraphPayload
import co.anitrend.domain.staff.model.StaffParam

internal sealed class StaffQuery : IGraphPayload {
    data class Find(
        val param: StaffParam.Find,
    ) : StaffQuery() {
        override fun toMap() =
            mapOf(
                "id" to param.id,
                "search" to param.search,
                "id_not" to param.id_not,
                "id_in" to param.id_in,
                "id_not_in" to param.id_not_in,
                "sort" to param.sort,
            )
    }

    data class Paged(
        val param: StaffParam.Paged,
    ) : StaffQuery() {
        override fun toMap() =
            mapOf(
                "id_in" to param.id_in,
                "id_not" to param.id_not,
                "id_not_in" to param.id_not_in,
                "search" to param.search,
                "sort" to param.sort,
                "isBirthday" to param.isBirthday,
            )
    }
}
