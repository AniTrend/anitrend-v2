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

package co.anitrend.data.model.query.user

import co.anitrend.data.model.contract.IGraphQuery
import co.anitrend.data.repository.user.attributes.UserSort

/** [Follow query](https://anilist.github.io/ApiV2-GraphQL-Docs/query.doc.html)
 *
 * @param userId User id of the follower/followed
 * @param sort The order the results will be returned in
 */
data class FollowQuery(
    val userId: Int,
    val sort: List<UserSort>? = null
) : IGraphQuery {

    /**
     * A map serializer to build maps out of object so that we can consume them
     * using [io.github.wax911.library.model.request.QueryContainerBuilder].
     */
    override fun toMap() = mapOf(
        "userId" to userId,
        "sort" to sort
    )
}