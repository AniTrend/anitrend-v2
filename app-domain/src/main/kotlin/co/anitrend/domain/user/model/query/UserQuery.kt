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

package co.anitrend.domain.user.model.query

import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.user.enums.UserSort

/** [User query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param id Filter by the user id
 * @param name Filter by the name of the user
 * @param search Filter by search query
 * @param sort The order the results will be returned in
 */
data class UserQuery(
    val id: Long? = null,
    val name: String? = null,
    val search: String? = null,
    val sort: List<UserSort>? = null
) : IGraphPayload {

    /**
     * A map serializer to build maps out of objects to allow easier consumption in a GraphQL API
     */
    override fun toMap() = mapOf(
        "id" to id,
        "name" to name,
        "search" to search,
        "sort" to sort
    )
}