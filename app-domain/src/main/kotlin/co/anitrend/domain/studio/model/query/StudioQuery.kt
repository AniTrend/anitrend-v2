/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.studio.model.query

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.studio.enums.StudioSort

/** [Studio query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the studio id
 * @param search Filter by search query
 * @param id_not Filter by the studio id
 * @param id_in Filter by the studio id
 * @param id_not_in Filter by the studio id
 * @param sort The order the results will be returned in
 */
data class StudioQuery(
    val id: Long? = null,
    val search: String? = null,
    val id_not: Long? = null,
    val id_in: List<Long>? = null,
    val id_not_in: List<Long>? = null,
    val sort: List<StudioSort>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to id,
        "search" to search,
        "id_not" to id_not,
        "id_in" to id_in,
        "id_not_in" to id_not_in,
        "sort" to sort
    )
}